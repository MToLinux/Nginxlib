package org.cs2c.nginlib.configtest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class RecBlockTest {
	RecBlock orb = new RecBlock();
	static String ConfText = null;
	RecConfigurator orc = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String path = "D:\\eclipseWorkspace\\confpath\\";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetLocalConfpath(path);
//		ConfText = rro.ReadConf();
	}

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
	public final void testGetBlocks() {
		Block blhttp = null;
		Map<Integer,Block> myBlocks= null;
		try {
			Block bl= orc.getRootBlock();
			myBlocks = bl.getBlocks();
			Iterator<Entry<Integer, Block>> it = myBlocks.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Block> entry = (Entry<Integer, Block>)it.next();
				String blname = entry.getValue().getName();
				blhttp = entry.getValue();
//				System.out.println(blname+":"+
//						entry.getKey());
			}
			
			//get again
			Map<Integer, Block> myBlockshttp = blhttp.getBlocks();
			Iterator<Entry<Integer, Block>> ithttp = myBlockshttp.entrySet().iterator();
			while(ithttp.hasNext()){
				Entry<Integer, Block> entry = (Entry<Integer, Block>)ithttp.next();
				String blname1 = entry.getValue().getName();
				Block blsubhttpele = entry.getValue();
				System.out.println(blname1+":"+
						entry.getValue().toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testdeleteBlock() {
		Block blhttp = null;
		Map<Integer,Block> myBlocks= null;
		try {
			Block bl= orc.getRootBlock();
			myBlocks = bl.getBlocks();
			
			Iterator<Entry<Integer, Block>> it = myBlocks.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Block> entry = (Entry<Integer, Block>)it.next();
				String blname = entry.getValue().getName();
				blhttp = entry.getValue();
//				System.out.println(blname+":"+
//						entry.getKey());
			}
			
			//delete block
			Map<Integer, Block> myBlockshttp = blhttp.getBlocks();
			Entry<Integer, Block> entry =null;
			Iterator<Entry<Integer, Block>> ithttp = myBlockshttp.entrySet().iterator();
			while(ithttp.hasNext()){
				entry = (Entry<Integer, Block>)ithttp.next();
//				String blname1 = entry.getValue().getName();
//				Block blsubhttpele = entry.getValue();
//				System.out.println(entry.getKey()+":"+
//						entry.getValue());
				blhttp.deleteElement(entry.getKey());
			}
			blhttp.deleteElement(7);
			blhttp.deleteElement(9);
			System.out.println("blhttp :" + blhttp.toString());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testdeleteDirectives() {
		Block blhttp = null;
		Map<Integer,Block> myBlocks= null;
		try {
			Block bl= orc.getRootBlock();
			myBlocks = bl.getBlocks();
			
			Iterator<Entry<Integer, Block>> it = myBlocks.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Block> entry = (Entry<Integer, Block>)it.next();
				String blname = entry.getValue().getName();
				blhttp = entry.getValue();
//				System.out.println(blname+":"+
//						entry.getKey());
			}
			
			//delete block
			Map<Integer, Directive> myBlockshttp = blhttp.getDirectives();
			Entry<Integer, Directive> entry =null;
			Iterator<Entry<Integer, Directive>> ithttp = myBlockshttp.entrySet().iterator();
			while(ithttp.hasNext()){
				entry = (Entry<Integer, Directive>)ithttp.next();
//				String blname1 = entry.getValue().getName();
//				Block blsubhttpele = entry.getValue();
//				System.out.println(entry.getKey()+":"+
//						entry.getValue());
				blhttp.deleteElement(entry.getKey());
			}
		
			System.out.println("blhttp :" + blhttp.toString());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testSetName() {
		String name = "http";
		orb.setName(name);
		assertEquals(name, orb.getName());
	}

	@Test
	public final void testGetName() {
		String name = "server";
		orb.setName(name);
		assertEquals(name, orb.getName());
	}

	@Test
	public final void testSetBlockText() {
		String path = "D:\\eclipseWorkspace\\confpath\\";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetLocalConfpath(path);
		String ConfText = null;
//			ConfText = rro.ReadConf();
//		orb.SetConfText(ConfText);
		orb.setName("server");
		assertFalse(ConfText == orb.toString());
		assertNotNull(orb.toString());
		assertTrue(orb.toString()!="");
		System.out.println(orb.toString());
	}

	private void IntegrationTestInClass(){
		String path = "D:\\eclipseWorkspace\\confpath\\";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetLocalConfpath(path);
		String ConfText = "";
//			ConfText = rro.ReadConf();
//		orb.SetConfText(ConfText);
		assertNotNull(orb.toString());
		assertTrue(orb.toString()!="");
//			System.out.println(orb.toString());
	}
	
	@Test
	public final void testSetConfText() {
		IntegrationTestInClass();
	}
	
/*
	@Test
	public final void testGetBlocks() {
		orb.SetConfText(ConfText);
		orb.setName("http");
		String BlockText = orb.toString();

		List<Block> listGetBlocks = new ArrayList<Block>();
		
		orb.SetBlockText(BlockText);
		try {
			listGetBlocks = orb.getBlocks();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		assertTrue(6 ==listGetBlocks.size());
		
		for(int i = 0;i<listGetBlocks.size(); i++){
			System.out.println(listGetBlocks.get(i)+"$$$###");
		}
	}
	@Test
	public final void testGetBlocks1() {
		orb.SetConfText(ConfText);
		orb.setName("server");
		String BlockText = orb.toString();

		List<Block> listGetBlocks = new ArrayList<Block>();
		
		orb.SetBlockText(BlockText);
		try {
			listGetBlocks = orb.getBlocks();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		assertTrue(3 ==listGetBlocks.size());
		
		for(int i = 0;i<listGetBlocks.size(); i++){
			System.out.println(listGetBlocks.get(i)+"$$$###");
		}
	}
	
	@Test
	public final void testGetDirectives() {
		orb.SetConfText(ConfText);
		orb.setName("events");
		String BlockText = orb.toString();

		List<Directive> list = new ArrayList<Directive>();
		
		orb.SetBlockText(BlockText);
		try {
			list = orb.getDirectives();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		assertTrue(1 ==list.size());
		
		for(int i = 0;i<list.size(); i++){
			System.out.println(list.get(i));
		}
	}
	@Test
	public final void testGetDirectives1() {
		orb.SetConfText(ConfText);
		orb.setName("server");
		String BlockText = orb.toString();

		List<Directive> list = new ArrayList<Directive>();
		
		orb.SetBlockText(BlockText);
		try {
			list = orb.getDirectives();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		assertTrue(9 ==list.size());
		
		for(int i = 0;i<list.size(); i++){
			System.out.println(list.get(i));
		}
	}
	
	@Test
	public final void testAddBlock() throws RemoteException {
		// get list.get(1) block
		RecRemoteOperator rro = new RecRemoteOperator();
		List<Block> list= null;
		String path = "D:\\eclipseWorkspace\\confpath\\";
		rro.SetLocalConfpath(path);
		list= rro.getBlocks("events", "");
//		System.out.println(list.get(1));
		
		//real test start
		orb.SetConfText(ConfText);
		String bltxt = orb.GetBlockText("http",1);
		orb.setName("http");
		orb.SetBlockText(bltxt);
		String srcold = orb.toString();
		orb.addBlock(list.get(1));
		assertNotEquals(srcold,orb.toString());
		System.out.println(orb.toString());
	}

	@Test
	public final void testAddDirective() throws RemoteException {
		// get list.get(1) block
		RecRemoteOperator rro = new RecRemoteOperator();
		List<Block> list= null;
		String path = "D:\\eclipseWorkspace\\confpath\\";
		rro.SetLocalConfpath(path);
		list= rro.getBlocks("events", "");
		
		List<Directive> listd = new ArrayList<Directive>();
		listd = list.get(0).getDirectives();
		
		//real test start
		orb.SetConfText(ConfText);
		String bltxt = orb.GetBlockText("http",1);
		orb.setName("http");
		orb.SetBlockText(bltxt);
		String srcold = orb.toString();
		orb.addDirective(listd.get(0));
		System.out.println(orb.toString());
		assertNotEquals(srcold,orb.toString());
	}

	@Test
	public final void testClone() throws CloneNotSupportedException {
		orb.SetConfText(ConfText);
		orb.setName("http");
		String BlockText = orb.toString();
		
		Element el = orb.clone();

		assertEquals("http",el.getName());
		assertEquals(BlockText,el.toString());
	}

	@Test
	public final void testToString() {
		testSetBlockText();
	}

	@Test
	public final void testGetBlockText() throws RemoteException {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("http",1);
//		System.out.println(bltxt);
		
		orb.setName("http");
		String BlockText = orb.toString();
		assertEquals(BlockText,bltxt);

	}
	
	@Test
	public final void testGetBlockText1() throws RemoteException {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("server",1);
		System.out.println(bltxt);
		
		orb.setName("server");
		String BlockText = orb.toString();
		assertEquals(BlockText,bltxt);

	}
	@Test
	public final void testGetBlockText2() throws RemoteException {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("events",40);
		System.out.println(bltxt);
		
		orb.setName("events");
		String BlockText = orb.toString();
		assertNotEquals(BlockText,bltxt);

	}
	*/
}
