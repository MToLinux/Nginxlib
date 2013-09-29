package org.cs2c.nginlib.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;

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
		return null;
	}

	@Override
	public Block newBlock() {
		// TODO Auto-generated method stub
		Block opBlock = new RecBlock();
		//opBlock.setName("server");
		
		return opBlock;
	}

	@Override
	public Directive newDirective() {
		// TODO Auto-generated method stub
		Directive opDirective = new RecDirective();
		return opDirective;
	}

	@Override
	public Variable newVariable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringParameter newStringParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Option newOption() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
