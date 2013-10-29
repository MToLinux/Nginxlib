package org.cs2c.nginlib.Junit;

import static org.junit.Assert.*;

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
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetName() {
		String name = "location ~ \\.php$";
		rd.setName(name);
		assertEquals(name, rd.getName());
		
		System.out.println(rd.getName());
	}

	@Test
	public final void testGetName() {
		String name = "server";
		rd.setName(name);
		assertEquals(name, rd.getName());
	}

	@Test
	public final void testGetParameters() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddParameter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

}
