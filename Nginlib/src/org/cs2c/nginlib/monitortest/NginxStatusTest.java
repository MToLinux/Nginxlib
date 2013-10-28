package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.cs2c.nginlib.monitor.NginxStatus;
import org.cs2c.nginlib.monitor.RecNginxStatus;
import org.cs2c.nginlib.monitor.RecProcessStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NginxStatusTest {
	
	private static NginxStatus ngstat = new RecNginxStatus(null, null, null, null);
	private int ActiveConnections = 1;
	private int ServerAccepts = 1;
	private int ServerHandled = 1;
	private int ServerRequests = 1;
	private int NginxReading = 1;
	private int NginxWriting = 1;
	private int KeepAliveConnections = 1;
	
	private List<RecProcessStatus> listNginxProcessStatus = new LinkedList<RecProcessStatus>();
	private String p_USER = "root";
	private int p_PID = 1234;
	private float p_CPU = 1;
	private float p_MEM = 1;
	private int p_VSZ = 1;
	private int p_RSS = 1;
	private String p_TTY = "?";
	private String p_STAT = "Ss";
	private String p_START = "12:00";
	private String p_TIME = "0:00";
	private String p_COMMAND = "nginx: master process /usr/local/nginx/sbin/nginx";
	
	private List<String> listNginxConfigArgs = new LinkedList<String>();
	private String CA = "--with-http_stub_status_module";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		((RecNginxStatus)ngstat).setActiveConnections(ActiveConnections);
		((RecNginxStatus)ngstat).setServerAccepts(ServerAccepts);
		((RecNginxStatus)ngstat).setServerHandled(ServerHandled);
		((RecNginxStatus)ngstat).setServerRequests(ServerRequests);
		((RecNginxStatus)ngstat).setNginxReading(NginxReading);
		((RecNginxStatus)ngstat).setNginxWriting(NginxWriting);
		((RecNginxStatus)ngstat).setKeepAliveConnections(KeepAliveConnections);
		
		RecProcessStatus pstat = new RecProcessStatus();
		pstat.setProcessUser(p_USER);
		pstat.setProcessID(p_PID);
		pstat.setProcessCPU(p_CPU);
		pstat.setProcessMem(p_MEM);
		pstat.setProcessVSZ(p_VSZ);
		pstat.setProcessRSS(p_RSS);
		pstat.setProcessTTY(p_TTY);
		pstat.setProcessSTAT(p_STAT);
		pstat.setProcessSTART(p_START);
		pstat.setProcessTIME(p_TIME);
		pstat.setProcessCmd(p_COMMAND);
		listNginxProcessStatus.add(pstat);
		((RecNginxStatus)ngstat).setNginxPSList(listNginxProcessStatus);
		
		listNginxConfigArgs.add(CA);
		((RecNginxStatus)ngstat).setNginxCAList(listNginxConfigArgs);
		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGetActiveConnections() {
		assertEquals(ActiveConnections, ngstat.getActiveConnections());
	}

	@Test
	public void testGetServerAccepts() {
		assertEquals(ServerAccepts, ngstat.getServerAccepts());
	}

	@Test
	public void testGetServerHandled() {
		assertEquals(ServerHandled, ngstat.getServerHandled());
	}

	@Test
	public void testGetServerRequests() {
		assertEquals(ServerRequests, ngstat.getServerRequests());
	}

	@Test
	public void testGetNginxReading() {
		assertEquals(NginxReading, ngstat.getNginxReading());
	}

	@Test
	public void testGetNginxWriting() {
		assertEquals(NginxWriting, ngstat.getNginxWriting());
	}

	@Test
	public void testGetKeepAliveConnections() {
		assertEquals(KeepAliveConnections, ngstat.getKeepAliveConnections());
	}

	@Test
	public void testGetNginxPSList() {
		assertEquals(listNginxProcessStatus, ngstat.getNginxPSList());
	}

	@Test
	public void testGetNginxCAList() {
		assertEquals(listNginxConfigArgs, ngstat.getNginxCAList());
	}

}
