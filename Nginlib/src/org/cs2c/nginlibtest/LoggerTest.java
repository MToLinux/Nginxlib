/**
 * 
 */
package org.cs2c.nginlibtest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.log.LogProfile;
import org.cs2c.nginlib.log.Logger;
import org.cs2c.nginlib.log.RecLogger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.trilead.ssh2.Connection;

/**
 * @author Administrator
 *
 */
public class LoggerTest {
	static private Logger logger;
	static private Connection conn;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/* Create a connection instance */
		conn = new Connection("10.1.50.4", 22);
		
		/* Connect */
		conn.connect();

		/* Authenticate. */
		boolean isAuthenticated = 
		conn.authenticateWithPassword("root", "cs2csolutions");
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		logger=new RecLogger(conn, "/usr/local/nginx");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		conn.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.cs2c.nginlib.log.RecLogger#getLogFileNames()}.
	 */
	@Test
	public void testGetLogFileNames() {
		try {
			List<LogProfile> logs=LoggerTest.logger.getLogFileNames();
			Assert.assertEquals(2, logs.size());
			Assert.assertEquals("access.log", logs.get(0).getName());
			Assert.assertEquals("error.log", logs.get(1).getName());
		} catch (RemoteException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.cs2c.nginlib.log.RecLogger#getLogContent(java.lang.String)}.
	 */
	@Test
	public void testGetLogContent() {
		try {
			String content=LoggerTest.logger.getLogContent("access.log");
			Assert.assertEquals("hello", content);
		} catch (RemoteException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.cs2c.nginlib.log.RecLogger#delete(java.lang.String)}.
	 */
	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.cs2c.nginlib.log.RecLogger#truncate(java.lang.String)}.
	 */
	@Test
	public void testTruncate() {
		try {
			LoggerTest.logger.truncate("access.log");
			String content=LoggerTest.logger.getLogContent("access.log");
			Assert.assertEquals("", content);
		} catch (RemoteException e) {
			fail(e.getMessage());
		}
	}

}
