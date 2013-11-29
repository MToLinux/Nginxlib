/**
 * 
 */
package org.cs2c.nginlib.config;
import java.util.*;
/**
 * @author Mikes
 * <P/>A directive in nginx configuration file is a command with end of ";".
 * A directive could have no parameter, or have more than one parameters.
 * @see Element
 */
public interface Directive extends Element{
	/**
	 * Get all parameters the directive having.
	 * @return parameter list. If the directive have no parameter, an empty list should be returned.
	 * */
	List<Parameter> getParameters();
	/**
	 * Add a parameter to the directive's end.
	 * @param parameter parameter to be added.
	 * */
	void addParameter(Parameter parameter);
	void deleteParameter(Parameter parameter);
}
