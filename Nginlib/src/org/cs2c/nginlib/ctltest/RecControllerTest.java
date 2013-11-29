package org.cs2c.nginlib.ctltest;

import java.io.File;
import java.io.IOException;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.ctl.RecController;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class RecControllerTest extends TestCase {

		MiddlewareFactory instance= null;
		RecController testController=null;
		
	protected void setUp() throws Exception{
		super.setUp();
		AuthInfo authInfo=null;
		authInfo=MiddlewareFactory.newAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");

		
		instance = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx/");

		testController=(RecController)instance.getController();

	}
	@Test
	public void testStart() throws RemoteException {

		testController.start();
	
	}

	@Test
	public void testShutdown() throws RemoteException {
		testController.shutdown();
	}

	@Test
	public void testRestart() throws RemoteException {
		testController.restart();
	}

	@Test
	public void testReload() throws RemoteException {
		testController.reload();
	}

	@Test
	public void testDeploy() throws IOException, RemoteException {
		File gzFile=new File("d:/Server.zip");
		testController.deploy(gzFile, "/usr/local/");
	}

	
	public void tearDown() throws Exception {
		super.tearDown();
    }

}
