package org.cs2c.nginlib.configtest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RecDirectiveTest {

	RecDirective rd = new RecDirective();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testSetDirectiveText() {
		String name = "worker_connections";
		rd.setName(name);
		String text = "    worker_connections  1024;";
		rd.SetDirectiveText(text);
		assertEquals(text, rd.toString());
		System.out.println(rd.toString());
	}

	@Test
	public final void testSetName() {
		String name = "location ~ \\.php$";
		rd.setName(name);
		assertEquals(name, rd.getName());
		
//		System.out.println(rd.getName());
	}

	@Test
	public final void testGetName() {
		String name = "server";
		rd.setName(name);
		assertEquals(name, rd.getName());
	}

	@Test
	public final void testGetParameters() throws RemoteException {
		// get list.get(1) block
		RecRemoteOperator rro = new RecRemoteOperator();
		List<Block> list= null;
		String path = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		rro.SetConfpathWithName(path);
		list= rro.getBlocks("server", "");
		
		List<Directive> listd = new ArrayList<Directive>();
		listd = list.get(0).getDirectives();
		
		//real test
//		System.out.println(listd.get(listd.size()-2).toString());
		List<Parameter> listp = listd.get(listd.size()-2).getParameters();
		assertTrue(2 ==listp.size());
		for(int i = 0;i<listp.size(); i++){
			System.out.println(listp.get(i));
		}
	}

	@Test
	public final void testToString() {
		testSetDirectiveText();
	}

	@Test
	public final void testAddParameter() {
		String name = "error_page";
		rd.setName(name);
		String text = "    error_page  500;";
		rd.SetDirectiveText(text);
		
		// case 1
		RecOption pa = new RecOption();
		pa.setName("page");
		pa.setValue("401");
		rd.addParameter(pa);
		assertEquals("    error_page  500 page=401;", rd.toString());
		
		// case 2
		RecVariable rv = new RecVariable();
		rv.setName("Variable");
		rd.addParameter(rv);
		assertEquals("    error_page  500 page=401 $Variable;", rd.toString());

		// case 3
		RecStringParameter rsp = new RecStringParameter();
		rsp.setValue("StringParameter");
		rd.addParameter(rsp);
		assertEquals("    error_page  500 page=401 $Variable StringParameter;", rd.toString());
	}

	@Test
	public final void testClone() throws CloneNotSupportedException {
		String name = "worker_connections";
		rd.setName(name);
		String text = "    worker_connections  1024;";
		rd.SetDirectiveText(text);
		
		Element el = rd.clone();
		
		assertEquals(name, el.getName());
		assertEquals(text, el.toString());
		System.out.println(rd.toString());
	}

}
