/**
 * 
 */
package org.cs2c.nginlib.monitor;
import java.util.*;
/**
 * @author Mikes
 * <P/>This interface is used to get the remote real-time info of IO which has been fetch from remote host.
 * The instance implementing the interface is related with general IO status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface IOStatus {
	/**
	 * Get current general IO input rate (block_ps) which has already fetched from remote host.
	 * @return The current general IO input rate (block_ps).
	 * */
	float getBlockInPerSec();
	/**
	 * Get current general IO output rate (block_ps) which has already fetched from remote host.
	 * @return The current general IO output rate (block_ps).
	 * */
	float getBlockOutPerSec();
	/**
	 * Get each device IO status info, which has already fetched from remote host.
	 * @return A list of device status info access interface.
	 * */
	List<Device> getDevices();
}
