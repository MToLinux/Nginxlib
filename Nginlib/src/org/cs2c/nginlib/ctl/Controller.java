/**
 * 
 */
package org.cs2c.nginlib.ctl;
import java.io.*;

import org.cs2c.nginlib.*;
/**
 * @author Mikes
 * <P/>This interface is used to handle Nginx's running life cycle.
 */
public interface Controller {
	/**
	 * Start the remote Nginx.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void start() throws RemoteException;
	/**
	 * Shutdown the remote Nginx.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void shutdown() throws RemoteException;
	/**
	 * Restart the remote Nginx
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void restart() throws RemoteException;
	/**
	 * Reload the Nginx configure file to make it take effect.
	 * Reloading will not interrupt the Nginx's running if it is running.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void reload() throws RemoteException;
	/**
	 * Deploy web site into remote nginx.
	 * The local web site file must be in format of zip file.
	 * If remote path has the same file, the former file will be rename with subfix of "_bak".
	 * When finishing deploying, zip file should be removed.
	 * @param zipFile Web file in the format of zip in local.
	 * @param targetPath Remote host home path where to deploy.
	 * @throws IOException When reading local web site zip file fails.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void deploy(File zipFile, String targetPath) throws IOException, RemoteException;
}
