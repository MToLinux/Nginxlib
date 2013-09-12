/**
 * 
 */
package org.cs2c.nginlib.config;

/**
 * @author Mikes
 * <P/>The variable is a mechanism for nginx get dynamic information when serve each request.
 * Most variables content is dynamically changed based on client, so get the value from this value is not meaningful.
 * Therefore, variable value's setter and getter methods are not included.
 * The variable begins as "$". It can be use directive parameter, example as follows:
 * <br/>rediect $url;
 * <br/>rewirte $url www.google.com;
 * @see Parameter
 */
public interface Variable extends Parameter{
	/**
	 * Set name of the variable instance. It is without "$" prefix.
	 * @param the variable instance name without the prefix of "$".
	 * */
	void setName(String name);
	/**
	 * Get the variable's name
	 * @return the variable's name
	 * */
	String getName();
}
