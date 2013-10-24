package org.cs2c.nginlib.Junit;

import static org.junit.Assert.*;

import java.util.List;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.Block;
import org.cs2c.nginlib.config.Element;
import org.cs2c.nginlib.config.RecBlock;
import org.cs2c.nginlib.config.RecRemoteOperator;
import org.junit.Before;
import org.junit.Test;

public class RecRemoteOperatorTest {
	RecRemoteOperator rro = new RecRemoteOperator();
	
	@Before
	public void setUp() throws Exception {
		//ReadConf ok

	}

	@Test
	public final void testRecRemoteOperator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRecRemoteOperatorAuthInfoString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetConfpathWithName() {
		testReadConf();
	}

	@Test
	public final void testAppend() {
//		append(Element element, String outerBlockNames);
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testInsertAfter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testReplace() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBlocks() {
		String blockName = "events";
		String outerBlockNames = "";
		try {
			List<Block> list= rro.getBlocks(blockName, outerBlockNames);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetRemoteConf() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWriteRemoteConf() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testReadConf() {
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		rro.SetConfpathWithName(path);
		String conftext = null;
		try {
			conftext = rro.ReadConf();
		} catch (RemoteException e) {
			e.printStackTrace();
			fail("have RemoteException");
		}
		assertNotNull("conftext", conftext);
	}

}
