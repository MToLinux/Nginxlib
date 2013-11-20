package org.cs2c.nginlib.configtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.Block;
import org.cs2c.nginlib.config.Directive;
import org.cs2c.nginlib.config.Element;
import org.cs2c.nginlib.config.RecBlock;
import org.cs2c.nginlib.config.RecRemoteOperator;
import org.junit.Before;
import org.junit.Test;

import com.trilead.ssh2.Connection;

public class RecRemoteOperatorTest {
	RecRemoteOperator rro = new RecRemoteOperator();
	String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
	
	@Before
	public void setUp() throws Exception {
		//ReadConf ok

		rro.SetLocalConfpath(path);
	}

	@Test
	public final void testRecRemoteOperator() {
		RecRemoteOperator rro1 = new RecRemoteOperator();
		assertTrue(null != rro1);
	}

	@Test
	public final void testRecRemoteOperatorAuthInfoString() throws IOException, RemoteException {
		String midwarePath = "/root/nginx/";
		AuthInfo authInfo= new RecAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");


		Connection conn = new Connection("10.1.50.4");
		/* Now connect */
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				"root", "cs2csolutions");
		if (isAuthenticated == false)
			throw new RemoteException("Authentication failed.");
		
		RecRemoteOperator rro1 = new RecRemoteOperator(authInfo,midwarePath,conn);

		assertTrue(null != rro1);
	}

	@Test
	public final void testAppend() {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		
		try {
			// case 1:
//			String oldStr = rro.ReadConf();
			blockName = "events";
			list= rro.getBlocks(blockName, outerBlockNames);
			
			outerBlockNames = "events";
			rro.append(list.get(0), outerBlockNames);
//			String afterAppendStr = rro.ReadConf();
//			assertNotEquals(oldStr, afterAppendStr);
//			System.out.println(afterAppendStr);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
//	
//	@Test
//	public final void testAppend2() {
//		String blockName = null;
////		outerBlockNames can be "http:0|server:0"
//		String outerBlockNames = "";
//		List<Block> list= null;
//		
////		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
////
////		rro.SetConfpathWithName(path);
//		
//		try {
//			// case 1:
//			String oldStr = rro.ReadConf();
//			blockName = "events";
//			list= rro.getBlocks(blockName, outerBlockNames);
//			
//			outerBlockNames = "http:0|server:0";
//			rro.append(list.get(0), outerBlockNames);
//			String afterAppendStr = rro.ReadConf();
//			assertNotEquals(oldStr, afterAppendStr);
//			System.out.println(afterAppendStr);
//
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}

	@Test
	public final void testAppendDirective() {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
//		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
//
//		rro.SetConfpathWithName(path);
		
		try {
			// case 1:
//			String oldStr = rro.ReadConf();
			blockName = "events";
			list= rro.getBlocks(blockName, outerBlockNames);
			List<Directive> listdi = list.get(0).getDirectives();
			
			outerBlockNames = "events";
			rro.append(list.get(0), outerBlockNames);
//			String afterAppendStr = rro.ReadConf();
//			assertNotEquals(oldStr, afterAppendStr);
//			System.out.println(afterAppendStr);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testDelete() {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
//		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
//
//		rro.SetConfpathWithName(path);

	}
	
	
	@Test
	public final void testDelete2() {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "http:0|server:0|location /:0";
		List<Block> list= null;
		
//		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
//
//		rro.SetConfpathWithName(path);
		
//		try {
//			// case 1:
//			String oldStr = rro.ReadConf();
//			
//			blockName = "location /";
//			list= rro.getBlocks(blockName, outerBlockNames);
//			rro.delete(list.get(0), "http:0|server:0|location /:0");
//			String afterdelStr = rro.ReadConf();
//			assertNotEquals(oldStr, afterdelStr);
//			System.out.println(afterdelStr);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}
	@Test
	public final void testInsertAfter() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
//		String oldStr = rro.ReadConf();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }";
		bl.SetBlockText(BlockText);
		
		list= rro.getBlocks("events", outerBlockNames);
		
		outerBlockNames = "http:0|events1:0";
		rro.insertAfter(list.get(0), bl, outerBlockNames);

//		outerBlockNames = "";
//		list= rro.getBlocks("InsertAfter", outerBlockNames);
//		System.out.println(list.size());
//		assertEquals(1, list.size());
	}

	@Test
	public final void testReplace() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
//		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }"+"\n";
		bl.SetBlockText(BlockText);
		
		list= rro.getBlocks("events", outerBlockNames);
		
		outerBlockNames = "events1";
		rro.replace(list.get(0), bl, outerBlockNames);

	}

	@Test
	public final void testGetBlocks() {
		String blockName = null;
		String outerBlockNames = "";
		List<Block> list= null;
		try {
			// case 1
			blockName = "events";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(2, list.size());
//			System.out.println(list.get(0).getName());
			assertEquals(blockName, list.get(0).getName());
			
			// case 2
			blockName = "server";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(1, list.size());
			assertEquals(blockName, list.get(0).getName());
			
			// case 3
			blockName = "location /";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(2, list.size());
			assertEquals(blockName, list.get(0).getName());
			assertEquals(blockName, list.get(1).getName());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testGetBlocks1() {
		String blockName = null;
		String outerBlockNames = "http";
		List<Block> list= null;
		try {
			// case 1
			blockName = "events";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(1, list.size());
//			System.out.println(list.get(0).getName());
			assertEquals(blockName, list.get(0).getName());
			
			// case 2
			blockName = "server";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(1, list.size());
			assertEquals(blockName, list.get(0).getName());
			
			// case 3
			blockName = "location /";
			list= rro.getBlocks(blockName, outerBlockNames);
			assertEquals(2, list.size());
			assertEquals(blockName, list.get(0).getName());
			assertEquals(blockName, list.get(1).getName());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/*
	@Test
	public final void testGetRemoteConf() {
		AuthInfo authInfo= new RecAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
		String remoteTargetConf = "/root/nginx/conf/nginx.conf";
		String remoteTargetDirectory = "/root/nginx/conf/";
		
		
		RecRemoteOperator objRc = new RecRemoteOperator(authInfo, remoteTargetDirectory);
		try {
			File fi = new File(path);
			assertFalse(fi.exists());
			objRc.SetConfpathWithName(path);
			
			objRc.GetRemoteConf(remoteTargetConf);

			assertTrue(fi.exists());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public final void testWriteRemoteConf() {
		AuthInfo authInfo= new RecAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
//		String remoteTargetConf = "/root/nginx/conf/nginx.conf";
		String remoteTargetDirectory = "/root/nginx/conf/";
		
		RecRemoteOperator objRc = new RecRemoteOperator(authInfo, remoteTargetDirectory);
		try {
			File fi = new File(path);
			assertTrue(fi.exists());
			objRc.SetConfpathWithName(path);
			
			objRc.WriteRemoteConf();

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
*/
}
