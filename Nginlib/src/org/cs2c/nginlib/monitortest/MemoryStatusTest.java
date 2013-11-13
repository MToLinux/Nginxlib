package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import org.cs2c.nginlib.monitor.MemoryStatus;
import org.cs2c.nginlib.monitor.RecMemoryStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryStatusTest {
	
	private static MemoryStatus mmstat = new RecMemoryStatus();
	private int TotalSwap = 1;
	private int UsedSwap = 1;
	private int SwapIn = 1;
	private int SwapOut = 1;
	private int Free = 1;
	private int Buffers = 1;
	private int Shared = 1;
	private int Cached = 1;
	private int Used = 1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		((RecMemoryStatus)mmstat).setTotalSwap(TotalSwap);
		((RecMemoryStatus)mmstat).setUsedSwap(UsedSwap);
		((RecMemoryStatus)mmstat).setSwapIn(SwapIn);
		((RecMemoryStatus)mmstat).setSwapOut(SwapOut);
		((RecMemoryStatus)mmstat).setFree(Free);
		((RecMemoryStatus)mmstat).setBuffers(Buffers);
		((RecMemoryStatus)mmstat).setShared(Shared);
		((RecMemoryStatus)mmstat).setCached(Cached);
		((RecMemoryStatus)mmstat).setUsed(Used);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTotalSwap() {
		assertEquals(TotalSwap, mmstat.getTotalSwap());
	}
	
	@Test
	public void testGetUsedSwap() {
		assertEquals(UsedSwap, mmstat.getUsedSwap());
	}

	@Test
	public void testGetSwapIn() {
		assertEquals(SwapIn, mmstat.getSwapIn());
	}

	@Test
	public void testGetSwapOut() {
		assertEquals(SwapOut, mmstat.getSwapOut());
	}

	@Test
	public void testGetFree() {
		assertEquals(Free, mmstat.getFree());
	}

	@Test
	public void testGetBuffers() {
		assertEquals(Buffers, mmstat.getBuffers());
	}

	@Test
	public void testGetShared() {
		assertEquals(Shared, mmstat.getShared());
	}

	@Test
	public void testGetCached() {
		assertEquals(Cached, mmstat.getCached());
	}

	@Test
	public void testGetUsed() {
		assertEquals(Used, mmstat.getUsed());
	}

}
