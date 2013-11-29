package org.cs2c.nginlib.config;

import java.util.ArrayList;
import java.util.List;

public class RecDirective implements Directive,Element{

	private String directiveName=null;
	private String directiveValue=null;
	private String directiveComment = null;
	
	private String diNameAndupspace=null;

	private List<Parameter> listParam = new  ArrayList<Parameter>();

	protected void SetDirectiveText(String DirectiveText) {
		directiveValue = DirectiveText;
		GetSubParameters();
	}
	
	private void GetSubParameters() {
		if(!listParam.isEmpty()){
			listParam.clear();
		}

		GetSubParam(directiveValue);
	}

	private void GetSubParam(String directiveValue2) {
		
		SetNameupspace(directiveValue2);
		// Directive_name Option=9 StringParameter $Variable
		String divalue = directiveValue2.substring(0, directiveValue2.length()-1);
		String[] lineArray=divalue.split(" ");
		
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
	}

	private void SetNameupspace(String directiveV) {
		int n = directiveV.lastIndexOf(directiveName);
		String divalue = directiveV.substring(0, n);
		divalue = divalue+directiveName;
		setNameAndupspace(divalue);
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
		return listParam;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		// start line
		String name = this.getNameAndupspace();
		if(null!=name){
			sb.append(name+" ");
		}else{
			// please set directiveName
			return null;
		}
		
		for(int i=0;i<listParam.size();i++){
			if(null != listParam.get(i)){
				String sbin = listParam.get(i).toString();
//	            System.out.println(i+" Elements:" + myElements.get(i).toString());
				sb.append(" "+sbin);
			}
		}
		//end directive
		sb.append(";");
		
		return sb.toString();		
	}
	
	@Override
	public void addParameter(Parameter parameter) {
		int index = listParam.size();
		listParam.add(index, parameter);
	}
	
	@Override
	public void deleteParameter(Parameter parameter) {
		for(int i=0;i<listParam.size();i++){
			if(listParam.get(i) == parameter){
				listParam.remove(i);
			}
		}
	}
	@Override
	public Element clone() throws CloneNotSupportedException{
		RecDirective obj=null;
		obj=(RecDirective) super.clone();
		return obj;
	}

	@Override
	public String getComment() {
		return directiveComment;
	}

	@Override
	public void setComment(String comment) {
		directiveComment = comment;
	}
	
	public String getNameAndupspace() {
		return diNameAndupspace;
	}
	public void setNameAndupspace(String NameAndupspace) {
		diNameAndupspace = NameAndupspace;
	}
}
