/**
 * 
 */
package org.cs2c.nginlib.config;
import java.util.*;
/**
 * @author Mikes
 * <P/>A module is a collection of directives implementing a specific function.
 * The modules nginx having is determined by compiling. If you want to add more module, you have to recompile nginx.
 */
public interface Module {
	/**
	 * Set the module name
	 * @param name the module name
	 * */
	void setName(String name);
	/**
	 * Get the module name
	 * @return the module name
	 * */
	String getName();
	/**
	 * Get all directives the module having.
	 * @return a list of directives
	 * */
	List<Directive> getDirectives();
}
