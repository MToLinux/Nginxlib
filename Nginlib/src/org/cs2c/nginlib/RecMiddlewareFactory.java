package org.cs2c.nginlib;

import org.cs2c.nginlib.config.Configurator;
import org.cs2c.nginlib.ctl.RecController;
import org.cs2c.nginlib.monitor.Monitor;
import org.cs2c.nginlib.ctl.Controller;

/**
 * @author LiuQin
 * The implement class of AuthInfo 
 * @see MiddlewareFactory
 */
public class RecMiddlewareFactory extends MiddlewareFactory{

	public RecAuthInfo authInfo;
	public RecController controller;
	String  midwarePath;
	
	/** Construct a RecMiddlewareFactory with specified properties */
	public  RecMiddlewareFactory(AuthInfo authInfo, String middlewareHome)
	{
		this.authInfo=(RecAuthInfo)authInfo;
		this.midwarePath=middlewareHome;	
	}
	
	@Override
	public Controller getController() {
		// TODO Auto-generated method stub
		this.controller=new RecController(authInfo,midwarePath);
		return this.controller;
	}

	@Override
	public Configurator getConfigurator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Monitor getMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

}
