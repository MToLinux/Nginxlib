package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class RecBlock implements Block,Element {
	
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

	public void SetConfBlockText(String BlockText) {
		confBlockText = BlockText;
	}
	
	@Override
	public List<Block> getBlocks() {
		//String conf = ReadConf();
		List<Block> list = new ArrayList<Block>();
		
		GetSubBlock();
		//Iterator iter = hbMap.entrySet().iterator();
	    for (Entry<String, String> entry : hbMap.entrySet()) {
            System.out.println("Key:" + entry.getKey() + "value:" + entry.getValue().toString());
    		Block objblock = new RecBlock();
    		// get name
    		objblock.setName(entry.getKey());
    		list.add(objblock);
        }
		return list;
	}

	@Override
	public List<Directive> getDirectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBlock(Block block) {
		StringBuilder sb = new StringBuilder();
		String bltext = blockValue.substring(0, blockValue.length()-1);
		sb.append(bltext + "\n");
		sb.append(block.toString());
		sb.append("}");
		// set blockValue
		SetConfBlockText(sb.toString());
	}

	@Override
	public void addDirective(Directive directive) {
		StringBuilder sb = new StringBuilder();
		String bltext = blockValue.substring(0, blockValue.length()-1);
		sb.append(bltext + "\n");
		sb.append(directive.toString());
		sb.append("}");
		// set blockValue
		SetConfBlockText(sb.toString());
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
		// TODO test
		return obj;
	}
	
	@Override
	public String toString(){
		String blocktext = GetBlockText(blockName);
		return blocktext;
	}
	
	public String GetBlockText(String gBlockName) {
		int StartLine = 1;
		return GetBlockText(gBlockName,StartLine);
	}
	
	/**
	 * Get Block Text which Block Name is gBlockName.
	 * @return Block text.
	 * */
//	private String GetBlockText() {
	public String GetBlockText(String gBlockName,int StartLine) {
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    int nPreCount = 0;
	    boolean bBlock = false;
	    boolean bStartBlock = false;
	    
		try {
		    BufferedReader br = new BufferedReader(new StringReader(confBlockText));
		    StringBuilder sb = new StringBuilder();
		    
			while( (linetxt = br.readLine()) != null) {
				nPreCount++;
				if(nPreCount == StartLine){
					bStartBlock = true;
				}
				if(!bStartBlock){
					continue;
				}
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
	 * Get Sub Block.
	 * */
	private void GetSubBlock() {
		GetSubBlock(confBlockText);
	}
	
	/**
	 * Get Sub Block.
	 * */
	private void GetSubBlock(String Text) {
		String tempblname = null;
		String blname = null;
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    boolean bBlock = false;
		
		try {
		    BufferedReader br = new BufferedReader(new StringReader(Text));
		    StringBuilder sb = new StringBuilder();
		    
			//blname = GetBlockName(linetxt);
		    
			while( (linetxt = br.readLine()) != null) {
				tempblname = GetBlockName(linetxt);
				if((null != tempblname)&&(null == blname)){
					blname = tempblname;
					bBlock = true;
					//System.out.println("blname:" + blname);
				}
				
				// make sure text start from the block name
				if(bBlock){
					if(IsNotComment(linetxt) && linetxt.contains("{")){
						nblockstart++;
					}
					if(IsNotComment(linetxt) && linetxt.contains("}")){
						nblockEnd++;
					}
				    sb.append(linetxt + "\n");
				
					if((null != blname) && (nblockstart!=0) && (nblockstart == nblockEnd)){
						blText = sb.toString();
						hbMap.put(blname, blText);
						//System.out.println("blname:" + blname + "   blText:" + blText);
						// close one block text,loop next.
						bBlock = false;
						blname = null;
						sb.delete(0, sb.length());
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String GetBlockName(String linetxt) {
		String bname = null;
		boolean bret = IsComment(linetxt);
		if(bret){
			return null;
		}
		
		if(linetxt.contains("{")){
			int endIndex=linetxt.trim().lastIndexOf(" ");
			bname = linetxt.trim().substring(0, endIndex);
		}
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
	

//	public Block getBlock() {
//		// TODO Auto-generated method stub
//		return objBlock;
//	}
	
	private String GetDirectiveName(String linetxt) {
		String bname = null;
		boolean bCret = IsComment(linetxt);
		if(bCret){
			return null;
		}
		
		boolean bEret = NotEndWithSemicolon(linetxt);
		if(bEret){
			return null;
		}
		
		String[] lineArray=linetxt.trim().split(" ");
		String dName = lineArray[0];
		
		if(dName != null){
			// Add to array
			// TODO
		}
		
		return bname;
	}
	
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
	
	private boolean EndWithSemicolon(String linetxt) {
		String suffix = ";";
		if(linetxt.trim().endsWith(suffix)){
			return true;
		}
		else{
			return false;
		}
	}
	private boolean NotEndWithSemicolon(String linetxt) {
		String suffix = ";";
		if(linetxt.trim().endsWith(suffix)){
			return false;
		}
		else{
			return true;
		}
	}

}