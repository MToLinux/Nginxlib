package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;

public class RecRemoteOperator implements RemoteOperator{
	private String confText = null;
	private String confPathWithName = null;
	
	private RecAuthInfo creauthInfo;
	private String remoteTargetDirectory;
	
	public RecRemoteOperator()
	{
	}
	
	public RecRemoteOperator(AuthInfo reauthInfo,String midwarePath)
	{
		this.creauthInfo=(RecAuthInfo) reauthInfo;
		this.remoteTargetDirectory=midwarePath;
	}
	
	public void SetConfpathWithName(String PathWithName){
		confPathWithName = PathWithName;
	}
	
	@Override
	public void append(Element element, String outerBlockNames)
			throws RemoteException {
		
	    CheckOuterBlockNames(outerBlockNames);
	    if((null == element.toString()) || ("" == element.toString())){
	    	return;
	    }
	    
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }

	    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
	    String BlockText = objHashMap.get("blocktext");
	    int BlockLength = Integer.parseInt(objHashMap.get("blocklength"));
	    int nblockNameNum = Integer.parseInt(objHashMap.get("nblocknamenum"));

		String newConfText = GetPreBlockText(confText,nblockNameNum)+BlockText+element.toString()
				+GetSufBlockText(confText,nblockNameNum+BlockLength);

		// write to local conf file
		WriteConf(newConfText);
	}
	
	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
	    
	    if(CheckOuterBlockNames(outerBlockNames)){
	    	throw new RemoteException("outerBlockNames is not correct,outerBlockNames ="+outerBlockNames);
	    }
	    if((null == element.toString()) || ("" == element.toString())){
	    	return;
	    }
	    
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }

	    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
	    String BlockText = objHashMap.get("blocktext");
	    int BlockLength = Integer.parseInt(objHashMap.get("blocklength"));
	    int nblockNameNum = Integer.parseInt(objHashMap.get("nblocknamenum"));

		String editBlockText = BlockDeleteElement(BlockText,element);
		
		String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlockText
				+GetSufBlockText(confText,nblockNameNum+BlockLength);

		// write to local conf file
		WriteConf(newConfText);
	}
	
	/**
	 * 删除指定Block内的 Element元素。
	 * @return Block text which parameter element is deleted.
	 * */
	private String BlockDeleteElement(String BlockText,Element element){
		
		String strRtn = ReplaceString(BlockText,element.toString(), "");
		
		return strRtn;
	}
	private String ReplaceString(String strSource, String strFrom, String strTo) {
		 if (strSource == null) {
		   return null;
		 }
	     int i = 0;
	     if ((i = strSource.indexOf(strFrom, i)) >= 0) {
	       char[] cSrc = strSource.toCharArray();
	       char[] cTo = strTo.toCharArray();
	       int len = strFrom.length();
	       StringBuffer buf = new StringBuffer(cSrc.length);
	       buf.append(cSrc, 0, i).append(cTo);
	       i += len;
	       int j = i;
	       while ((i = strSource.indexOf(strFrom, i)) > 0) {
		buf.append(cSrc, j, i - j).append(cTo);
		i += len;
		j = i;
	       }
	       buf.append(cSrc, j, cSrc.length - j);
	       return buf.toString();
	     }
	     return strSource;
	}
	@Override
	public void insertAfter(Element element, Element after,
			String outerBlockNames) throws RemoteException {
	    
	    if(CheckOuterBlockNames(outerBlockNames)){
	    	throw new RemoteException("outerBlockNames is not correct,outerBlockNames ="+outerBlockNames);
	    }
	    
	    if((null == after.toString()) || ("" == after.toString())){
	    	return;
	    }
	    
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }
	    
	    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
	    String BlockText = objHashMap.get("blocktext");
	    int BlockLength = Integer.parseInt(objHashMap.get("blocklength"));
	    int nblockNameNum = Integer.parseInt(objHashMap.get("nblocknamenum"));

		String editBlockText = BlockInsertAfter(BlockText,BlockLength,element,after);
		
		String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlockText
				+GetSufBlockText(confText,nblockNameNum+BlockLength);

		// write to local conf file
		WriteConf(newConfText);
	}
	
	private String BlockInsertAfter(String BlockText,int BlockLength,Element element, Element after) throws RemoteException {
	    StringBuilder sbtext = new StringBuilder();
	    
		//找到第一个符合的element文本位置
		int nelementLocation = BlockText.indexOf(element.toString());
		sbtext.append(GetPreBlockText(BlockText,nelementLocation));
		sbtext.append(element.toString());
		sbtext.append(after.toString());
		sbtext.append(GetSufBlockText(BlockText,nelementLocation+BlockLength));

		return sbtext.toString();
	}
	
	private HashMap<String,String> EditCommon(String outerBlockNames) throws RemoteException{
		String nameAndIndex = null;
		String blname = null;
	    int nindex = 0;
	    int nLineCount = 0;
	    int nblockNameNum = 0;
		String BlockText = null;
		int BlockLength = 0;
		
	    HashMap<String,String> InfoHashMap = new HashMap<String,String>();
	    
		confText = ReadConf();
		
		//get location with outerBlockNames
		if("".equals(outerBlockNames.trim())){
			// when outerBlockNames is "" ,search all nginx.conf file.
			// TODO blname
			nblockNameNum = GetPreBlockLength(blname,nblockNameNum,nindex);
			
			BlockText = confText;
			BlockLength = GetBlockLenth(BlockText);
		}
		else if(outerBlockNames.contains("|")){
			String[] arrayOuterBNames=outerBlockNames.split("\\|");
			for(int i=0;i<arrayOuterBNames.length;i++){
				nameAndIndex = arrayOuterBNames[i];
				String[] lineArray=nameAndIndex.split(":");

				blname = lineArray[0];
				nindex = Integer.parseInt(lineArray[1]);
				nLineCount = GetPreBlockLength(blname,nblockNameNum,nindex);
				nblockNameNum += nLineCount;
			}
			
			BlockText = GetBlockTextWithIndex(blname,nblockNameNum);
			BlockLength = GetBlockLenth(BlockText);
		}else{
			String[] lineArray=outerBlockNames.split(":");
			blname = lineArray[0];
			nindex = Integer.parseInt(lineArray[1]);
			nLineCount = GetPreBlockLength(blname,nblockNameNum,nindex);
			nblockNameNum += nLineCount;
			
			BlockText = GetBlockTextWithIndex(blname,nblockNameNum);
			BlockLength = GetBlockLenth(BlockText);
		}
		

		
		InfoHashMap.put("blocktext", BlockText);
		InfoHashMap.put("blocklength",Integer.toString(BlockLength));
		InfoHashMap.put("nblocknamenum", Integer.toString(nblockNameNum));
		
		return InfoHashMap;
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
		
	    if(CheckOuterBlockNames(outerBlockNames)){
	    	throw new RemoteException("outerBlockNames is not correct,outerBlockNames ="+outerBlockNames);
	    }
	    
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }

	    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
	    String BlockText = objHashMap.get("blocktext");
	    int BlockLength = Integer.parseInt(objHashMap.get("blocklength"));
	    int nblockNameNum = Integer.parseInt(objHashMap.get("nblocknamenum"));

		String editBlockText = BlockReplaceElement(BlockText,oldElement, newElement);
		
		String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlockText
				+GetSufBlockText(confText,nblockNameNum+BlockLength);

		// write to local conf file
		WriteConf(newConfText);
	}
	private String BlockReplaceElement(String BlockText,Element oldElement,Element newElement){
		
		String strRtn = ReplaceString(BlockText,oldElement.toString(), newElement.toString());
		
		return strRtn;
	}
	
	@Override
	public List<Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
	    CheckOuterBlockNames(outerBlockNames);
	    if((null == blockName.toString()) || ("" == blockName.toString())){
	    	return null;
	    }
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }

	    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
	    String BlockText = objHashMap.get("blocktext");

		RecBlock objRecBlock = new RecBlock();
		objRecBlock.SetBlockText(BlockText);
		return objRecBlock.getBlocks(blockName);
		
	}

	private boolean CheckOuterBlockNames(String outerBlockNames) {
		if( outerBlockNames == null){
			return true;
		}
		
		return false;
	}

	private int GetPreBlockLength(String blname,int nStartLine,int nbindex) throws RemoteException {
		int nRepeat = -1;
		int nLineCount = -1;
		int nBlockLineCount = 0;
		boolean bblCount = false;
	    String linetxt =null;
	    BufferedReader br = new BufferedReader(new StringReader(confText));
	    
		try {
			while( (linetxt = br.readLine()) != null) {
				nLineCount++;
				if(nLineCount == nStartLine){
					bblCount = true;
				}
				
				if(bblCount){
					nBlockLineCount++;
					if(HasBlockName(blname,linetxt)){
						nRepeat++;
						if(nbindex == nRepeat){
							break;
						}
					}
				}
			}
			return nBlockLineCount;
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
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
	private boolean IsComment(String s) {
		String prefix = "#";
		if(s.trim().startsWith(prefix)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Get the nginx.conf file which nginx.conf fullpath is parameter remoteFile.
	 * @throws IOException 
	 * */
	public void GetRemoteConf(String remoteFile) throws IOException
	{
		String localTargetDirectory = "D:\\eclipseWorkspace\\confpath";

		Connection conn = new Connection(this.creauthInfo.getHostname());
		/* Now connect */
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.creauthInfo.getUsername(), this.creauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		
		SCPClient scpc=conn.createSCPClient();
		
		scpc.get(remoteFile, localTargetDirectory);
		/* Close the connection */
		conn.close();
	}
	
	/**
	 * Write the Remote nginx.conf file which is select.
	 * */
	public void WriteRemoteConf() throws IOException{
		Connection conn = new Connection(this.creauthInfo.getHostname());
		/* Now connect */
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.creauthInfo.getUsername(), this.creauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		
		SCPClient scpc=conn.createSCPClient();
//		Session sess = conn.openSession();
		
		System.out.println("Here is some information about the remote host:");
		
		String localFile = confPathWithName;
		scpc.put(localFile, remoteTargetDirectory);
		
		/* Close this session */
//		sess.close();
		/* Close the connection */
		conn.close();
		}

	/**
	 * Get the conf text which conf path is confPathWithName.
	 * @param config path with fullname.
	 * @return config text.
	 * @throws RemoteException 
	 * */
	public String ReadConf() throws RemoteException{
	    try {
		    // read conf
		    File file = new File(confPathWithName);
		    StringBuilder sb = new StringBuilder();
		    String s ="";

		    BufferedReader br = new BufferedReader(new FileReader(file));
		    
			while( (s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			
		    br.close();
		    String str = sb.toString();
		    return str;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	//write to local conf file
	private void WriteConf(String wcConftext) throws RemoteException{
		try {
	    	FileWriter fw = null;
	    	fw = new FileWriter(confPathWithName);

		    fw.write(wcConftext);
			fw.close();

		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	private String GetPreBlockText(String Text,int Index) throws RemoteException{
		String SufBlockText = null;
	    String linetxt =null;
	    int nPreCount = 0;
	    
	    BufferedReader br = new BufferedReader(new StringReader(Text));
	    StringBuilder sb = new StringBuilder();
	    
		try {
			while( (linetxt = br.readLine()) != null) {
				nPreCount++;
				if(nPreCount == Index){
					break;
				}
				sb.append(linetxt + "\n");
			}
			SufBlockText = sb.toString();
			
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
		return SufBlockText;
	}
	
	private String GetSufBlockText(String Text,int Index) throws RemoteException{
		String SufBlockText = null;
	    String linetxt =null;
	    int nPreCount = 0;
	    boolean bStartBlock = false;
	    
	    BufferedReader br = new BufferedReader(new StringReader(Text));
	    StringBuilder sb = new StringBuilder();
	    
		try {
			while( (linetxt = br.readLine()) != null) {
				nPreCount++;
				if(nPreCount == Index){
					bStartBlock = true;
				}
				
				if(!bStartBlock){
					continue;
				}
				sb.append(linetxt + "\n");
			}
			SufBlockText = sb.toString();
			
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
		return SufBlockText;
	}
	
	private String GetBlockTextWithIndex(String blname,int Index){
		
		RecBlock rb = new RecBlock();
		rb.SetConfText(confText);
		
		String BlockText = rb.GetBlockText(blname,Index);
		return BlockText;
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
}
