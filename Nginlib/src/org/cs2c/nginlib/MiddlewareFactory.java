/**
 * 
 */
package org.cs2c.nginlib;
import org.cs2c.nginlib.config.*;
import org.cs2c.nginlib.ctl.*;
import org.cs2c.nginlib.monitor.*;
import java.util.*;
import java.io.*;
/**
 * @author Mikes
 * <P/>The MiddlewareFactory is the entry of the library. Each instance of this class is associated with 
 * a specific remote middleware (not remote host). Through the instance of the class, we can further 
 * get to configure, control and monitor the remote middleware.
 */
public abstract class MiddlewareFactory {
	/**
	 * Get an instance of the class based on remote middleware home path.
	 * @param authInfo Remote host access information
	 * @return the instance binded with the specific remote middleware.
	 * @throws RemoteException When authentication fails, network error or nginx does not exist.
	 * */
	static public MiddlewareFactory getInstance(AuthInfo authInfo, String middlewareHome) throws RemoteException{
		// TODO
		return null;
	}
	/**
	 * If remote middleware is not there, we can call this method to install it remotely.
	 * Before installing, a local setup file should be prepared.
	 * @param authInfo Authentication info in order to access remote host.
	 * @param gzFile middleware setup file in local. Format must be tar.gz.
	 * @param targetPath The path in remote host where the middleware will be installed.
	 * @param modules Which modules should be included when middleware (such as Nginx) is installed.
	 * @return the instance of the class, which is binded with the middleware installed.
	 * @throws IOException When reading local gzFile fails.
	 * @throws RemoteException When installing remote nginx fails.
	 * */
	static public MiddlewareFactory install(AuthInfo authInfo, File gzFile, String targetPath, List<Module> modules) 
		throws IOException, RemoteException{
		// TODO
		return null;
	}
	/**
	 * Get the controller interface in order to operate Nginx' running. The Nginx is binded with the instance.
	 * @return The Controller interface of the middleware (Nginx).
	 * */
	abstract public Controller getController();
	/**
	 * Get the configurator interface in order to operate Nginx configure file. The Nginx is binded with the instance.
	 * @return The configurator interface of the middleware (Nginx).
	 * */
	abstract public Configurator getConfigurator();
	/**
	 * Get the monitor interface in order to fetch the remote host and middleware's running status. The Nginx is binded with the instance.
	 * @return The monitor interface of the middleware (Nginx).
	 * */
	abstract public Monitor getMonitor();
	/**
	 * Create an instance of AuthInfo without any info.
	 * Then you can set the instance info through AuthInfo interface.
	 * @return An instance of AuthInfo without any info.
	 * */
	abstract public AuthInfo newAuthInfo();
}
