package org.cs2c.nginlib.config;

import java.util.List;

import org.cs2c.nginlib.RemoteException;

public class RecRemoteOperator implements RemoteOperator{
	
	public String ReadConf(String path){
	    try {
	    	RecBlock oro = new RecBlock();
			return oro.ReadConf(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void append(Element element, String outerBlockNames)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAfter(Element element, Element after,
			String outerBlockNames) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
		// TODO Auto-generated method stub
		Block objblock = new RecBlock();
		objblock.setName(outerBlockNames);
		
		return null;
	}

}
