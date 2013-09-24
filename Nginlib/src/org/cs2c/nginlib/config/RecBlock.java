package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecBlock implements Block {
	
	private String blockName = null;
	private String blockValue = null;
	private String confBlockText = null;
	private HashMap<String,String> hbMap=new HashMap<String,String>();
    
	@Override
	public void setName(String name) {
		blockName = name;
	}

	@Override
	public String getName() {
		return blockName;
	}

	@Override
	public List<Block> getBlocks() {
		//String conf = ReadConf();
		Block objblock = new RecBlock();
		// get name
		objblock.setName("name");
		
		List<Block> list = new ArrayList<Block>();
		list.add(objblock);
		return list;
	}

	@Override
	public List<Directive> getDirectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBlock(Block block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDirective(Directive directive) {
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
	
	@Override
	public String toString(){
		//String blockValue = GetBlockText();
		return blockValue;
	}
	
	/**
	 * Get Block Text.
	 * @return Block text.
	 * */
//	private String GetBlockText() {
	public String GetBlockText(String gBlockName) {
		String blname = null;
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    boolean bBlock = false;
	    
		try {
		    BufferedReader br = new BufferedReader(new StringReader(confBlockText));
		    StringBuilder sb = new StringBuilder();
		    
			while( (linetxt = br.readLine()) != null) {
				if(HasBlockName(gBlockName,linetxt)){
					// make sure text start from the block name
					bBlock = true;
				}
				
				if(bBlock){
					if(IsNotComment(linetxt) && linetxt.contains("{")){
						nblockstart++;
					}
					if(IsNotComment(linetxt) && linetxt.contains("}")){
						nblockEnd++;
					}
				    sb.append(linetxt + "\n");
				}
				
				if((nblockstart!=0) && (nblockstart == nblockEnd)){
					blText = sb.toString();
					break;
				}
			}
				
			return blText;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	


	/**
	 * Check whether the Block contains "{".
	 * @return if the Text contain Block return true,else return false.
	 * */
	private boolean CheckStartBlock(String Text) {
		if(Text.contains("{")){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Check Block.
	 * @return Block text.
	 * */
	private String GetSubBlock(String Text) {
		String blname = null;
		String blValue = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    boolean bBlock = false;
		String bname = null;
		
		try {
		    BufferedReader br = new BufferedReader(new StringReader(Text));
		    StringBuilder sb = new StringBuilder();
		    
			//blname = GetBlockName(linetxt);
		    
			while( (linetxt = br.readLine()) != null) {
				if(linetxt.contains("{")){
					String[] frages=linetxt.split(" ");
					bname=frages[0];
					
					nblockstart++;
					bBlock = true;
				}
				
				if(linetxt.contains("}")){
					nblockEnd++;
				}
				if(nblockstart == nblockEnd){
					blValue = sb.toString();
					hbMap.put(blname, blValue);
				}
				
				if(bBlock){
				    sb.append(linetxt + "\n");
				}
			}
			
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String GetBlockName(String linetxt) {
		String bname = null;
		if(linetxt.contains("{")){
			int endIndex=linetxt.trim().lastIndexOf(" ");
			bname = linetxt.trim().substring(0, endIndex);
		}
		
//		if(bname != null){
//			System.out.println("subname ："+ "\n"+bname);
//		}
		return bname;
	}

	/**
	 * Check the line whether has Block Name or not.
	 * @return Block text.
	 * */
	private boolean HasBlockName(String hBlockName,String linetxt) {
		boolean bHasBlockName=false;
		boolean bret = IsComment(linetxt);
		if(bret){
			return false;
		}
		
		if(hBlockName.equals(GetBlockName(linetxt))){
			bHasBlockName = true;
		}else{
			bHasBlockName = false;
		}
		return bHasBlockName;
	}
	
	/**
	 * Get Block Text.
	 * @return Block text.
	 * */
//	private String GetBlockText(String subBlockName) {
//	    String s ="";
//		try {
//		    BufferedReader br = new BufferedReader(new StringReader(confBlockText));
//		    StringBuilder sb = new StringBuilder();
//		    
//			while( (s = br.readLine()) != null) {
//				String[] frages=s.split(" ");
//				String dev=frages[0];
//
//				if(dev.equals(subBlockName) && s.contains("{")){
//				    sb.append(s + "\n");
//				}
//			}
//			
//			return sb.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	/**
	 * Get the conf text which conf path is parameter path.
	 * @param config path with fullname.
	 * @return config text.
	 * */
	public String ReadConf(String path){
	    try {
		    // read conf
		    File file = new File(path);
		    StringBuilder sb = new StringBuilder();
		    String s ="";

		    BufferedReader br = new BufferedReader(new FileReader(file));
		    
			while( (s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			
		    br.close();
		    String str = sb.toString();
		    confBlockText = str;
		    return str;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	public Block getBlock() {
//		// TODO Auto-generated method stub
//		return objBlock;
//	}

	private boolean IsComment(String s) {
		String prefix = "#";
		if(s.trim().startsWith(prefix)){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean IsNotComment(String linetxt) {
		String prefix = "#";
		if(linetxt.trim().startsWith(prefix)){
			return false;
		}
		else{
			return true;
		}
	}

}
