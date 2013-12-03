/**
 * 
 */
package org.cs2c.nginlib.config;

import org.cs2c.nginlib.RemoteException;

/**
 * @author Mikes
 * <P/>The Configurator interface contains all methods to operate remote nginx configuration file.
 * This interface have the functions of create empty elements and RemoteOperate.
 * @see RemoteOperator
 */
public interface Configurator extends RemoteOperator{
	/**
	 * Create an empty block object.
	 * @return An empty block object.
	 * @throws RemoteException 
	 * */
	Block newBlock() throws RemoteException;
	/**
	 * Create an empty directive object.
	 * @return An empty directive object.
	 * */
	Directive newDirective();
	/**
	 * Create an empty variable object.
	 * @return An empty variable object.
	 * */
	Variable newVariable();
	/**
	 * Create an empty string parameter object.
	 * @return An empty string parameter object.
	 * */
	StringParameter newStringParameter();
	/**
	 * Create an empty option object.
	 * @return An empty option object.
	 * */
	Option newOption();
}
