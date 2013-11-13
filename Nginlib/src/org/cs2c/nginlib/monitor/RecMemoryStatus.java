package org.cs2c.nginlib.monitor;

public class RecMemoryStatus implements MemoryStatus {

	private int TotalSwap = 0;
	private int UsedSwap = 0;
	private int SwapIn = 0;
	private int SwapOut = 0;
	private int Free = 0;
	private int Buffers = 0;
	private int Shared = 0;
	private int Cached = 0;
	private int Used = 0;
	
	public void setTotalSwap(int TotalSwap)
	{
		this.TotalSwap = TotalSwap;
	}
	@Override
	public int getTotalSwap() {
		return TotalSwap;
	}
	
	public void setUsedSwap(int UsedSwap)
	{
		this.UsedSwap = UsedSwap;
	}
	@Override
	public int getUsedSwap() {
		return UsedSwap;
	}

	public void setSwapIn(int SwapIn)
	{
		this.SwapIn = SwapIn;
	}
	@Override
	public int getSwapIn() {
		return SwapIn;
	}

	public void setSwapOut(int SwapOut)
	{
		this.SwapOut = SwapOut;
	}
	@Override
	public int getSwapOut() {
		return SwapOut;
	}

	public void setFree(int Free)
	{
		this.Free = Free;
	}
	@Override
	public int getFree() {
		return Free;
	}

	public void setBuffers(int Buffers)
	{
		this.Buffers = Buffers;
	}
	@Override
	public int getBuffers() {
		return Buffers;
	}

	public void setShared(int Shared)
	{
		this.Shared = Shared;
	}
	@Override
	public int getShared() {
		return Shared;
	}

	public void setCached(int Cached)
	{
		this.Cached = Cached;
	}
	@Override
	public int getCached() {
		return Cached;
	}

	public void setUsed(int Used)
	{
		this.Used = Used;
	}
	@Override
	public int getUsed() {
		return Used;
	}

}
