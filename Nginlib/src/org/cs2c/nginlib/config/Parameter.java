/**
 * 
 */
package org.cs2c.nginlib.config;

/**
 * @author Mikes
 * <P/>A parameter belongs to some directive.
 * There are three types of parameter: StringParameter, Variable and Option.
 * @see StringParameter
 * @see Variable
 * @see Option
 */
public interface Parameter {
	/**
	 * Get the parameter content which will be indicated in the nginx configuration file.
	 * @return the parameter content string
	 * */
	String toString();
	/**
	 * Set the up space of the parameter
	 * @param up space to be set
	 * */
	void setUpSpace(String UpSpace);
	/**
	 * get the up space of the parameter
	 * */
	String getUpSpace();
}
