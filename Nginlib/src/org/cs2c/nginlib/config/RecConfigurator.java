package org.cs2c.nginlib.config;

import java.util.List;
import java.util.Map;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;

import com.trilead.ssh2.Connection;

public class RecConfigurator implements Configurator {

	private RecAuthInfo creauthInfo= null;
	private String cmidwarePath= null;
	private String confPath = null;
	private Connection connConfigurator = null;
	
	RecRemoteOperator rro = null;
	
	public RecConfigurator(AuthInfo reauthInfo,String midwarePath,Connection conn)
	{
		this.creauthInfo=(RecAuthInfo) reauthInfo;
		this.cmidwarePath=midwarePath;
		this.connConfigurator=conn;
		rro = new RecRemoteOperator(creauthInfo,cmidwarePath,connConfigurator);
	}
	
	/**
	 * Set the Path of the local Conf File.
	 * @param Path : local nginx.conf Path.
	 * */
	public void SetLocalConfpath(String Path){
		confPath = Path;
		rro.SetLocalConfpath(confPath);
	}
	
	@Override
	public void append(Element element, String outerBlockNames)
			throws RemoteException {
		rro.append(element, outerBlockNames);
	}

	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
		rro.delete(element, outerBlockNames);
	}

	@Override
	public void insertAfter(Element element, Element after,
			String outerBlockNames) throws RemoteException {

		rro.insertAfter(element, after, outerBlockNames);
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
		rro.replace(oldElement, newElement, outerBlockNames);
	}

	@Override
	public Map<Integer, Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
		return rro.getBlocks(blockName, outerBlockNames);
	}

	@Override
	public Block newBlock() {
		Block opBlock = new RecBlock();
		
		return opBlock;
	}

	@Override
	public Directive newDirective() {
		Directive opDirective = new RecDirective();
		return opDirective;
	}

	@Override
	public Variable newVariable() {
		Variable opVariable = new RecVariable();
		return opVariable;
	}

	@Override
	public StringParameter newStringParameter() {
		StringParameter opStringParameter = new RecStringParameter();
		return opStringParameter;
	}

	@Override
	public Option newOption() {
		Option opOption = new RecOption();
		return opOption;
	}

	@Override
	public Block getRootBlock() throws RemoteException {
		return rro.getRootBlock();
	}
}
