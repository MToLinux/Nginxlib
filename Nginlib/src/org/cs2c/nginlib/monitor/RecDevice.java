package org.cs2c.nginlib.monitor;

public class RecDevice implements Device {
	
	private String Name = "";
	private float BlockWritenPerSec = 0;
	private float BlockReadPerSec = 0;
	private long BlockWriten = 0;
	private long BlockRead = 0;
	private float TPS = 0;

	public void setName(String Name) {
		this.Name = Name;
	}
	@Override
	public String getName() {
		return Name;
	}

	public void setBlockWritenPerSec(float BlockWritenPerSec) {
		this.BlockWritenPerSec = BlockWritenPerSec;
	}
	@Override
	public float getBlockWritenPerSec() {
		return BlockWritenPerSec;
	}

	public void setBlockReadPerSec(float BlockReadPerSec) {
		this.BlockReadPerSec = BlockReadPerSec;
	}
	@Override
	public float getBlockReadPerSec() {
		return BlockReadPerSec;
	}

	public void setBlockWriten(long BlockWriten) {
		this.BlockWriten = BlockWriten;
	}
	@Override
	public long getBlockWriten() {
		return BlockWriten;
	}

	public void setBlockRead(long BlockRead) {
		this.BlockRead = BlockRead;
	}
	@Override
	public long getBlockRead() {
		return BlockRead;
	}

	public void setTPS(float TPS) {
		this.TPS = TPS;
	}
	@Override
	public float getTPS() {
		return TPS;
	}

}
