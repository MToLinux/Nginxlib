package org.cs2c.nginlib.monitor;

public class RecNetworkStatus implements NetworkStatus {
	
	float InputKbPerSec = 0;
	float OutputPerSec = 0;

	public void setInputKbPerSec(float InputKbPerSec) {
		this.InputKbPerSec = InputKbPerSec;
	}
	@Override
	public float getInputKbPerSec() {
		// TODO Auto-generated method stub
		return InputKbPerSec;
	}

	public void setOutputPerSec(float OutputPerSec) {
		this.OutputPerSec = OutputPerSec;
	}
	@Override
	public float getOutputPerSec() {
		// TODO Auto-generated method stub
		return OutputPerSec;
	}

}
