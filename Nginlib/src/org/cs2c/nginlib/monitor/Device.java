/**
 * 
 */
package org.cs2c.nginlib.monitor;

/**
 * @author Mikes
 * <P/>This interface is used to get the remote real-time info of one device which has been fetch from remote host.
 * The instance implementing the interface is related with specific IO status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface Device {
	/**
	 * Get the device name.
	 * @return The device name.
	 * */
	String getName();
	/**
	 * Get current the device IO input rate (block_ps) which has already fetched from remote host.
	 * @return The current device IO input rate (block_ps).
	 * */
	float getBlockWritenPerSec();
	/**
	 * Get current the device IO output rate (block_ps) which has already fetched from remote host.
	 * @return The current the device IO output rate (block_ps).
	 * */
	float getBlockReadPerSec();
	/**
	 * Get current the device total IO input (block) which has already fetched from remote host.
	 * @return The device total IO input (block).
	 * */
	long getBlockWriten();
	/**
	 * Get current the device total IO output (block) which has already fetched from remote host.
	 * @return The device total IO output (block).
	 * */
	long getBlockRead();
	/**
	 * Get current the device IO transaction rate (tps) which has already fetched from remote host.
	 * @return The current device IO transaction rate (tps).
	 * */
	float getTPS();
}
