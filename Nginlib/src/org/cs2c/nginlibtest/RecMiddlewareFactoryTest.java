package org.cs2c.nginlibtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.management.monitor.Monitor;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RecMiddlewareFactory;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.Configurator;
import org.cs2c.nginlib.config.Module;
import org.cs2c.nginlib.config.RecConfigurator;
import org.cs2c.nginlib.ctl.Controller;
import org.cs2c.nginlib.ctl.RecController;
import org.cs2c.nginlib.log.RecLogger;
import org.cs2c.nginlib.monitor.RecMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;


public class RecMiddlewareFactoryTest extends TestCase {
	
	RecAuthInfo authInfo=new RecAuthInfo();
	MiddlewareFactory instance = null;	
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
		
		instance = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx");
		
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetController() throws IOException {
		Controller rcontroller=null;
		rcontroller=instance.getController();
		Assert.assertNotNull(rcontroller);
	}

	@Test
	public void testGetConfigurator() {
		Configurator rconfigurator=null;
		rconfigurator=instance.getConfigurator();
		Assert.assertNotNull(rconfigurator);
	}

	@Test
	public void testGetMonitor() {
		RecMonitor rmonitor=null;
		rmonitor=(RecMonitor)instance.getMonitor();
		Assert.assertNotNull(rmonitor);
	}
	@Test
	public void testGetLogger() {
		RecLogger rlogger=null;
		rlogger=(RecLogger)instance.getLogger();
		Assert.assertNotNull(rlogger);
	}


	
	@Test
	public void testGetInstance() throws RemoteException, IOException  {
		Assert.assertNotNull(instance.getInstance(authInfo, "/usr/local/nginx"));

	}

	@Test
	public void testInstall() throws IOException, RemoteException {
		File gzFile=new File("d:/nginx-1.0.15.tar.gz");
		
		if(gzFile.exists()==false)
			try {
				throw new IOException();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		String targetPath="/root/nginx/";
		
		Assert.assertNotNull(instance.install(authInfo, gzFile, targetPath, null));
	}

	@Test
	public void testNewAuthInfo() {
		Assert.assertNotNull(instance.newAuthInfo());
	}

}
