/**
 * 
 */
package org.cs2c.nginlib.log;
import java.util.*;

import org.cs2c.nginlib.RemoteException;
/**
 * @author Mikes
 *
 */
public interface Logger {
	List<LogProfile> getLogFileNames() throws RemoteException;
	String getLogContent(String filename) throws RemoteException;
	void delete(String filename) throws RemoteException;
	void truncate(String filename) throws RemoteException;
}
