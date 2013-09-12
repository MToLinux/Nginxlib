/**
 * 
 */
package org.cs2c.nginlib.config;

/**
 * @author Mikes
 * <P/>Element represents a part of nginx's configuration file. There are two kinds of Element: Block and Directive.
 * <P/>For the following configure fregement:
 * <br/>upstream real_server {
 * <br/>		server 10.1.60.8:8080;
 * <br/>}
 * <P/>The upsream is a Block, representing a range of directive. The server is a directive with end of ";".
 * @see Block
 * @see Directive
 */
public interface Element extends Cloneable {
	/**
	 * Set the Element's name.
	 * @param name Element's name.
	 * */
	void setName(String name);
	/**
	 * Get the Element's name.
	 * @return Element's name.
	 * */
	String getName();
	/**
	 * @return Element's total string which will be in nginx's configuration file. 
	 * */
	String toString();
	/**
	 * Override Cloneable.clone(). The method need to be implemented using a deep copy.
	 * @return the cloned instance with a deep copy.
	 * */
	Element clone();
}
