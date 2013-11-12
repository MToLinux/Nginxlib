package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cs2c.nginlib.RemoteException;

public class RecBlock implements Block,Element {
	
	private String blockName = null;
	private String blockValue = null;
	private String confText = null;
	
	List<Block> listGetBlocks = new ArrayList<Block>();
//	private Map<String,String> IhbMap=new IdentityHashMap<String,String>();
    
	@Override
	public void setName(String name) {
		blockName = name;
	}

	@Override
	public String getName() {
		return blockName;
	}

	public void SetBlockText(String BlockText) {
		blockValue = BlockText;
	}
	
	public void SetConfText(String outConfText) {
		confText = outConfText;
	}
	
	@Override
	public List<Block> getBlocks() throws RemoteException {
		
		GetSubBlock();

		return listGetBlocks;
	}

	@Override
	public List<Directive> getDirectives() throws RemoteException {
		List<Directive> list = new ArrayList<Directive>();

		String linetxt = null;
		String tempdname = null;
		String tempdtext = null;
		
	    BufferedReader br = new BufferedReader(new StringReader(blockValue));

		try {
			while( (linetxt  = br.readLine()) != null) {
				tempdname = GetDirectiveName(linetxt);
				if((null != tempdname)){
					tempdtext = linetxt.trim();
					//System.out.println("blname:" + blname);
					RecDirective objDirective = new RecDirective();
					objDirective.setName(tempdname);
					objDirective.SetDirectiveText(tempdtext);
		    		list.add(objDirective);
				}
			}
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public void addBlock(Block block) {
		StringBuilder sb = new StringBuilder();
		String bltext = blockValue.substring(0, blockValue.length()-2);
		String blEndtext = blockValue.substring(blockValue.length()-2, blockValue.length()-1);
		sb.append(bltext + "\n");
		sb.append(block.toString());
		sb.append(blEndtext);
		// set blockValue
		SetBlockText(sb.toString());
	}

	@Override
	public void addDirective(Directive directive) {
		StringBuilder sb = new StringBuilder();
		String bltext = blockValue.substring(0, blockValue.length()-2);
		String blEndtext = blockValue.substring(blockValue.length()-2, blockValue.length()-1);
		sb.append(bltext + "\n");
		sb.append(directive.toString()+ "\n");
		sb.append(blEndtext);
		// set blockValue
		SetBlockText(sb.toString());
	}
	
	@Override
	public Element clone() throws CloneNotSupportedException{
		RecBlock obj = null;
		obj=(RecBlock) super.clone();
		return obj;
	}
	
	@Override
	public String toString(){
		String blocktext=null;
		if((null != blockName) && (null != blockValue)){
			return blockValue;
		}else if((null != blockName) && (null == blockValue)){
			blocktext = GetBlockText(blockName);
			return blocktext;
		}else{
			return confText;
		}
	}
	 
	private String GetBlockText(String gBlockName) {
		int StartLine = 1;
		return GetBlockText(gBlockName,StartLine);
	}
	
	/**
	 * Get Block Text which Block Name is gBlockName.before GetBlockText you must set conf text.
	 * @param gBlockName Block name to be search.
	 * @return make sure StartLine's value > 0.
	 * */
	public String GetBlockText(String gBlockName,int StartLine) {
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    int nPreCount = 0;
	    boolean bBlock = false;
	    boolean bStartBlock = false;
	    
		try {
		    BufferedReader br = new BufferedReader(new StringReader(confText));
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
	 * Get Sub Block.
	 * @throws RemoteException 
	 * */
	private void GetSubBlock() throws RemoteException {
		GetSubBlocks(blockValue);
	}
	
	/**
	 * Get Sub Blocks.
	 * @throws RemoteException 
	 * */
	private void GetSubBlocks(String Text) throws RemoteException {
		String tempblname = null;
		String blname = null;
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    int nlinecount = 0;
	    boolean bBlock = false;
		
		try {
		    BufferedReader br = new BufferedReader(new StringReader(Text));
		    StringBuilder sb = new StringBuilder();
		    		    
			while( (linetxt = br.readLine()) != null) {
				tempblname = GetBlockName(linetxt);
				if((null != tempblname)&&(null == blname)){
					blname = tempblname;
					bBlock = true;
//					System.out.println("blname:" + blname);
				}
				
				// make sure text start from the block name
				if(bBlock){
					if(IsNotComment(linetxt) && linetxt.contains("{")){
						nblockstart++;
					}
					if(IsNotComment(linetxt) && linetxt.contains("}")){
						nblockEnd++;
					}
					if((nlinecount==0)||((null != blname) && (nblockstart!=0) && (nblockstart == nblockEnd))){
					}else{
					    sb.append(linetxt + "\n");
					}
					nlinecount++;
					if((null != blname) && (nblockstart!=0) && (nblockstart == nblockEnd)){

						blText = sb.toString();
			            RecBlock objblock = new RecBlock();
			    		// get name
			    		objblock.setName(blname);
			    		objblock.SetBlockText(blText);
			    		listGetBlocks.add(objblock);
//						IhbMap.put(new String(blname), blText);
//						System.out.println("blname:" + blname + "   blText:" + blText+"\n");
						// close one block text,loop next.
						bBlock = false;
						blname = null;
						nlinecount = 0;
						sb.delete(0, sb.length());
						
						//get SubBlock and do recursion
						String blockcontent = GetBlockContent(blText);
			    		if(hasSubBlock(blockcontent)){
			    			GetSubBlocks(blockcontent);//recursion
			    		}
					}
				}
			}

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	private String GetBlockContent(String blText) throws IOException {
		String linetxt = null;
		int nBlockRowCount = 0;
		int nBlockRowLength = 0;
	    StringBuilder sb = new StringBuilder();
	    
	    BufferedReader br = new BufferedReader(new StringReader(blText));
	    
		nBlockRowLength = GetBlockLenth(blText);
		
		while( (linetxt = br.readLine()) != null) {
			nBlockRowCount++;
			if(1 == nBlockRowCount){
				continue;
			}
			if(nBlockRowLength == nBlockRowCount){
				continue;
			}

		    sb.append(linetxt + "\n");
		}
		return sb.toString();
	}
	
	private int GetBlockLenth(String BlockText) {
	    int nBlockLineCount = 0;

		try {
		    BufferedReader br = new BufferedReader(new StringReader(BlockText));
		    
			while( br.readLine() != null) {
				nBlockLineCount++;
			}

			return nBlockLineCount;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	private boolean hasSubBlock(String value) throws IOException {
		String linetxt = null;
		int nBlockNameCount = 0;
		
	    BufferedReader br = new BufferedReader(new StringReader(value));
		while( (linetxt = br.readLine()) != null) {
			if(HasBlockName(linetxt)){
				nBlockNameCount++;
			}
		}
		
		if(nBlockNameCount >= 1){
			return true;
		}else{
			return false;
		}
	}

	private String GetBlockName(String linetxt) {
		String bname = null;
		boolean bret = IsComment(linetxt);
		if(bret){
			return null;
		}
		
		if(linetxt.contains("{")){
			int endIndex=linetxt.trim().lastIndexOf("{");
			bname = linetxt.trim().substring(0, endIndex);
		}
		return bname;
	}

	/**
	 * Check the line whether has Block Name which hBlockName is indicate or not.
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
	
	private boolean HasBlockName(String linetxt) {
		boolean bHasBlockName=false;
		boolean bret = IsComment(linetxt);
		if(bret){
			return false;
		}
		
		if(null != GetBlockName(linetxt)){
			bHasBlockName = true;
		}else{
			bHasBlockName = false;
		}
		return bHasBlockName;
	}
	
	private String GetDirectiveName(String linetxt) {
		String dName = null;
		boolean bCret = IsComment(linetxt);
		if(bCret){
			return null;
		}
		
		boolean bEret = NotEndWithSemicolon(linetxt);
		if(bEret){
			return null;
		}
		
		String[] lineArray=linetxt.trim().split(" ");
		dName = lineArray[0];
		
		return dName;
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

	private boolean NotEndWithSemicolon(String linetxt) {
		String suffix = ";";
		if(linetxt.trim().endsWith(suffix)){
			return false;
		}
		else{
			return true;
		}
	}

	/*
	 * Query all blocks with the specific block name in the block
	 * */
	public List<Block> getBlocks(String gBblockName) throws RemoteException{
		GetSubBlock();
		List<Block> listBlWithBName = new ArrayList<Block>();
		
	    for (int i = 0;i<listGetBlocks.size();i++) {
            RecBlock objblock = new RecBlock();
            
    		// get name
            if(gBblockName.equals(listGetBlocks.get(i).getName())){
	    		objblock = (RecBlock) listGetBlocks.get(i);
//	            System.out.println("objblock:" + objblock);
	            listBlWithBName.add(objblock);
            }
	    }
		return listBlWithBName;
	}

}
