package org.cs2c.nginlib.monitor;

public class RecNginxStatus {
	int ActiveConnections;
	int ServerAccepts;
	int ServerHandled;
	int ServerRequests;
	int NginxReading;
	int NginxWriting;
	int KeepAliveConnections;
	
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

}
