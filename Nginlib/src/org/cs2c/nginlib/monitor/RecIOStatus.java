package org.cs2c.nginlib.monitor;

import java.util.List;

public class RecIOStatus implements IOStatus {
	
	float BlockInPerSec = 0;
	float BlockOutPerSec = 0;
	List<Device> Devices = null;

	public void setBlockInPerSec(float BlockInPerSec) {
		this.BlockInPerSec = BlockInPerSec;
	}
	@Override
	public float getBlockInPerSec() {
		// TODO Auto-generated method stub
		return BlockInPerSec;
	}

	public void setBlockOutPerSec(float BlockOutPerSec) {
		this.BlockOutPerSec = BlockOutPerSec;
	}
	@Override
	public float getBlockOutPerSec() {
		// TODO Auto-generated method stub
		return BlockOutPerSec;
	}

	public void setDevices(List<Device> Devices) {
		this.Devices = Devices;
	}
	@Override
	public List<Device> getDevices() {
		// TODO Auto-generated method stub
		return Devices;
	}

}
