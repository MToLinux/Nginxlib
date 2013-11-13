/**
 * 
 */
package org.cs2c.nginlib.monitor;

import java.util.List;

/**
 * @author yanbin.jia
 * <P/>This interface will get the remote real-time info of nginx which has been fetch from remote host.
 * The instance implementing the interface is related with nginx status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface NginxStatus {

	/**
	 * Get current nginx status path which has already fetched from remote host.
	 * @return The current nginx status path.
	 * */
	public String getNginxStatusPath();
	
	/**
	 * Get current nginx user name which has already fetched from remote host.
	 * @return The current nginx status path.
	 * */
	public String getNginxUsername();
	
	/**
	 * Get current nginx password which has already fetched from remote host.
	 * @return The current nginx password.
	 * */
	public String getNginxPasswd();
	
	/**
	 * Get current nginx path which has already fetched from remote host.
	 * @return The current nginx path.
	 * */
	public String getNginxPath();

	
	/* Status Informations */
	/**
	 * Get current nginx status module flag which indicates that the status module has been installed.
	 * @return The current nginx status module flag. 
	 * */
	public boolean getStatusModuleFlag();
	
	/**
	 * Get current number of all open connections which has already fetched from remote host.
	 * @return The current number of all open connections.
	 * */
	public int getActiveConnections();
	
	/**
	 * Get current number of connections nginx accepted which has already fetched from remote host.
	 * @return The current number of connections nginx accepted.
	 * */
	public int getServerAccepts();
	
	/**
	 * Get current number of connections nginx handled which has already fetched from remote host.
	 * @return The current number of connections nginx handled.
	 * */
	public int getServerHandled();
	
	/**
	 * Get current number of requests nginx handles which has already fetched from remote host.
	 * @return The current number of requests nginx handles.
	 * */
	public int getServerRequests();
	
	/**
	 * Get current number of request headers nginx reads from clients which has already fetched from remote host.
	 * @return The current number of requests nginx handles.
	 * */
	public int getNginxReading();
	
	/**
	* Get current number of responses nginx writes to clients which has already fetched from remote host.
	* @return The current number of responses nginx writes to clients.
	* */
	public int getNginxWriting();
	
	/**
	 * Get current keep-alive connections which has already fetched from remote host, actually it is active - (reading + writing).
	 * @return The current keep-alive connections.
	 * */
	public int getKeepAliveConnections();
	
	/**
	 * Get current nginx processes' status which has already fetched from remote host.
	 * @return The current nginx processes' status.
	 * */
	public List<RecProcessStatus> getNginxPSList();

	/* configure arguments */
	/**
	 * Get current nginx configure arguments which has already fetched from remote host.
	 * @return The current nginx configure arguments.
	 * */
	public List<String> getNginxCAList();
}
