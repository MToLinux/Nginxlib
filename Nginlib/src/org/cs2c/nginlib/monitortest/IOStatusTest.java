package org.cs2c.nginlib.monitortest;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.cs2c.nginlib.monitor.Device;
import org.cs2c.nginlib.monitor.IOStatus;
import org.cs2c.nginlib.monitor.RecDevice;
import org.cs2c.nginlib.monitor.RecIOStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IOStatusTest {
	
	private static IOStatus iostat = new RecIOStatus();
	private float BlockInPerSec = 1;
	private float BlockOutPerSec = 1;
	
	private List<Device> Devices = new LinkedList<Device>();
	private String Name = "sda";
	private float BlockWritenPerSec = 1;
	private float BlockReadPerSec = 1;
	private long BlockWriten = 1;
	private long BlockRead = 1;
	private float TPS = 1;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		((RecIOStatus)iostat).setBlockInPerSec(BlockInPerSec);
		((RecIOStatus)iostat).setBlockOutPerSec(BlockOutPerSec);
		
		RecDevice device = new RecDevice();
		device.setBlockRead(BlockRead);
		device.setBlockReadPerSec(BlockReadPerSec);
		device.setBlockWriten(BlockWriten);
		device.setBlockWritenPerSec(BlockWritenPerSec);
		device.setName(Name);
		device.setTPS(TPS);
		Devices.add(device);
		((RecIOStatus)iostat).setDevices(Devices);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBlockInPerSec() {
		assertEquals(BlockInPerSec, iostat.getBlockInPerSec(), 0);
	}

	@Test
	public void testGetBlockOutPerSec() {
		assertEquals(BlockOutPerSec, iostat.getBlockOutPerSec(), 0);
	}

	@Test
	public void testGetDevices() {
		assertEquals(Devices, iostat.getDevices());
	}

}
