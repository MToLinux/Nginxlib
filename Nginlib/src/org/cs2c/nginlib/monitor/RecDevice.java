package org.cs2c.nginlib.monitor;

public class RecDevice implements Device {
	
	String Name = "";
	float BlockWritenPerSec = 0;
	float BlockReadPerSec = 0;
	long BlockWriten = 0;
	long BlockRead = 0;
	float TPS = 0;

	public void setName(String Name) {
		this.Name = Name;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return Name;
	}

	public void setBlockWritenPerSec(float BlockWritenPerSec) {
		this.BlockWritenPerSec = BlockWritenPerSec;
	}
	@Override
	public float getBlockWritenPerSec() {
		// TODO Auto-generated method stub
		return BlockWritenPerSec;
	}

	public void setBlockReadPerSec(float BlockReadPerSec) {
		this.BlockReadPerSec = BlockReadPerSec;
	}
	@Override
	public float getBlockReadPerSec() {
		// TODO Auto-generated method stub
		return BlockReadPerSec;
	}

	public void setBlockWriten(long BlockWriten) {
		this.BlockWriten = BlockWriten;
	}
	@Override
	public long getBlockWriten() {
		// TODO Auto-generated method stub
		return BlockWriten;
	}

	public void setBlockRead(long BlockRead) {
		this.BlockRead = BlockRead;
	}
	@Override
	public long getBlockRead() {
		// TODO Auto-generated method stub
		return BlockRead;
	}

	public void setTPS(float TPS) {
		this.TPS = TPS;
	}
	@Override
	public float getTPS() {
		// TODO Auto-generated method stub
		return TPS;
	}

}
