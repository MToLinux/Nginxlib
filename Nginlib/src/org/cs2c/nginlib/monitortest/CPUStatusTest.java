package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import org.cs2c.nginlib.monitor.CPUStatus;
import org.cs2c.nginlib.monitor.RecCPUStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CPUStatusTest {

	private static CPUStatus cpustat = new RecCPUStatus();
	
	private int RunningNum = 1;
	private int BlockingNum = 1;
	private int InterruptNum = 1;
	private int ContextSwitchNum = 1;
	private float UserPercent = 1;
	private float SystemPercent = 1;
	private float IdlePercent = 1;
	private float IOWaitPercent = 1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		((RecCPUStatus)cpustat).setRunningNum(RunningNum);
		((RecCPUStatus)cpustat).setBlockingNum(BlockingNum);
		((RecCPUStatus)cpustat).setInterruptNum(InterruptNum);
		((RecCPUStatus)cpustat).setContextSwitchNum(ContextSwitchNum);
		((RecCPUStatus)cpustat).setUserPercent(UserPercent);
		((RecCPUStatus)cpustat).setSystemPercent(SystemPercent);
		((RecCPUStatus)cpustat).setIdlePercent(IdlePercent);
		((RecCPUStatus)cpustat).setIOWaitPercent(IOWaitPercent);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRunningNum() {
		//fail("Not yet implemented");
		assertEquals(RunningNum, cpustat.getRunningNum());
		
	}

	@Test
	public void testGetBlockingNum() {
		assertEquals(BlockingNum, cpustat.getBlockingNum());
	}

	@Test
	public void testGetInterruptNum() {
		assertEquals(InterruptNum, cpustat.getInterruptNum());
	}

	@Test
	public void testGetContextSwitchNum() {
		assertEquals(ContextSwitchNum, cpustat.getContextSwitchNum());
	}

	@Test
	public void testGetUserPercent() {
		assertEquals(UserPercent, cpustat.getUserPercent(), 0);
	}

	@Test
	public void testGetSystemPercent() {
		assertEquals(SystemPercent, cpustat.getSystemPercent(), 0);
	}

	@Test
	public void testGetIdlePercent() {
		assertEquals(IdlePercent, cpustat.getIdlePercent(), 0);
	}

	@Test
	public void testGetIOWaitPercent() {
		assertEquals(IOWaitPercent, cpustat.getIOWaitPercent(), 0);
	}

}
