package org.cs2c.nginlib.config;

import java.util.List;

public class RecDirective implements Directive,Element{

	private String directiveName=null;
	private String directiveValue=null;
	
	public void SetDirectiveText(String DirectiveText) {
		directiveValue = DirectiveText;
	}
	
	@Override
	public void setName(String name) {
		directiveName = name;
	}

	@Override
	public String getName() {
		return directiveName;
	}

	@Override
	public List<Parameter> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addParameter(Parameter parameter) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Element clone(){
		Element obj=null;
		try{
			obj=(Element)super.clone();
		}
		catch (CloneNotSupportedException ex) {
			ex.printStackTrace(); 
		}
		//obj.setBegin((Date)this.getBegin().clone());
		// TODO
		return obj;
	}

}
