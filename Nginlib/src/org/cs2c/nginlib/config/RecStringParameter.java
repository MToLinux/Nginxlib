package org.cs2c.nginlib.config;

public class RecStringParameter implements StringParameter {
	private String StringParameterValue = null;
	
	@Override
	public void setValue(String value) {
		StringParameterValue = value;
	}

	@Override
	public String getValue() {
		return StringParameterValue;
	}
}
