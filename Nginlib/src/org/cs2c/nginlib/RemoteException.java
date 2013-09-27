/**
 * 
 */
package org.cs2c.nginlib;

/**
 * @author Mikes
 * <P/>RemoteException should be threw when remotely operation fails.
 */
public class RemoteException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public RemoteException() {
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param arg0
	 */
	public RemoteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param arg0
	 */
	public RemoteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RemoteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub


	}

}
