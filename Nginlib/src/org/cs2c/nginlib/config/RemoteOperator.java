/**
 * 
 */
package org.cs2c.nginlib.config;
import java.util.*;
import org.cs2c.nginlib.*;
/**
 * @author Mikes
 * <P/>RemoteOperate interface is responsible for operating nginx configuration file.
 * The operations includes add, delete and update configuration file elements.
 * This interface also supports query for specific elements of configuration file.
 * Each method have to indicate the target block through the parameter of outerBlockNames.
 * <P/>For example, if we want put "redirect" directive appending to the second block "if":
 * <br/>http {
 * <br/>		server {
 * <br/>			if {
 * <br/>				...
 * <br/>			}
 * <br/>			if {
 * <br/>			    ...
 * <br/>			}
 * <br/>		}
 * <br/>}
 * <P/>Then, outerBlockNames can be "http:0|server:0|if:1". "if:1" means the second if block.
 * The index of block is from zero. If there is no index in outerBlockNames, default the first block.
 * @see Configurator 
 */
public interface RemoteOperator {
	/**
	 * Put the element at the end of the target block.
	 * The parameter of outerBlockNames give all blocks out of the target block in order to find the target block.
	 * 
	 * @param element Element to be append.
	 * @param outerBlockNames Indicate the target block position.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void append(Element element, String outerBlockNames) throws RemoteException;
	/**
	 * Delete the specific element in the specific block which the outerBlockNames string indicates.
	 * @param element Element to be deleted.
	 * @param outerBlockNames Indicate the target block position.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void delete(Element element, String outerBlockNames) throws RemoteException;
	/**
	 * Insert the specific element after another element in the specific block which the outerBlockNames string indicates.
	 * @param element Element to be inserted.
	 * @param after	Element after which the new element is inserted.
	 * @param outerBlockNames Indicate the target block position.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void insertAfter(Element element, Element after, String outerBlockNames) throws RemoteException;
	/**
	 * Replace the specific old element with the new element in the specific block which the outerBlockNames string indicates.
	 * @param oldElement The element need to be replaced.
	 * @param newElement The element Which will replace the old one.
	 * @param outerBlockNames Indicate the target block position.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	void replace(Element oldElement, Element newElement, String outerBlockNames) throws RemoteException;
	/**
	 * Query all blocks with the specific block name in the specific block which the outerBlockNames string indicates.
	 * @param blockName The name of block which will be query.
	 * @param outerBlockNames Indicate the target block position.
	 * @return All blocks in a list, which have the block name in the block outerBlockNames specifying.
	 * @throws RemoteException When this remote operation fails for any non-local reason.
	 * */
	List<Block> getBlocks(String blockName, String outerBlockNames) throws RemoteException;
}
