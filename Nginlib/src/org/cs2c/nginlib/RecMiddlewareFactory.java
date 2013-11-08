package org.cs2c.nginlib;

import java.io.IOException;

import org.cs2c.nginlib.config.Configurator;
import org.cs2c.nginlib.config.RecConfigurator;
import org.cs2c.nginlib.ctl.RecController;
import org.cs2c.nginlib.monitor.Monitor;
import org.cs2c.nginlib.monitor.RecMonitor;
import org.cs2c.nginlib.ctl.Controller;

import com.trilead.ssh2.Connection;

/**
 * @author LiuQin The implement class of AuthInfo
 * @see MiddlewareFactory
 */
public class RecMiddlewareFactory extends MiddlewareFactory {

	RecAuthInfo authInfo = null;
	RecController controller = null;
	RecMonitor monitor = null;
	RecConfigurator configurator = null;
	Connection conn = null;
	String midwarePath = "";
	

	/**
	 * Construct a RecMiddlewareFactory with specified properties
	 * 
	 * @throws RemoteException 
	 */
	public RecMiddlewareFactory(AuthInfo authInfo, String middlewareHome)
			throws  RemoteException {
		this.authInfo = (RecAuthInfo) authInfo;
		this.midwarePath = pathStrConvert(middlewareHome);
		this.creatConnection();
	}

	@Override
	public Controller getController() {
		this.controller = new RecController(authInfo, midwarePath, conn);
		return this.controller;
	}

	@Override
	public Connection getConnection() throws IOException {
		return conn;
	}

	public void creatConnection() throws RemoteException {
		/* Create a connection instance */
		conn = new Connection(authInfo.getHostname());
		/* Now connect */
		try {
			conn.connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			throw new RemoteException(e1);

		}
		try {
			conn.authenticateWithPassword(
					authInfo.getUsername(), authInfo.getPassword());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RemoteException(e);
		}
	}

	@Override
	public Configurator getConfigurator() {
		// TODO Auto-generated method stub
		configurator = new RecConfigurator(authInfo, midwarePath, conn);
		return configurator;
		// return null;
	}

	@Override
	public Monitor getMonitor() {
		// TODO Auto-generated method stub
		monitor = new RecMonitor(authInfo, midwarePath, conn);
		return monitor;
		// return null;
	}

	public void closeConnection() {
		conn.close();
	}

}
