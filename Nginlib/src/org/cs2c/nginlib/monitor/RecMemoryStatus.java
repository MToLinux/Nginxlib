package org.cs2c.nginlib.monitor;

public class RecMemoryStatus implements MemoryStatus {

	int UsedSwap = 0;
	int SwapIn = 0;
	int SwapOut = 0;
	int Free = 0;
	int Buffers = 0;
	int Shared = 0;
	int Cached = 0;
	int Used = 0;
	
	public void setUsedSwap(int UsedSwap)
	{
		this.UsedSwap = UsedSwap;
	}
	@Override
	public int getUsedSwap() {
		// TODO Auto-generated method stub
		return UsedSwap;
	}

	public void setSwapIn(int SwapIn)
	{
		this.SwapIn = SwapIn;
	}
	@Override
	public int getSwapIn() {
		// TODO Auto-generated method stub
		return SwapIn;
	}

	public void setSwapOut(int SwapOut)
	{
		this.SwapOut = SwapOut;
	}
	@Override
	public int getSwapOut() {
		// TODO Auto-generated method stub
		return SwapOut;
	}

	public void setFree(int Free)
	{
		this.Free = Free;
	}
	@Override
	public int getFree() {
		// TODO Auto-generated method stub
		return Free;
	}

	public void setBuffers(int Buffers)
	{
		this.Buffers = Buffers;
	}
	@Override
	public int getBuffers() {
		// TODO Auto-generated method stub
		return Buffers;
	}

	public void setShared(int Shared)
	{
		this.Shared = Shared;
	}
	@Override
	public int getShared() {
		// TODO Auto-generated method stub
		return Shared;
	}

	public void setCached(int Cached)
	{
		this.Cached = Cached;
	}
	@Override
	public int getCached() {
		// TODO Auto-generated method stub
		return Cached;
	}

	public void setUsed(int Used)
	{
		this.Used = Used;
	}
	@Override
	public int getUsed() {
		// TODO Auto-generated method stub
		return Used;
	}

}
