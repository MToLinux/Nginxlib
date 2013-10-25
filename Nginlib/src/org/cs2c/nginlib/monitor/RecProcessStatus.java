package org.cs2c.nginlib.monitor;

public class RecProcessStatus implements ProcessStatus {
	
	private String p_USER = null;
	private int p_PID = -1;
	private float p_CPU = 0;
	private float p_MEM = 0;
	private int p_VSZ = 0;
	private int p_RSS = 0;
	private String p_TTY = null;
	private String p_STAT = null;
	private String p_START = null;
	private String p_TIME = null;
	private String p_COMMAND = null;
	
	public void setProcessUser(String processUser) {
		this.p_USER = processUser;
	}
	public String getProcessUser() {
		return p_USER;
	}
	
	public void setProcessID(int processID) {
		this.p_PID = processID;
	}
	public int getProcessID() {
		return p_PID;
	}
	
	public void setProcessCPU(float processCPU) {
		this.p_CPU = processCPU;
	}
	public float getProcessCPU() {
		return p_CPU;
	}
	
	public void setProcessMem(float processMem) {
		this.p_MEM = processMem;
	}
	public float getProcessMem() {
		return p_MEM;
	}
	
	public void setProcessVSZ(int processVSZ) {
		this.p_VSZ = processVSZ;
	}
	public float getProcessVSZ() {
		return p_VSZ;
	}
	
	public void setProcessRSS(int processRSS) {
		this.p_RSS = processRSS;
	}
	public float getProcessRSS() {
		return p_RSS;
	}
	
	public void setProcessTTY(String processTTY) {
		this.p_TTY = processTTY;
	}
	public String getProcessTTY() {
		return p_TTY;
	}
	
	public void setProcessSTAT(String processSTAT) {
		this.p_STAT = processSTAT;
	}
	public String getProcessSTAT() {
		return p_STAT;
	}
	
	public void setProcessSTART(String processSTART) {
		this.p_START = processSTART;
	}
	public String getProcessSTART() {
		return p_START;
	}
	
	public void setProcessTIME(String processTIME) {
		this.p_TIME = processTIME;
	}
	public String getProcessTIME() {
		return p_TIME;
	}
	
	public void setProcessCmd(String processCmd) {
		this.p_COMMAND = processCmd;
	}
	public String getProcessCmd() {
		return p_COMMAND;
	}
}
