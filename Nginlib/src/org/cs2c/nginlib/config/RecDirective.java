package org.cs2c.nginlib.config;

import java.util.ArrayList;
import java.util.List;

public class RecDirective implements Directive,Element{

	private String directiveName=null;
	private String directiveValue=null;
	private String directiveComment = null;

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
		// Directive_name Option=9 StringParameter $Variable
		String divalue = directiveValue.substring(0, directiveValue.length()-1);
		String[] lineArray=divalue.split(" ");
		List<Parameter> listParam = new  ArrayList<Parameter>();
		
		for(int i=1;i<lineArray.length;i++){
			if(lineArray[i].contains("=")){
				RecOption objOption = new RecOption();
				String[] lineOption=lineArray[i].split("=");
				objOption.setName(lineOption[0]);
				objOption.setValue(lineOption[1]);
				listParam.add(objOption);
			}else if(lineArray[i].contains("$")){
				RecVariable objVariable = new RecVariable();
				objVariable.setName(lineArray[i].substring(1, lineArray[i].length()-1));
				listParam.add(objVariable);
			}else if(lineArray[i].length() == 0){

			}else{
				RecStringParameter objStringParameter = new RecStringParameter();
				objStringParameter.setValue(lineArray[i]);
				listParam.add(objStringParameter);
			}
		}

		return listParam;
	}
	
	@Override
	public String toString(){
		return directiveValue;
	}
	
	@Override
	public void addParameter(Parameter parameter) {
		StringBuilder sb = new StringBuilder();
		if((null == directiveValue) || ("" == directiveValue)){
			sb.append(directiveName+"  ");
			sb.append(parameter.toString()+";");
		}else{
			sb.append(directiveValue.substring(0, directiveValue.length()-1)+" ");
			sb.append(parameter.toString());
			sb.append(directiveValue.substring(directiveValue.length()-1, directiveValue.length()));
		}

		directiveValue = sb.toString();
	}
	
	@Override
	public Element clone() throws CloneNotSupportedException{
		RecDirective obj=null;
		obj=(RecDirective) super.clone();
		return obj;
	}

	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		return directiveComment;
	}

	@Override
	public void setComment(String comment) {
		// TODO Auto-generated method stub
		directiveComment = comment;
	}

}
