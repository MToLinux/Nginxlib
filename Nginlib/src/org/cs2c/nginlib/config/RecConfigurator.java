package org.cs2c.nginlib.config;

import java.util.List;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;

public class RecConfigurator implements Configurator {

	private RecAuthInfo creauthInfo;
	private String remoteTargetDirectory;
	private String confPathWithName = null;
	
	public RecConfigurator()
	{
	}
	
	public RecConfigurator(AuthInfo reauthInfo,String midwarePath)
	{
		this.creauthInfo=(RecAuthInfo) reauthInfo;
		this.remoteTargetDirectory=midwarePath;
	}
	
	//set local Conf File With full Name.
	/**
	 * Set the name of the local Conf File.
	 * @param PathWithName : Path With full nginx.conf Name.
	 * */
	public void SetConfpathWithName(String PathWithName){
		confPathWithName = PathWithName;
	}
	
	@Override
	public void append(Element element, String outerBlockNames)
			throws RemoteException {
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(confPathWithName);
		rro.append(element, outerBlockNames);
	}

	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(confPathWithName);
		rro.delete(element, outerBlockNames);
	}

	@Override
	public void insertAfter(Element element, Element after,
			String outerBlockNames) throws RemoteException {
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(confPathWithName);
		rro.insertAfter(element, after, outerBlockNames);
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(confPathWithName);
		rro.replace(oldElement, newElement, outerBlockNames);
	}

	@Override
	public List<Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
		RecRemoteOperator rro = new RecRemoteOperator();
		rro.SetConfpathWithName(confPathWithName);
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
}
