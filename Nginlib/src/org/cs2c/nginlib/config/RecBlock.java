package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.cs2c.nginlib.RemoteException;

public class RecBlock implements Block,Element {
	
	private String blockComment = new String();
	private String blockNameline = null;
	private String blockEndline = null;
	
	private String blockName = null;
	private String blockValue = null;
	private List<Element> myElements = new ArrayList<Element>();
//	int myElementsSize = 0;
	
	private List<Block> myBlocks = new ArrayList<Block>();
	private List<Directive> myDirectives = new ArrayList<Directive>();

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
	
	// blockNameline
	public void setNameline(String Nameline) {
		blockNameline = Nameline;
	}
	// blockNameline
	public String getNameline() {
		return blockNameline;
	}
	
	// blockEndline
	public void setblockEndline(String blEndline) {
		blockEndline = blEndline;
	}

	// blockEndline
	public String getblockEndline() {
		return blockEndline;
	}

	@Override
	public void setName(String name) {
		blockName = name;
	}

	@Override
	public String getName() {
		return blockName;
	}
	
	public void SetBlockText(String BlockText) throws RemoteException {
		blockValue = BlockText;
		GetBlSubElements();
	}
	
	@Override
	public List<Block> getBlocks() throws RemoteException {
//		GetBlSubElements();
		return myBlocks;
	}

	@Override
	public List<Directive> getDirectives() throws RemoteException {
//		GetBlSubElements();
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
//		myElementsSize = myElements.size();
	}

	@Override
	public void addBlock(Block block){
		
		int indexadd = myBlocks.size();
		myBlocks.add(indexadd, block);
		
		int index = myElements.size();
		myElements.add(index, block);
	}

	@Override
	public void addDirective(Directive directive){
		int indexadd = myDirectives.size();
		myDirectives.add(indexadd, directive);

		int index = myElements.size();
		myElements.add(index, directive);
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
		// start line
		String name = GetBlockNameLinetxt();
		if(null!=name){
			sb.append(name);
		}else{
			// please set block name
			return null;
		}
		
		for(int i=0;i<myElements.size();i++){
			String sbinComment = myElements.get(i).getComment();
			if(null != sbinComment){
//	            System.out.println(i+" sbinComment:" + sbinComment);
				sb.append(sbinComment);
			}
			if(null != myElements.get(i)){
				String sbin = myElements.get(i).toString();
//	            System.out.println(i+" Elements:" + sbin);
				sb.append(sbin);
			}
		}
		
		// end line
		if("" != name){
			String Endline = getblockEndline();
			if(null != Endline){
				sb.append(Endline);
			}else{
				sb.append("}"+ "\n");
			}
		}
		return sb.toString();
	}
	
	private String GetBlockNameLinetxt(){
		StringBuilder sb = new StringBuilder();
		if("nginx.conf" == blockName){
			// all nginx.conf
			return "";
		}else{
			if(null != getNameline()){
				return getNameline();
			}else{
				if(null != blockName){
					sb.append(blockName+" ");
					sb.append("{"+ "\n");
					return sb.toString();
				}else{
					return null;
				}
			}
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
	 * Get Sub Elements.
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

		boolean bBlock = false;
	    StringBuilder sbcomment = new StringBuilder();

		try {
			int getlinecount = GetBlockLenth(Text);
			if(0 == getlinecount){
				// throw
				return;
			}
			
		    BufferedReader br = new BufferedReader(new StringReader(Text));
		    StringBuilder sb = new StringBuilder();

			while( (linetxt = br.readLine()) != null) {
				nlinecount++;
//				System.out.println("nlinecount:" + nlinecount);
				tempblname = GetBlockName(linetxt);
				//check if start with himself name
				if((1 == nlinecount)&&(blockName.equals(tempblname))){
					this.setNameline(linetxt+ "\n");
					continue;
				}
				//	set block Endline 
				if(getlinecount == nlinecount){
					this.setblockEndline(sbcomment.toString() + linetxt + "\n");
					sbcomment.delete(0, sbcomment.length());
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
						//System.out.println("blname:" + blname);
						RecDirective objDirective = new RecDirective();
						objDirective.setComment(sbcomment.toString());
						objDirective.setName(tempdname);
						objDirective.SetDirectiveText(linetxt + "\n");
						myElements.add(objDirective);
						myDirectives.add(objDirective);
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

						//put block Elements
						myElements.add(objblock);
						myBlocks.add(objblock);
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
			if(("" == BlockText)||(null == BlockText)){
				return 0;
			}
			
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
	
//	private boolean HasBlockName(String linetxt) {
//		boolean bHasBlockName=false;
//		boolean bret = IsComment(linetxt);
//		if(bret){
//			return false;
//		}
//		
//		if(null != GetBlockName(linetxt)){
//			bHasBlockName = true;
//		}else{
//			bHasBlockName = false;
//		}
//		return bHasBlockName;
//	}
	
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
	protected List<Block> getBlocks(String gBblockName) throws RemoteException{
		GetBlSubElements();
		List<Block> listBlWithBName = new ArrayList<Block>();

	    for (int i = 0;i<myBlocks.size();i++) {
            RecBlock objblock = new RecBlock();
//            System.out.println("myBlocks.get(i):" + myBlocks.get(i).getName());
    		// get name
            if(gBblockName.equals(myBlocks.get(i).getName())){
	    		objblock = (RecBlock) myBlocks.get(i);
//	            System.out.println("objblock:" + objblock.toString());
	            listBlWithBName.add(objblock);
            }
	    }
		return listBlWithBName;
	}

	@Override
	public void deleteElement(Element element){
		if(element instanceof Block){
			//check element is directive or block type
			for(int i=0;i<myBlocks.size();i++){
				if(myBlocks.get(i) == element){
					myBlocks.remove(i);
				}
			}
		}else{
			for(int i=0;i<myDirectives.size();i++){
				if(myDirectives.get(i) == element){
					myDirectives.remove(i);
				}
			}
		}

		for(int i=0;i<myElements.size();i++){
			if(myElements.get(i) == element){
				myElements.remove(i);
			}
		}
		// set blockValue
//		SetBlockText(retBlock.toString());
	}
}
