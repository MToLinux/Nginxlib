package org.cs2c.nginlib.monitor;

public class RecCPUStatus implements CPUStatus {
	
	private int RunningNum = 0;
	private int BlockingNum = 0;
	private int InterruptNum = 0;
	private int ContextSwitchNum = 0;
	private float UserPercent = 0;
	private float SystemPercent = 0;
	private float IdlePercent = 0;
	private float IOWaitPercent = 0;

	public void setRunningNum(int RunningNum) {
		this.RunningNum = RunningNum;
	}
	@Override
	public int getRunningNum() {
		return RunningNum;
	}

	public void setBlockingNum(int BlockingNum) {
		this.BlockingNum = BlockingNum;
	}
	@Override
	public int getBlockingNum() {
		return BlockingNum;
	}

	public void setInterruptNum(int InterruptNum) {
		this.InterruptNum = InterruptNum;
	}
	@Override
	public int getInterruptNum() {
		return InterruptNum;
	}

	public void setContextSwitchNum(int ContextSwitchNum) {
		this.ContextSwitchNum = ContextSwitchNum;
	}
	@Override
	public int getContextSwitchNum() {
		return ContextSwitchNum;
	}

	public void setUserPercent(float UserPercent) {
		this.UserPercent = UserPercent;
	}
	@Override
	public float getUserPercent() {
		return UserPercent;
	}

	public void setSystemPercent(float SystemPercent) {
		this.SystemPercent = SystemPercent;
	}
	@Override
	public float getSystemPercent() {
		return SystemPercent;
	}

	public void setIdlePercent(float IdlePercent) {
		this.IdlePercent = IdlePercent;
	}
	@Override
	public float getIdlePercent() {
		return IdlePercent;
	}

	public void setIOWaitPercent(float IOWaitPercent) {
		this.IOWaitPercent = IOWaitPercent;
	}
	@Override
	public float getIOWaitPercent() {
		return IOWaitPercent;
	}
}
