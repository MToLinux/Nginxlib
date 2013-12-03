package org.cs2c.nginlib.config;

public class RecOption implements Option,Parameter {

	private String optionName = null;
	private String optionValue = null;
	private String optionUpSpace = null;
	@Override
	public void setUpSpace(String UpSpace) {
		optionUpSpace = UpSpace;
	}
	@Override
	public String getUpSpace() {
		return optionUpSpace;
	}

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
