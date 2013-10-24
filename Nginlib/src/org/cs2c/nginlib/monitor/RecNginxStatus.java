package org.cs2c.nginlib.monitor;

import java.util.List;

public class RecNginxStatus implements NginxStatus{
	
	private String NginxPath = "/usr/local/nginx";
	private String NginxStatusPath = "status";
	private String NginxUsername = "user";
	private String NginxPasswd = "qwer1234";
	
	/* Constructor */
	public RecNginxStatus(String NginxPath, String NginxStatusPath, String NginxUsername, String NginxPasswd)
	{
		if(NginxPath!=null && NginxPath.length()!=0)
		{
			this.NginxPath = NginxPath;
		}
		else
		{
			this.NginxPath = "/usr/local/nginx";
		}
		if(NginxStatusPath!=null && NginxStatusPath.length()!=0)
		{
			this.NginxStatusPath = NginxStatusPath;
		}
		else
		{
			this.NginxStatusPath = "status";
		}
		if(NginxUsername!=null && NginxUsername.length()!=0)
		{
			this.NginxUsername = NginxUsername;
		}
		else
		{
			this.NginxUsername = "user";
		}
		if(NginxPasswd!=null && NginxPasswd.length()!=0)
		{
			this.NginxPasswd = NginxPasswd;
		}
		else
		{
			this.NginxPasswd = "qwer1234";
		}
	}
	
	
	public void setNginxStatusPath(String NginxStatusPath) {
		this.NginxStatusPath = NginxStatusPath;
	}
	public String getNginxStatusPath() {
		return NginxStatusPath;
	}
	
	public void setNginxUsername(String NginxUsername) {
		this.NginxUsername = NginxUsername;
	}
	public String getNginxUsername() {
		return NginxUsername;
	}
	
	public void setNginxPasswd(String NginxPasswd) {
		this.NginxPasswd = NginxPasswd;
	}
	public String getNginxPasswd() {
		return NginxPasswd;
	}
	
	public void setNginxPath(String NginxPath) {
		this.NginxPath = NginxPath;
	}
	public String getNginxPath() {
		return NginxPath;
	}

	
	/* Status Information */
	private int ActiveConnections = 0;
	private int ServerAccepts = 0;
	private int ServerHandled = 0;
	private int ServerRequests = 0;
	private int NginxReading = 0;
	private int NginxWriting = 0;
	private int KeepAliveConnections = 0;
	private List<RecProcessStatus> listNginxProcessStatus = null;
		
	public void setActiveConnections(int ActiveConnections) {
		this.ActiveConnections = ActiveConnections;
	}
	public int getActiveConnections() {
		return ActiveConnections;
	}
	
	public void setServerAccepts(int ServerAccepts) {
		this.ServerAccepts = ServerAccepts;
	}
	public int getServerAccepts() {
		return ServerAccepts;
	}
	
	public void setServerHandled(int ServerHandled) {
		this.ServerHandled = ServerHandled;
	}
	public int getServerHandled() {
		return ServerHandled;
	}
	
	public void setServerRequests(int ServerRequests) {
		this.ServerRequests = ServerRequests;
	}
	public int getServerRequests() {
		return ServerRequests;
	}
	
	public void setNginxReading(int NginxReading) {
		this.NginxReading = NginxReading;
	}
	public int getNginxReading() {
		return NginxReading;
	}
	
	public void setNginxWriting(int NginxWriting) {
		this.NginxWriting = NginxWriting;
	}
	public int getNginxWriting() {
		return NginxWriting;
	}
	
	public void setKeepAliveConnections(int KeepAliveConnections) {
		this.KeepAliveConnections = KeepAliveConnections;
	}
	public int getKeepAliveConnections() {
		return KeepAliveConnections;
	}
	
	public void setNginxPSList(List<RecProcessStatus> listNginxProcessStatus) {
		this.listNginxProcessStatus = listNginxProcessStatus;
	}
	public List<RecProcessStatus> getNginxPSList() {
		return listNginxProcessStatus;
	}

	/* configure arguments */
	private List<String> listNginxConfigArgs = null;
	
	public void setNginxCAList(List<String> listNginxConfigArgs) {
		this.listNginxConfigArgs = listNginxConfigArgs;
	}
	public List<String> getNginxCAList() {
		return listNginxConfigArgs;
	}
}
