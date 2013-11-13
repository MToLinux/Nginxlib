/**
 * 
 */
package org.cs2c.nginlib.monitor;

/**
 * @author Mikes
 * <P/>This interface is used to get the remote real-time info of memory which has been fetch from remote host.
 * The instance implementing the interface is related with memory status of a specific time.
 * The operate is off-line, and the instance state-less.
 */
public interface MemoryStatus {
	/**
	 * Get current total swap (Kb) which has already fetched from remote host.
	 * @return The current total swap (Kb).
	 * */
	int getTotalSwap();
	/**
	 * Get current used swap (Kb) which has already fetched from remote host.
	 * @return The current used swap (Kb).
	 * */
	int getUsedSwap();
	/**
	 * Get current data swapped out of memory, which has already fetched from remote host.
	 * @return The current data swapped out of memory (Kb).
	 * */
	int getSwapIn();
	/**
	 * Get current data swapped into memory, which has already fetched from remote host.
	 * @return The current data swapped into memory (Kb).
	 * */
	int getSwapOut();
	/**
	 * Get current free memory (Kb, and not include buffers and cached) which has already fetched from remote host.
	 * @return The current free memory (Kb).
	 * */
	int getFree();
	/**
	 * Get current buffer memory (Kb) which has already fetched from remote host.
	 * @return The current buffer memory (Kb).
	 * */
	int getBuffers();
	/**
	 * Get current shared memory (Kb) which has already fetched from remote host.
	 * @return The current shared memory (Kb).
	 * */
	int getShared();
	/**
	 * Get current cached memory (Kb) which has already fetched from remote host.
	 * @return The current cached memory (Kb).
	 * */
	int getCached();
	/**
	 * Get current used memory (Kb) which has already fetched from remote host.
	 * @return The current used memory (Kb).
	 * */
	int getUsed();
}
