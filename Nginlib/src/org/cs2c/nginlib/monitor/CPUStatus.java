/**
 * 
 */
package org.cs2c.nginlib.monitor;

/**
 * @author Mikes
 * <P/>This interface is used to get the remote real-time info of CPU.
 * The instance implementing the interface is related with cpu status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface CPUStatus {
	/**
	 * Get current running task number which has already fetched from remote host.
	 * @return The current running task number
	 * */
	int getRunningNum();
	/**
	 * Get current blocked task number which has already fetched from remote host.
	 * @return The current blocked task number
	 * */
	int getBlockingNum();
	/**
	 * Get current CPU interrupt count per second, which has already fetched from remote host.
	 * @return The current CPU interrupt count per second.
	 * */
	int getInterruptNum();
	/**
	 * Get current CPU context switch count per second, which has already fetched from remote host.
	 * @return The current CPU context switch count per second.
	 * */
	int getContextSwitchNum();
	/**
	 * Get current CPU used percent of user, which has already fetched from remote host.
	 * @return The current CPU used percent of user.
	 * */
	float getUserPercent();
	/**
	 * Get current CPU used percent of system, which has already fetched from remote host.
	 * @return The current CPU used percent of system.
	 * */
	float getSystemPercent();
	/**
	 * Get current CPU idle percent, which has already fetched from remote host.
	 * @return The current CPU idle percent.
	 * */
	float getIdlePercent();
	/**
	 * Get current CPU time percent for IO waiting, which has already fetched from remote host.
	 * @return The current CPU time percent for IO waiting.
	 * */
	float getIOWaitPercent();
}
