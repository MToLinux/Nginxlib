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
	
	public RecConfigurator(AuthInfo reauthInfo,String midwarePath)
	{
		this.creauthInfo=(RecAuthInfo) reauthInfo;
		this.remoteTargetDirectory=midwarePath;
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
		return null;
	}

	@Override
	public Block newBlock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Directive newDirective() {
		// TODO Auto-generated method stub
		return null;
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
	
	/**
	 * Get the nginx.conf file which nginx.conf fullpath is parameter remoteFile.
	 * */
	public void ReadRemoteConf(String remoteFile) throws IOException
	{
		String localTargetDirectory = "D:\\eclipseWorkspace\\confpath";

		Connection conn = new Connection(this.creauthInfo.getHostname());
		/* Now connect */
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.creauthInfo.getUsername(), this.creauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		
		SCPClient scpc=conn.createSCPClient();
		
		scpc.get(remoteFile, localTargetDirectory);
		/* Close the connection */
		conn.close();
	}
	
	/**
	 * Write the Remote nginx.conf file which is select.
	 * */
	public void WriteRemoteConf() throws IOException{
		WriteRemoteConf(remoteTargetDirectory);
	}
	
	public void WriteRemoteConf(String targetPath) throws IOException{
		Connection conn = new Connection(this.creauthInfo.getHostname());
		/* Now connect */
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.creauthInfo.getUsername(), this.creauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		
		SCPClient scpc=conn.createSCPClient();
		Session sess = conn.openSession();
		
		System.out.println("Here is some information about the remote host:");
		
		String localFile = "E:\\工作目录\\项目资料\\中间件监控管理平台\\nginx\\conf\\nginx.conf";
		scpc.put(localFile, targetPath);
		
		/* Close this session */
		sess.close();
		/* Close the connection */
		conn.close();
		}
}
