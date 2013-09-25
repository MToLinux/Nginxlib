/**
 * 
 */
package org.cs2c.nginlib.monitor;

import org.cs2c.nginlib.RemoteException;

/**
 * @author Mikes
 * <P/>This interface get the middleware status info already fetch.
 */
public interface MiddlewareStatus {
	// TODO
	//find some useful mid-ware status info, and get it!
	public RecNginxStatus getNginxStatusValue(RecMonitor monitor) throws RemoteException;

	public RecNginxStatus getNginxStatus(); 
}
