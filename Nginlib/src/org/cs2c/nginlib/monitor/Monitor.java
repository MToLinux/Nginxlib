/**
 * 
 */
package org.cs2c.nginlib.monitor;
import org.cs2c.nginlib.*;
/**
 * @author Mikes
 * <P/>This interface will get the various host status by remote operating.
 * The status type includes CPU, IO, network, mid-ware and memory.
 * @see CPUStatus
 * @see IOStatus
 * @see NetworkStatus
 * @see MemoryStatus
 * @see MiddlewareStatus(NginxStatus)
 */
public interface Monitor {
	
	String hostname = "127.0.0.1";
	String username = "root";
	String password = "qwer1234";
	String nginxpath = "/usr/local/nginx/";
	
	/**
	 * Remotely fetch the CPU status info, and return a CPUStatus handler by which to get the info already fetched.
	 * The command can be "vmstat" on the remote host.
	 * @return A CPUStatus handler by which to get the info already fetched.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	CPUStatus getCPUStatus() throws RemoteException;
	/**
	 * Remotely fetch the IO status info, and return a IOStatus handler by which to get the info already fetched.
	 * The command can be "vmstat" and "iostat" on the remote host.
	 * @return A IOStatus handler by which to get the info already fetched.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	IOStatus getIOStatus() throws RemoteException;
	/**
	 * Remotely fetch the network status info, and return a NetworkStatus handler by which to get the info already fetched.
	 * The command can be "ifstat" on the remote host but ifstat tool need be installed individually.
	 * @return A NetworkStatus handler by which to get the info already fetched.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	NetworkStatus getNetworkStatus() throws RemoteException;
	/**
	 * Remotely fetch the memory status info, and return a MemoryStatus handler by which to get the info already fetched.
	 * The command can be "vmstat" and "free" on the remote host but ifstat tool need be installed individually.
	 * @return A MemoryStatus handler by which to get the info already fetched.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	MemoryStatus getMemoryStatus() throws RemoteException;
	
	/**
	 * Remotely fetch the mid-ware(nginx) status info, and return a MiddlewareStatus(NginxStatus) handler by which to get the info already fetched.
	 * @return A MiddlewareStatus(NginxStatus) handler by which to get the info already fetched.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	NginxStatus getNginxStatus(boolean nginx_statusflag, String nginx_statuspath, String nginx_username, String nginx_password) throws RemoteException;
}
