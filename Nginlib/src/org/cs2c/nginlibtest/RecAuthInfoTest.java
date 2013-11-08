package org.cs2c.nginlibtest;

import org.cs2c.nginlib.RecAuthInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RecAuthInfoTest extends TestCase {

	RecAuthInfo authInfo = new RecAuthInfo();

	@Before
	protected void setUp() throws Exception {

		super.setUp();

	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testSetHost() {
		String hostName = "10.1.50.4";
		authInfo.setHost(hostName);
		Assert.assertEquals(hostName, authInfo.getHostname());

	}

	@Test
	public void testSetUsername() {
		String userName = "root";
		authInfo.setUsername(userName);
		Assert.assertEquals(userName, authInfo.getUsername());

	}

	@Test
	public void testSetPassword() {
		String passWord = "qwer1234";
		authInfo.setPassword(passWord);
		Assert.assertEquals(passWord, authInfo.getPassword());

	}

}
