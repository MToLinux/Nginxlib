package org.cs2c.nginlib.config;

public class RecStringParameter implements StringParameter,Parameter {
	private String StringParameterValue = null;
	private String StringParaUpSpace = null;
	@Override
	public void setUpSpace(String UpSpace) {
		StringParaUpSpace = UpSpace;
	}
	@Override
	public String getUpSpace() {
		return StringParaUpSpace;
	}

	@Override
	public void setValue(String value) {
		StringParameterValue = value;
	}

	@Override
	public String getValue() {
		return StringParameterValue;
	}
	@Override
	public String toString(){
		return StringParameterValue;
	}
}
