package org.cs2c.nginlib.monitor;

import java.util.List;

public interface NginxStatus {

	public String getNginxStatusPath();
	
	public String getNginxUsername();
	
	public String getNginxPasswd();
	
	public String getNginxPath();

	
	/* Status Informations */
	
	public int getActiveConnections();
	
	public int getServerAccepts();
	
	public int getServerHandled();
	
	public int getServerRequests();
	
	public int getNginxReading();
	
	public int getNginxWriting();
	
	public int getKeepAliveConnections();
	
	public List<RecProcessStatus> getNginxPSList();

	/* configure arguments */
	public List<String> getNginxCAList();
}
