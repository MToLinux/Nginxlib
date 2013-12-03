package org.cs2c.nginlib.config;

public class RecVariable implements Variable,Parameter {
	private String VariableName = null;
	private String VariableUpSpace = null;
	
	@Override
	public void setUpSpace(String UpSpace) {
		VariableUpSpace = UpSpace;
	}
	@Override
	public String getUpSpace() {
		return VariableUpSpace;
	}
	
	@Override
	public void setName(String name) {
		VariableName = name;
	}

	@Override
	public String getName() {
		return VariableName;
	}
	
	@Override
	public String toString(){
		if(null == VariableName)
		{
			return null;
		}else{
			return "$"+VariableName;
		}
	}
}
