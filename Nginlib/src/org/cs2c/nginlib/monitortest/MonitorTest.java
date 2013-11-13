package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.monitor.CPUStatus;
import org.cs2c.nginlib.monitor.Device;
import org.cs2c.nginlib.monitor.IOStatus;
import org.cs2c.nginlib.monitor.MemoryStatus;
import org.cs2c.nginlib.monitor.Monitor;
import org.cs2c.nginlib.monitor.NetworkStatus;
import org.cs2c.nginlib.monitor.NginxStatus;
import org.cs2c.nginlib.monitor.RecMonitor;
import org.cs2c.nginlib.monitor.RecProcessStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trilead.ssh2.Connection;

public class MonitorTest {
	
	private static Monitor monitor = null;
	private static Connection conn;
	private static String hostname = "127.0.0.1";
	private static String username = "root";
	private static String password = "qwer1234";
	private static RecAuthInfo auInfo = null;
	private static int port = 22;
	private static String nginxpath = "/usr/local/nginx/";
	
	private static boolean StatusModuleFlag = false;
	
	public static void establishConnection() throws IOException {
		/* Create a connection instance */
		conn = new Connection(hostname, port);
		
		/* Connect */
		conn.connect();

		/* Authenticate. */
		boolean isAuthenticated = 
		conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
	}
	public static void CloseConnection() throws IOException {
		conn.close();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		hostname = "10.1.50.4";
		username = "root";
		password = "cs2csolutions";
		port = 22;
		nginxpath = "/usr/local/nginx/";
		
		StatusModuleFlag = true;
		
		auInfo = new RecAuthInfo();
		auInfo.setHost(hostname);
		auInfo.setUsername(username);
		auInfo.setPassword(password);
		
		establishConnection();
		
		monitor = new RecMonitor(auInfo, nginxpath, conn);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		CloseConnection();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(timeout=10000)
	public void testGetCPUStatus() throws RemoteException {

		float expectedUserPercent = 0;
		float expectedSystemPercent = 0;
		float expectedIdlePercent = 0;
		float expectedIOWaitPercent = 0;
		
		float UserPercentDelta = 100;
		float SystemPercentDelta = 100;
		float IdlePercentDelta = 100;
		float IOWaitPercentDelta = 100;

		
		CPUStatus cpustat = monitor.getCPUStatus();
		
		assertTrue(cpustat.getRunningNum() >= 0);
		assertTrue(cpustat.getBlockingNum() >= 0);
		assertTrue(cpustat.getInterruptNum() >= 0);
		assertTrue(cpustat.getContextSwitchNum() >= 0);
		assertTrue(cpustat.getUserPercent() >= 0);
		assertTrue(cpustat.getSystemPercent() >= 0);
		assertTrue(cpustat.getIdlePercent() >= 0);
		assertTrue(cpustat.getIOWaitPercent() >= 0);
		assertEquals(expectedUserPercent, cpustat.getUserPercent(), UserPercentDelta);
		assertEquals(expectedSystemPercent, cpustat.getSystemPercent(), SystemPercentDelta);
		assertEquals(expectedIdlePercent, cpustat.getIdlePercent(), IdlePercentDelta);
		assertEquals(expectedIOWaitPercent, cpustat.getIOWaitPercent(), IOWaitPercentDelta);


		//print
		System.out.println("CPU Status:");
		System.out.println("RunningNum:" + cpustat.getRunningNum());
		System.out.println("BlockingNum:" + cpustat.getBlockingNum());
		System.out.println("InterruptNum:" + cpustat.getInterruptNum());
		System.out.println("ContextSwitchNum:" + cpustat.getContextSwitchNum());
		System.out.println("UserPercent:" + cpustat.getUserPercent());
		System.out.println("SystemPercent:" + cpustat.getSystemPercent());
		System.out.println("IdlePercent:" + cpustat.getIdlePercent());
		System.out.println("IOWaitPercent:" + cpustat.getIOWaitPercent());

		System.out.println("");
	}

	@Test(timeout=10000)
	public void testGetIOStatus() throws RemoteException {
		
		float expectedBlockInPerSec = 0;
		float expectedBlockOutPerSec = 0;
		
		float BlockInPerSecDelta = Float.MAX_VALUE;
		float BlockOutPerSecDelta = Float.MAX_VALUE;
		
		String unexpectedName = "";
		float expectedBlockWritenPerSec = 0;
		float expectedBlockReadPerSec = 0;
		float expectedTPS = 0;

		float BlockWritenPerSecDelta = Float.MAX_VALUE;
		float BlockReadPerSecDelta = Float.MAX_VALUE;
		float TPSDelta = Float.MAX_VALUE;
		
		
		IOStatus iostat = monitor.getIOStatus();
		
		assertTrue(iostat.getBlockInPerSec() >= 0);
		assertTrue(iostat.getBlockOutPerSec() >= 0);
		assertEquals(expectedBlockInPerSec, iostat.getBlockInPerSec(), BlockInPerSecDelta);
		assertEquals(expectedBlockOutPerSec, iostat.getBlockOutPerSec(), BlockOutPerSecDelta);

		//Devices
		List<Device> Devices = new LinkedList<Device>();
		Devices = iostat.getDevices();
		
		assertTrue(!Devices.isEmpty());
		
		Iterator<Device> it = Devices.iterator();
		while (it.hasNext())
		{
			Device dev = it.next();
			
			assertTrue(!(dev.getName().isEmpty()) && (dev.getName() != unexpectedName));
			assertTrue(dev.getBlockWritenPerSec() >= 0);
			assertTrue(dev.getBlockReadPerSec() >= 0);
			assertTrue(dev.getBlockWriten() >= 0);
			assertTrue(dev.getBlockRead() >= 0);
			assertTrue(dev.getTPS() >= 0);
			assertEquals(expectedBlockWritenPerSec, dev.getBlockWritenPerSec(), BlockWritenPerSecDelta);
			assertEquals(expectedBlockReadPerSec, dev.getBlockReadPerSec(), BlockReadPerSecDelta);
			assertEquals(expectedTPS, dev.getTPS(), TPSDelta);
		}

		
		//print
		System.out.println("IO Status:");
		System.out.println("BlockInPerSec:" + iostat.getBlockInPerSec());
		System.out.println("BlockOutPerSec:" + iostat.getBlockOutPerSec());
		it = Devices.iterator();
		while (it.hasNext())
		{
			Device dev = it.next();
			System.out.println(
					dev.getName() + " " +
					dev.getTPS() + " " +
					dev.getBlockReadPerSec() + " " +
					dev.getBlockWritenPerSec() + " " +
					dev.getBlockRead() + " " +
					dev.getBlockWriten());
		}
		
		System.out.println("");
	}

	@Test(timeout=10000)
	public void testGetNetworkStatus() throws RemoteException {
		
		float expectedInputKbPerSec = 0;
		float expectedOutputPerSec = 0;
		
		float InputKbPerSecDelta = Float.MAX_VALUE;
		float OutputPerSecDelta = Float.MAX_VALUE;
		
		NetworkStatus netwstat = monitor.getNetworkStatus();

		assertTrue(netwstat.getInputKbPerSec() >= 0);
		assertTrue(netwstat.getOutputPerSec() >= 0);
		assertEquals(expectedInputKbPerSec, netwstat.getInputKbPerSec(), InputKbPerSecDelta);
		assertEquals(expectedOutputPerSec, netwstat.getOutputPerSec(), OutputPerSecDelta);
		
		//print
		System.out.println("Network Status:");
		System.out.println("InputKbPerSec:" + netwstat.getInputKbPerSec());
		System.out.println("OutputPerSec:" + netwstat.getOutputPerSec());

		System.out.println("");
	}

	@Test(timeout=10000)
	public void testGetMemoryStatus() throws RemoteException {

		MemoryStatus memstat = monitor.getMemoryStatus();
		
		assertTrue(memstat.getTotalSwap() >= 0);
		assertTrue(memstat.getUsedSwap() >= 0);
		assertTrue(memstat.getSwapIn() >= 0);
		assertTrue(memstat.getSwapOut() >= 0);
		assertTrue(memstat.getFree() >= 0);
		assertTrue(memstat.getBuffers() >= 0);
		assertTrue(memstat.getShared() >= 0);
		assertTrue(memstat.getCached() >= 0);
		assertTrue(memstat.getUsed() >= 0);
		
		//print
		System.out.println("Memory Status:");
		System.out.println("TotalSwap:" + memstat.getTotalSwap());
		System.out.println("UsedSwap:" + memstat.getUsedSwap());
		System.out.println("SwapIn:" + memstat.getSwapIn());
		System.out.println("SwapOut:" + memstat.getSwapOut());
		System.out.println("Free:" + memstat.getFree());
		System.out.println("Buffers:" + memstat.getBuffers());
		System.out.println("Shared:" + memstat.getShared());
		System.out.println("Cached:" + memstat.getCached());
		System.out.println("Used:" + memstat.getUsed());
		
		System.out.println("");
	}

	@Test(timeout=10000)
	public void testGetNginxStatus() throws RemoteException {
		
		NginxStatus ngstat = monitor.getNginxStatus(StatusModuleFlag, null, null, null);
		if(StatusModuleFlag)
		{
			assertTrue(ngstat.getActiveConnections() >= 0);
			assertTrue(ngstat.getServerAccepts() >= 0);
			assertTrue(ngstat.getServerHandled() >= 0);
			assertTrue(ngstat.getServerRequests() >= 0);
			assertTrue(ngstat.getNginxReading() >= 0);
			assertTrue(ngstat.getNginxWriting() >= 0);
			assertTrue(ngstat.getKeepAliveConnections() >= 0);
		}


		//Nginx Process
		List<RecProcessStatus> ngpslist = new LinkedList<RecProcessStatus>();
		ngpslist = ngstat.getNginxPSList();
		
		assertTrue(!ngpslist.isEmpty());
		
		Iterator<RecProcessStatus> it = ngpslist.iterator();
		while (it.hasNext())
		{
			RecProcessStatus nps = it.next();
			
			String unexpectedUser = "";
			float expectedCPU = 0;
			float expectedMEM = 0;
			float CPUDelta = Float.MAX_VALUE;
			float MEMDelta = Float.MAX_VALUE;
			String unexpectedTTY = "";
			String unexpectedSTAT = "";
			String unexpectedSTART = "";
			String unexpectedTIME = "";
			String unexpectedCOMMAND = "";
			
			assertTrue(!(nps.getProcessUser().isEmpty()) && (nps.getProcessUser() != unexpectedUser));
			assertTrue(nps.getProcessID() >= 0);
			assertTrue(nps.getProcessCPU() >= 0);
			assertTrue(nps.getProcessMem() >= 0);
			assertEquals(expectedCPU, nps.getProcessCPU(), CPUDelta);
			assertEquals(expectedMEM, nps.getProcessMem(), MEMDelta);
			assertTrue(nps.getProcessVSZ() >= 0);
			assertTrue(nps.getProcessRSS() >= 0);
			assertTrue(!(nps.getProcessTTY().isEmpty()) && (nps.getProcessTTY() != unexpectedTTY));
			assertTrue(!(nps.getProcessSTAT().isEmpty()) && (nps.getProcessSTAT() != unexpectedSTAT));
			assertTrue(!(nps.getProcessSTART().isEmpty()) && (nps.getProcessSTART() != unexpectedSTART));
			assertTrue(!(nps.getProcessTIME().isEmpty()) && (nps.getProcessTIME() != unexpectedTIME));
			assertTrue(!(nps.getProcessCmd().isEmpty()) && (nps.getProcessCmd() != unexpectedCOMMAND));
		}
		
		List<String> ngcalist = new LinkedList<String>();
		ngcalist = ngstat.getNginxCAList();
		Iterator<String> cait = ngcalist.iterator();
		while (cait.hasNext())
		{
			String unexpectedCA = "";
			String nextStr = cait.next();
			assertTrue(!(nextStr.isEmpty()) && (nextStr != unexpectedCA));
		}


		//print
		System.out.println("Nginx Status:");
		if(StatusModuleFlag)
		{
			System.out.println("ActiveConnections:" + ngstat.getActiveConnections());
			System.out.println("ServerAccepts:" + ngstat.getServerAccepts());
			System.out.println("ServerHandled:" + ngstat.getServerHandled());
			System.out.println("ServerRequests:" + ngstat.getServerRequests());
			System.out.println("NginxReading:" + ngstat.getNginxReading());
			System.out.println("NginxWriting:" + ngstat.getNginxWriting());
			System.out.println("KeepAliveConnections:" + ngstat.getKeepAliveConnections());
		}
		else
		{
			System.out.println("Nginx status module is not installed!");
		}

		it = ngpslist.iterator();
		System.out.println("NginxProcessListSize:" + ngpslist.size());
		while (it.hasNext())
		{
			RecProcessStatus nps = it.next();
			System.out.println(
					nps.getProcessUser() + " " +
					nps.getProcessID() + " " +
					nps.getProcessCPU() + " " +
					nps.getProcessMem() + " " +
					nps.getProcessVSZ() + " " +
					nps.getProcessRSS() + " " +
					nps.getProcessTTY() + " " +
					nps.getProcessSTAT() + " " +
					nps.getProcessSTART() + " " +
					nps.getProcessTIME() + " " +
					nps.getProcessCmd() );
		}

		cait = ngcalist.iterator();
		System.out.println("NginxCAListSize:" + ngcalist.size());
		System.out.println("configure arguments:");
		while (cait.hasNext())
		{
			System.out.println(cait.next() + " ");
		}
		
		System.out.println("");
	}

}
