package org.cs2c.nginlib.configtest;

import static org.junit.Assert.*;

import java.util.List;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.*;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

public class RecConfiguratorTest {

	String midwarePath;
	RecAuthInfo authInfo=new RecAuthInfo();
	MiddlewareFactory instance = null;
	RecConfigurator orc = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		AuthInfo authInfo;
		authInfo=MiddlewareFactory.newAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
		MiddlewareFactory instance= null;

		instance = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx/");
		orc = (RecConfigurator) instance.getConfigurator();
	}

	@Test
	public final void testSetConfpathWithName() {
		String PathWithName = "D:\\eclipseWorkspace\\confpath\\";
		orc.SetLocalConfpath(PathWithName);
	}

	@Test
	public final void testAppend(){
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		try {
			setUp();
			testSetConfpathWithName();
			// case 1:
			blockName = "events";
			list= orc.getBlocks(blockName, outerBlockNames);
			System.out.println(list.size());

	//		assertEquals(1, list.size());
	//		
			outerBlockNames = "http";
			orc.append(list.get(0), outerBlockNames);
	//
	//		outerBlockNames = "";
	//		list= orc.getBlocks(blockName, outerBlockNames);
	//		assertEquals(2, list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testAppend1() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "events";
		bl.setName(blockName);
		String BlockText="events {"+"\n"
						+"    worker_connections  2000;"+"\n"
						+"}";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(1, list.size());
		
		outerBlockNames = "events1";
		orc.append(bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(2, list.size());
	}
	
	@Test
	public final void testAppendWithOpration() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		
		testSetConfpathWithName();
		
		Block newBlock = orc.newBlock();
		newBlock.setName("server");
		//make Directive : server_name
			RecDirective rdserver_name = new RecDirective();
			rdserver_name.setName("server_name");
				RecStringParameter param1 = new RecStringParameter();
				param1.setValue("sernameval");
			rdserver_name.addParameter(param1);
		newBlock.addDirective(rdserver_name);
		//make Directive : listen
			RecDirective rd = new RecDirective();
			rd.setName("listen");
				RecStringParameter param = new RecStringParameter();
				param.setValue("80");
			rd.addParameter(param);
		newBlock.addDirective(rd);
		
		//add the new server to conf,first do getBlocks and get datastamp
		blockName = "http";

//		List<Block> list= orc.getBlocks(blockName, outerBlockNames);
		List<Block> list= orc.getBlocks("server", "http:0");
		
//		System.out.println("getBlocks:"+list.size());
		if(list.size()>0){
//			System.out.println("Start append");
			// case1:
			orc.append(newBlock, blockName);
			// case2:
//			orc.append(newBlock, "http:0|server:0");
			// case delete:
//			orc.delete(newBlock, "http:0");
		}
//		assertEquals(2, list.size());
	}
	
	@Test
	public final void testDelete() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "events";
//		bl.setName(blockName);
//		String BlockText="events {"+"/n"
//						+"    worker_connections  1024;"+"/n"
//						+"}";
//		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(2, list.size());

		orc.delete(list.get(0), outerBlockNames);

//		outerBlockNames = "";
//		list= orc.getBlocks(blockName, outerBlockNames);
//		assertEquals(0, list.size());

	}

	@Test
	public final void testInsertAfter() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks("server", outerBlockNames);
		
		outerBlockNames = "http";
		orc.insertAfter(list.get(0), bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks("InsertAfter", outerBlockNames);
		assertEquals(1, list.size());
	}

	@Test
	public final void testReplace() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }"+"\n";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks("events", outerBlockNames);
		
		outerBlockNames = "events1";
		orc.replace(list.get(0), bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks("InsertAfter", outerBlockNames);
		assertEquals(1, list.size());
	}
	@Test
	public final void testgetRootBlock() {

		List<Block> list= null;
		
		try {
			list= orc.getRootBlock();
			for(int i = 0;i< list.size();i++){
//				System.out.println(list.get(i).getName());
				
				System.out.println(list.get(i).getName()+" :"+list.get(i).toString());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public final void testGetBlocks() {
		String blockName = null;
		String outerBlockNames = "";
		List<Block> list= null;
		try {
//			testSetConfpathWithName();

			// case 1
//			blockName = "events";
//			list= orc.getBlocks(blockName, outerBlockNames);
//			assertEquals(2, list.size());
//			assertEquals(blockName, list.get(0).getName());
			
//			// case 2
			blockName = "server";
			list= orc.getBlocks(blockName, "http");
			System.out.println(list.get(0).toString());
			
//			for(int i = 0;i< list.get(0).getDirectives().size();i++){
//				System.out.println(list.get(0).getDirectives().get(i).toString());
//			}
//			System.out.println("list.size() :"+list.size());
//			assertEquals(2, list.size());
//			assertEquals(blockName, list.get(0).getName());
//			
//			// case 3
//			blockName = "location /";
//			list= orc.getBlocks(blockName, outerBlockNames);
//			assertEquals(2, list.size());
//			assertEquals(blockName, list.get(0).getName());
//			assertEquals(blockName, list.get(1).getName());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	@Test
	public final void testGetBlocks1() {
		String blockName = null;
		String outerBlockNames = "http:0";
		List<Block> list= null;
		try {
			testSetConfpathWithName();
			
			// case 2
			blockName = "server";
			list= orc.getBlocks(blockName, outerBlockNames);
			assertEquals(1, list.size());
			assertEquals(blockName, list.get(0).getName());

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	@Test
	public final void testNewBlock() {
		Block op = orc.newBlock();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewDirective() {
		Directive op = orc.newDirective();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewVariable() {
		Variable op = orc.newVariable();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewStringParameter() {
		StringParameter op = orc.newStringParameter();		
		assertNotNull(op);
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewOption() {
		Option op = orc.newOption();

		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

}
