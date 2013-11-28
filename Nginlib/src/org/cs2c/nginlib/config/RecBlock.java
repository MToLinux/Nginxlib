package org.cs2c.nginlib.config;

import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
//import java.util.HashMap;
//import java.util.IdentityHashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;

import java.util.Map;
import java.util.Map.Entry;

import org.cs2c.nginlib.RemoteException;

public class RecBlock implements Block,Element {
	
	private String blockComment = new String();
	
	private String blockName = null;
	private String blockValue = null;
//	private String confText = null;
//	List<Element> listElements = new ArrayList<Element>();
	private Hashtable<Integer,Element> myElements = new Hashtable<Integer,Element>();
	int myElementsSize = 0;
	
	private Map<Integer,Block> myBlocks = new HashMap<Integer,Block>();
	private Map<Integer,Directive> myDirectives = new HashMap<Integer,Directive>();

//	List<Block> listGetBlocks = new ArrayList<Block>();
//	private Map<String,String> IhbMap=new IdentityHashMap<String,String>();
	@Override
	public void setComment(String comment) {
		blockComment = comment;
	}
	
	@Override
	public String getComment() {
		return blockComment;
	}

	@Override
	public void setName(String name) {
		blockName = name;
	}

	@Override
	public String getName() {
		return blockName;
	}

	protected void SetBlockText(String BlockText) {
		blockValue = BlockText;
	}
	
//	protected void SetConfText(String outConfText) {
//		confText = outConfText;
//	}
	
	@Override
	public Map<Integer,Block> getBlocks() throws RemoteException {
		GetBlSubElements();
		return myBlocks;
	}

	@Override
	public Map<Integer, Directive> getDirectives() throws RemoteException {
		GetBlSubElements();
		return myDirectives;
	}
	
	private void GetBlSubElements() throws RemoteException {
		if(!myDirectives.isEmpty()){
			myDirectives.clear();
		}
		if(!myBlocks.isEmpty()){
			myBlocks.clear();
		}
		if(!myElements.isEmpty()){
			myElements.clear();
		}

		GetSubElements(blockValue);
		myElementsSize = myElements.size();
	}

	/* by new data type map
	private List<Directive> GetOwnDirectives(String strBlocktxt) throws IOException {

	    int nblockstart = 0;
	    int nblockEnd = 0;
	    int nblockLineCount = 0;
	    String linetxt = "";
		List<Directive> list = new ArrayList<Directive>();
		boolean bBlock = false;
		
	    BufferedReader br = new BufferedReader(new StringReader(strBlocktxt));
		while( (linetxt  = br.readLine()) != null) {
			nblockLineCount++;
			if(nblockLineCount <= 1){
				continue;
			}

			// ignore sub block's Directive
			if(HasBlockName(linetxt)){
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
			}
			if((bBlock) && (nblockstart!=0) && (nblockstart == nblockEnd)){
				//end sub block
				bBlock = false;
			}

			if(!bBlock){
				String tempdname = GetDirectiveName(linetxt);
				if((null != tempdname)){
					String tempdtext = linetxt.trim();
					//System.out.println("DirectiveName:" + blname);
					RecDirective objDirective = new RecDirective();
					objDirective.setName(tempdname);
					objDirective.SetDirectiveText(tempdtext);
//					System.out.println("tempdtext:"+tempdtext);
		    		list.add(objDirective);
				}
			}
		}
		
		return list;
	}
*/
	@Override
	public void addBlock(Block block) throws RemoteException {
		AddString(block.toString());
	}

	@Override
	public void addDirective(Directive directive) throws RemoteException {
		AddString(directive.toString());
	}
	
	@Override
	public Element clone() throws CloneNotSupportedException{
		RecBlock obj = null;
		obj=(RecBlock) super.clone();
		return obj;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if((null != blockName) && ("" != blockName)){
			if("nginx.conf" == blockName){
				// all nginx.conf
				if(null != blockValue){
					return blockValue;
				}else{
					return "";
				}
			}else{
				if(null == blockValue){
					sb.append(blockName+" ");
					sb.append("{"+ "\n");
					sb.append("}"+ "\n");
					return sb.toString();
				}else{
					return blockValue;
				}
			}
		}else{
			// blockName == null
			return null;
		}
	}
	 
	private String GetBlockText(String gBlockName) throws RemoteException {
		int StartLine = 1;
		return GetBlockText(gBlockName,StartLine);
	}
	
	/**
	 * Get Block Text which Block Name is gBlockName.before GetBlockText you must set conf text.
	 * @param gBlockName Block name to be search.
	 * @return make sure StartLine's value > 0.
	 * @throws RemoteException 
	 * */
	protected String GetBlockText(String gBlockName,int StartLine) throws RemoteException {
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
	    int nPreCount = 0;
	    boolean bBlock = false;
	    boolean bStartBlock = false;
	    
		try {
		    BufferedReader br = new BufferedReader(new StringReader(blockValue));
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
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Get Sub Blocks.
	 * @throws RemoteException 
	 * */
	private void GetSubElements(String Text) throws RemoteException {
		String tempblname = null;
		String blname = null;
		String blText = null;
	    String linetxt =null;
	    int nblockstart = 0;
	    int nblockEnd = 0;
		int nlinecount = 0;
		int nelementscount = -1;

		boolean bBlock = false;
	    StringBuilder sbcomment = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new StringReader(Text));
		    StringBuilder sb = new StringBuilder();

			while( (linetxt = br.readLine()) != null) {
				nlinecount++;
//				System.out.println("nlinecount:" + nlinecount);
				tempblname = GetBlockName(linetxt);
				//check if start with himself name
				if((1 == nlinecount)&&(blockName.equals(tempblname))){
					continue;
				}

				if((null != tempblname)&&(null == blname)){
					blname = tempblname;
					bBlock = true;
				}
				
				// check if Elements is directive
				if(!bBlock){
					//get directive
					String tempdname = GetDirectiveName(linetxt);
					if((null != tempdname)){
						nelementscount++;
						//System.out.println("blname:" + blname);
						RecDirective objDirective = new RecDirective();
						objDirective.setComment(sbcomment.toString());
						objDirective.setName(tempdname);
						objDirective.SetDirectiveText(linetxt + "\n");
						myElements.put(nelementscount, objDirective);
						myDirectives.put(nelementscount, objDirective);
						sbcomment.delete(0, sbcomment.length());
					}else{
						//make all sub element's comment
						sbcomment.append(linetxt + "\n");
					}
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
			            RecBlock objblock = new RecBlock();
			    		// get name
			    		objblock.setName(blname);
			    		objblock.SetBlockText(blText);
			    		objblock.setComment(sbcomment.toString());
						sbcomment.delete(0, sbcomment.length());
						nelementscount++;
						//put block Elements
						myElements.put(nelementscount, objblock);
						myBlocks.put(nelementscount, objblock);
//			    		listGetBlocks.add(objblock);
//						System.out.println("blname:" + blname + "   blText:" + blText+"\n");
						// close one block text,loop next.
						bBlock = false;
						blname = null;
						sb.delete(0, sb.length());
					}
				}
			}

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

/*
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
*/
	
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
			bname = linetxt.trim().substring(0, endIndex).trim();
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
	    
		// Comment ignore
		boolean bCret = IsComment(linetxt);
		if(bCret){
			return null;
		}
		
		// is not end with ; ignore
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
	protected Map<Integer,Block> getBlocks(String gBblockName) throws RemoteException{
		GetBlSubElements();

		Map<Integer,Block> retBlocks = new HashMap<Integer,Block>();
		
		Iterator<Entry<Integer, Block>> it = myBlocks.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Block> entry = (Entry<Integer, Block>)it.next();
			String blname = entry.getValue().getName();
    		// get name
            if(gBblockName.equals(blname)){
//	            System.out.println("objblock:" + objblock);
	    		retBlocks.put(entry.getKey(), entry.getValue());
            }
	    }
		return retBlocks;
	}

	@Override
	public void deleteElement(Integer eleindex)
			throws RemoteException {
		RecBlock retBlock = new RecBlock();
		retBlock.setName(blockName);

		for(int i=0;i<myElementsSize;i++){
			if(!eleindex.equals(i)){
				if(null != myElements.get(i)){
					retBlock.AddString(myElements.get(i).getComment());
					retBlock.AddString(myElements.get(i).toString());

//		            System.out.println(i+" Elements:" + myElements.get(i).toString());
//		            System.out.println(" addElement:" + i);
				}
			}else{
				myElements.remove(i);
//	            System.out.println(" deleteElement:" + i);
			}
		}
		// set blockValue
		SetBlockText(retBlock.toString());
	}

	private void AddString(String element) throws RemoteException {
		StringBuilder sb = new StringBuilder();
		
		//TODO
//        System.out.println(" blockName:" + blockName);
//        System.out.println(" blockValue:" + blockValue);
//        System.out.println(" this.toString():" + this.toString());

		// func : if blockName is "nginx.conf" means all conf
		if("nginx.conf" == blockName){
			String conftxt = this.toString();
			if("" == conftxt){
				sb.append(element);
			}else{
				sb.append(conftxt);
				sb.append(element);
			}
		}else{
			// real block(except nginx.conf root block)
			sb.append(AppendElement(element));
		}

		// set blockValue
		SetBlockText(sb.toString());
	}
	
/*
	@Override
	public void replaceElement(Element newElement, Integer eleindex)
			throws RemoteException {
		RecBlock retBlock = new RecBlock();
		retBlock.setName(blockName);

		for(int i=0;i<myElements.size();i++){
			if(eleindex.equals(i)){
				retBlock.AddString(newElement.toString());
			}else{
				retBlock.AddString(myElements.get(i).getComment());
				retBlock.AddString(myElements.get(i).toString());
			}
		}
		
		// set blockValue
		SetBlockText(retBlock.toString());
	}
*/

	private String AppendElement(String element) throws RemoteException {
		StringBuilder sb = new StringBuilder();
		String blalltext = null;
		String linetxt = null;
		String Endlinetxt = null;
		int nBlockRowCount = 0;
		boolean bEndlineBigslogan = false;
		try {
			// func : blockValue is null
			if(null == blockValue){
				sb.append(blockName+" ");
				sb.append("{"+ "\n");
				sb.append(element+ "\n");
				sb.append("}"+ "\n");
			}else{
				blalltext = this.toString();
//				System.out.println("bltext :"+blalltext);
				//check if last line trim() is "}"
				int linecount = GetBlockLenth(blalltext);
			    BufferedReader br = new BufferedReader(new StringReader(blalltext));
				while( (linetxt = br.readLine()) != null) {
					nBlockRowCount++;
					if(nBlockRowCount == linecount){
						if(linetxt.trim().equals("}"));{
							bEndlineBigslogan = true;
						}
					}
				}
				
				if(bEndlineBigslogan){
					//if last line trim() is "}"
					nBlockRowCount = 0;
					StringBuilder sbtemp = new StringBuilder();
				    BufferedReader br1 = new BufferedReader(new StringReader(blalltext));
					while( (linetxt = br1.readLine()) != null) {
						nBlockRowCount++;
						if(nBlockRowCount == linecount){
							Endlinetxt = linetxt;
						}else{
							sbtemp.append(linetxt+"\n");
						}
					}
					sb.append(sbtemp.toString());
					sb.append(element);
					sb.append(Endlinetxt);
				}else{
					// if end with }}
					String bltext = blalltext.substring(0, blalltext.length()-2);
					String blEndtext = blalltext.substring(blalltext.length()-2, blalltext.length()-1);
	//				System.out.println("bltext :"+bltext);
					sb.append(bltext);
					sb.append(element);
					sb.append("\n");
					sb.append(blEndtext);
				}
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}
}
