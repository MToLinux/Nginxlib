package org.cs2c.nginlib.monitor;

import java.util.List;

public class RecIOStatus implements IOStatus {
	
	private float BlockInPerSec = 0;
	private float BlockOutPerSec = 0;
	private List<Device> Devices = null;

	public void setBlockInPerSec(float BlockInPerSec) {
		this.BlockInPerSec = BlockInPerSec;
	}
	@Override
	public float getBlockInPerSec() {
		return BlockInPerSec;
	}

	public void setBlockOutPerSec(float BlockOutPerSec) {
		this.BlockOutPerSec = BlockOutPerSec;
	}
	@Override
	public float getBlockOutPerSec() {
		return BlockOutPerSec;
	}

	public void setDevices(List<Device> Devices) {
		this.Devices = Devices;
	}
	@Override
	public List<Device> getDevices() {
		return Devices;
	}

}
