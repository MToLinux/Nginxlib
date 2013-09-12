/**
 * 
 */
package org.cs2c.nginlib.monitor;

/**
 * @author Mikes
 * <P/>This interface get the network real-time status info already fetched from remote host.
 * The instance implementing the interface is related with network status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface NetworkStatus {
	/**
	 * Get current network input rate (kbps) which has already fetched from remote host.
	 * @return The current network input rate (kbps).
	 * */
	float getInputKbPerSec();
	/**
	 * Get current network output rate (kbps) which has already fetched from remote host.
	 * @return The current network output rate (kbps).
	 * */
	float getOutputPerSec();
}
