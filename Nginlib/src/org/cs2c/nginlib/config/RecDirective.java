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
//		System.out.println("DirectiveText:"+DirectiveText);
		GetSubParameters();
	}
	
	private void GetSubParameters() {
		if(!listParam.isEmpty()){
			listParam.clear();
		}

		GetSubParam(directiveValue);
	}

	private void GetSubParam(String diValue) {
		int Indexfrom = 0;
		int Indexto = 0;
		String paraSpace = null;
		String trimValue = null;

		//check
		if(diValue.length() <= directiveName.length()){
			//do not have param
			return;
		}
		
		this.SetNameupspace(diValue);
		//setUpSpace use
		Indexfrom = diValue.indexOf(directiveName)+directiveName.length();
		//editValue=   StringParameter1     StringParameter3   StringParameter4
		String editValue = diValue.substring(Indexfrom,diValue.length()-1);

		// Directive_name Option=9 StringParameter $Variable
		String temdi = diValue.substring(Indexfrom).trim();
		String divalueOutlast = temdi.substring(0, temdi.length()-1);
		String[] lineArray=divalueOutlast.split(" ");

		for(int i=0;i<lineArray.length;i++){
			if(lineArray[i].length() == 0){
				continue;
			}
//			System.out.println("lineArray[i]:"+lineArray[i]);
			// setUpSpace use start:
			//trimValue=StringParameter3   StringParameter4
			trimValue = editValue.trim();
			Indexto = editValue.length()-trimValue.length();
			paraSpace = editValue.substring(0,Indexto);
			//editValue=   StringParameter4
			editValue = editValue.substring(Indexto+lineArray[i].length());
			// setUpSpace use end

//			System.out.println("Indexto:"+Indexto);
			
			if(lineArray[i].contains("=")){
				RecOption objOption = new RecOption();
				String[] lineOption=lineArray[i].split("=");
				objOption.setName(lineOption[0]);
				objOption.setValue(lineOption[1]);
				objOption.setUpSpace(paraSpace);
				listParam.add(objOption);
			}else if(lineArray[i].contains("$")){
				RecVariable objVariable = new RecVariable();
				objVariable.setName(lineArray[i].substring(1, lineArray[i].length()-1));
				objVariable.setUpSpace(paraSpace);

				listParam.add(objVariable);
			}else if(lineArray[i].length() == 0){

			}else{
				RecStringParameter objStringParameter = new RecStringParameter();
				objStringParameter.setValue(lineArray[i]);
				objStringParameter.setUpSpace(paraSpace);
//				System.out.println("StringParameter:"+lineArray[i]);
				listParam.add(objStringParameter);
			}
		}
	}

	private void SetNameupspace(String directiveV) {
		int n = directiveV.indexOf(directiveName);
		String divalue = directiveV.substring(0, n);
		divalue = divalue+directiveName;
//		System.out.println("setNameAndupspace:"+divalue);//
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
			sb.append(name);
		}else{
			// please set directiveName
			return null;
		}
		
		for(int i=0;i<listParam.size();i++){
			if(null != listParam.get(i)){
				String sbin = listParam.get(i).toString();
//	            System.out.println(i+" Elements:" + myElements.get(i).toString());
				if(null == listParam.get(i).getUpSpace()){
					sb.append("  ");
				}else{
					sb.append(listParam.get(i).getUpSpace());//TODO
				}
				sb.append(sbin);
			}
		}
		//end directive
		sb.append(";");
		sb.append("\n");
//        System.out.println("sb.toString():" + sb.toString());
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
		if(null != diNameAndupspace){
			return diNameAndupspace;
		}else{
			return directiveName;
		}
	}
	public void setNameAndupspace(String NameAndupspace) {
		diNameAndupspace = NameAndupspace;
	}
}
