package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import org.cs2c.nginlib.monitor.NetworkStatus;
import org.cs2c.nginlib.monitor.RecNetworkStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NetworkStatusTest {
	
	private static NetworkStatus nwstat = new RecNetworkStatus();
	private float InputKbPerSec = 0;
	private float OutputPerSec = 0;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		((RecNetworkStatus)nwstat).setInputKbPerSec(InputKbPerSec);
		((RecNetworkStatus)nwstat).setOutputKbPerSec(OutputPerSec);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInputKbPerSec() {
		assertEquals(InputKbPerSec, nwstat.getInputKbPerSec(), 0);
	}

	@Test
	public void testGetOutputPerSec() {
		assertEquals(OutputPerSec, nwstat.getOutputKbPerSec(), 0);
	}

}
