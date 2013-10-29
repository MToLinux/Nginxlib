package org.cs2c.nginlib.Junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class RecBlockTest {
	RecBlock orb = new RecBlock();
	static String ConfText = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(path);
		ConfText = rro.ReadConf();

	}

	@Before
	public void setUp() throws Exception {
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
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(path);
		String ConfText;
		try {
			ConfText = rro.ReadConf();
			orb.SetConfText(ConfText);
			orb.setName("server");
			assertFalse(ConfText == orb.toString());
			assertNotNull(orb.toString());
			assertTrue(orb.toString()!="");
			System.out.println(orb.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void IntegrationTestInClass(){
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		//ReadConf ok
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(path);
		String ConfText;
		try {
			ConfText = rro.ReadConf();
			orb.SetConfText(ConfText);
			assertNotNull(orb.toString());
			assertTrue(orb.toString()!="");
//			System.out.println(orb.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testSetConfText() {
		IntegrationTestInClass();
	}

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
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		rro.SetConfpathWithName(path);
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
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		rro.SetConfpathWithName(path);
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
	public final void testGetBlockText() {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("http",1);
//		System.out.println(bltxt);
		
		orb.setName("http");
		String BlockText = orb.toString();
		assertEquals(BlockText,bltxt);

	}
	
	@Test
	public final void testGetBlockText1() {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("server",1);
		System.out.println(bltxt);
		
		orb.setName("server");
		String BlockText = orb.toString();
		assertEquals(BlockText,bltxt);

	}
	@Test
	public final void testGetBlockText2() {
		orb.SetConfText(ConfText);
		
		String bltxt = orb.GetBlockText("events",40);
		System.out.println(bltxt);
		
		orb.setName("events");
		String BlockText = orb.toString();
		assertNotEquals(BlockText,bltxt);

	}
}
