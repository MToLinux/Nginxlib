/**
 * 
 */
package org.cs2c.nginlib.config;
import java.util.*;

import org.cs2c.nginlib.RemoteException;
/**
 * @author Mikes
 * <P/>Block represents a range of directive with "{}".
 * Block can contain sub blocks and directives.
 * The sequence of calling addBlock and addDirective is sensitive.
 * <P/>For instance, if you call addBlock and then call addDirective, Finally toString should return:
 * <br/>[block name] ... {
 * <br/>...
 * <br/>}
 * <br/>[directive name] ...;
 * <P/>But not return:
 * <br/>[directive name] ...;
 * <br/>[block name] ... {
 * <br/>...
 * <br/>}
 * @see Element
 */
public interface Block extends Element{
	/**
	 * Get sub block list with in the instance.
	 * @return sub blocks with list container. If the block have no sub block, an empty list should be returned.
	 * @throws RemoteException 
	 * */
	List<Block> getBlocks() throws RemoteException;
	/**
	 * Get all directives directly in the block.
	 * @return directives with list container. If the block have no directive, an empty list should be returned.
	 * @throws RemoteException 
	 * */
	List<Directive> getDirectives() throws RemoteException;
	/**
	 * Add a block in the block end.
	 * @param block block to be added.
	 * */
	void addBlock(Block block);
	/**
	 * add a directive in the block end.
	 * @param directive directive to be added.
	 * */
	void addDirective(Directive directive);
}
