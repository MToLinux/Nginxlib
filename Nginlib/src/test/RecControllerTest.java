package test;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RecAuthInfo;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class RecControllerTest extends TestCase {

	private org.cs2c.nginlib.ctl.RecController testController;
	protected void setUp() throws Exception{
		super.setUp();
		AuthInfo authInfo;
		authInfo=MiddlewareFactory.newAuthInfo();
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
			
		testController=new org.cs2c.nginlib.ctl.RecController((RecAuthInfo)authInfo,"/usr/local/nginx/");

	}
	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

	@Test
	public void testShutdown() {
		fail("Not yet implemented");
	}

	@Test
	public void testRestart() {
		fail("Not yet implemented");
	}

	@Test
	public void testReload() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeploy() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsRunning(){
		//testController.start();
		//Assert.assertEquals(true, testController.isRunning());
		//Assert.assertTrue(testController.isRunning());
		//testController.shutdown();
		Assert.assertEquals(false, testController.isRunning());
		//Assert.assertFalse(testController.isRunning());
	}
	public void tearDown() throws Exception {
		super.tearDown();
    }

}
