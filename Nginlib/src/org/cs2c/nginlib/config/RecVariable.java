package org.cs2c.nginlib.config;

public class RecVariable implements Variable,Parameter {
	private String VariableName = null;
	
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
