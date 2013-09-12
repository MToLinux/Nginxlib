/**
 * 
 */
package org.cs2c.nginlib.config;

/**
 * @author Mikes
 * <P/>The StringParameter is just a common string as a directive parameter.
 * For instance, the following directives' parameters are StringParameter:
 * <br/>use epoll;
 * <br/>worker_connection 1024;
 * @see Parameter
 */
public interface StringParameter extends Parameter{
	/**
	 * Set the StringParamter value.
	 * @param value the parameter's value string.
	 * */
	void setValue(String value);
	/**
	 * Get the StringParameter's value. The same as toString method.
	 * @return the parameter's value string.
	 * */
	String getValue();
}
