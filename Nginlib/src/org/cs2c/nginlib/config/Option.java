/**
 * 
 */
package org.cs2c.nginlib.config;

/**
 * @author Mikes
 * <P/>The Option is a style of parameter. It contains name and value, which is separated by "=".
 * The option style example as follow:
 * <P/>server 10.1.60.8:8080 weight=10;
 * <P/>As above, "weight=10" is an option parameter while "10.1.60.8:8080" is a string parameter.
 * "weight" is the name of the option parameter, and "10" is the value of the option parameter.
 */
public interface Option extends Parameter{
	/**
	 * Set the name of the option parameter
	 * @param name the part before "=" of the option parameter
	 * */
	void setName(String name);
	/**
	 * Get the name of the option parameter.
	 * @return the part before "=" of the option parameter
	 * */
	String getName();
	/**
	 * Set the value of the option parameter.
	 * @param value the part after "=" of the option parameter
	 * */
	void setValue();
	/**
	 * Get the value of the option parameter.
	 * @return the part after "=" of the option parameter
	 * */
	String getValue();
}
