package org.cs2c.nginlib.monitor;

public class RecNetworkStatus implements NetworkStatus {
	
	private float InputKbPerSec = 0;
	private float OutputPerSec = 0;

	public void setInputKbPerSec(float InputKbPerSec) {
		this.InputKbPerSec = InputKbPerSec;
	}
	@Override
	public float getInputKbPerSec() {
		return InputKbPerSec;
	}

	public void setOutputPerSec(float OutputPerSec) {
		this.OutputPerSec = OutputPerSec;
	}
	@Override
	public float getOutputPerSec() {
		return OutputPerSec;
	}

}
