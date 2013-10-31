package org.cs2c.nginlib.config;

public class RecOption implements Option,Parameter {

	private String optionName = null;
	private String optionValue = null;
	
	@Override
	public void setName(String name) {
		optionName = name;
	}

	@Override
	public String getName() {
		return optionName;
	}

	@Override
	public void setValue(String value) {
		optionValue = value;
		
	}

	@Override
	public String getValue() {
		return optionValue;
	}
	
	@Override
	public String toString(){
		if((null == optionName)&&(null == optionValue))
		{
			return null;
		}else{
			return optionName +"="+optionValue;
		}
	}
}
