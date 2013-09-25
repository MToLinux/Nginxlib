package org.cs2c.nginlib.monitor;

public class RecCPUStatus implements CPUStatus {
	
	int RunningNum = 0;
	int BlockingNum = 0;
	int InterruptNum = 0;
	int ContextSwitchNum = 0;
	float UserPercent = 0;
	float SystemPercent = 0;
	float IdlePercent = 0;
	float IOWaitPercent = 0;

	public void setRunningNum(int RunningNum) {
		this.RunningNum = RunningNum;
	}
	@Override
	public int getRunningNum() {
		// TODO Auto-generated method stub
		return RunningNum;
	}

	public void setBlockingNum(int BlockingNum) {
		this.BlockingNum = BlockingNum;
	}
	@Override
	public int getBlockingNum() {
		// TODO Auto-generated method stub
		return BlockingNum;
	}

	public void setInterruptNum(int InterruptNum) {
		this.InterruptNum = InterruptNum;
	}
	@Override
	public int getInterruptNum() {
		// TODO Auto-generated method stub
		return InterruptNum;
	}

	public void setContextSwitchNum(int ContextSwitchNum) {
		this.ContextSwitchNum = ContextSwitchNum;
	}
	@Override
	public int getContextSwitchNum() {
		// TODO Auto-generated method stub
		return ContextSwitchNum;
	}

	public void setUserPercent(float UserPercent) {
		this.UserPercent = UserPercent;
	}
	@Override
	public float getUserPercent() {
		// TODO Auto-generated method stub
		return UserPercent;
	}

	public void setSystemPercent(float SystemPercent) {
		this.SystemPercent = SystemPercent;
	}
	@Override
	public float getSystemPercent() {
		// TODO Auto-generated method stub
		return SystemPercent;
	}

	public void setIdlePercent(float IdlePercent) {
		this.IdlePercent = IdlePercent;
	}
	@Override
	public float getIdlePercent() {
		// TODO Auto-generated method stub
		return IdlePercent;
	}

	public void setIOWaitPercent(float IOWaitPercent) {
		this.IOWaitPercent = IOWaitPercent;
	}
	@Override
	public float getIOWaitPercent() {
		// TODO Auto-generated method stub
		return IOWaitPercent;
	}
}
