package org.cs2c.nginlib;

import org.cs2c.nginlib.config.Configurator;
import org.cs2c.nginlib.config.RecConfigurator;
import org.cs2c.nginlib.ctl.RecController;
import org.cs2c.nginlib.monitor.Monitor;
import org.cs2c.nginlib.monitor.RecMonitor;
import org.cs2c.nginlib.ctl.Controller;

/**
 * @author LiuQin
 * The implement class of AuthInfo 
 * @see MiddlewareFactory
 */
public class RecMiddlewareFactory extends MiddlewareFactory{

	public RecAuthInfo authInfo;
	public RecController controller;
	public RecMonitor monitor;
	public RecConfigurator configurator;
	String  midwarePath;
	
	/** Construct a RecMiddlewareFactory with specified properties */
	public  RecMiddlewareFactory(AuthInfo authInfo, String middlewareHome)
	{
		this.authInfo=(RecAuthInfo)authInfo;
		this.midwarePath=middlewareHome;	
	}
	
	@Override
	public Controller getController() {
		this.controller=new RecController(authInfo,midwarePath);
		return this.controller;
	}

	@Override
	public Configurator getConfigurator() {
		// TODO Auto-generated method stub
		configurator=new RecConfigurator(authInfo,midwarePath);
		return configurator;
	}

	@Override
	public Monitor getMonitor() {
		// TODO Auto-generated method stub
		//monitor=new RecMonitor(authInfo);
		//return this.monitor;
		return null;
	}

}
