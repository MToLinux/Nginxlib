package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class RecRemoteOperator implements RemoteOperator{
	private String confText = null;
	private String localConfPath = null;
	private String oldConfDatestamp = null;
	private RecAuthInfo creauthInfo = null;
	private String remoteConf = null;
	private Connection conn = null;
	
	public RecRemoteOperator(){
		
	}
	
	public RecRemoteOperator(AuthInfo reauthInfo,String midwarePath,Connection conn)
	{
		this.creauthInfo=(RecAuthInfo) reauthInfo;
		this.remoteConf=ConvertfullPath(midwarePath);
		this.conn=conn;
		
		this.localConfPath = System.getProperty("java.io.tmpdir");
//		System.out.println(localConfPath);
	}
	
	/**
	 * add nginx server path with "conf/nginx.conf"
	 * @param pathstr indicates nginx server path
	 * @return nginx server path add "conf/nginx.conf"
	 */
	private String ConvertfullPath(String pathstr)
	{
		String pathstrend = pathstr;
		if(!pathstr.substring(pathstr.length()-1).equals("/"))
		{
			pathstrend=pathstr + "/";
		}

		pathstrend += "conf/nginx.conf"; 
		return pathstrend;
	}
	
	/**
	 * Set local Confpath With full Name which parameter loPath indicates
	 * @param loPath local config path With full Name
	 */
	public void SetLocalConfpath(String loPath){
		this.localConfPath = loPath;
	}
	
	@Override
	public void append(Element element, String outerBlockNames)
			throws RemoteException {
		String editBlString = null;
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
//		System.out.println("BlockLength:"+BlockLength);
//		System.out.println("nblockNameNum"+nblockNameNum);

		RecBlock objRecBlock = new RecBlock();
		objRecBlock.setName(objHashMap.get("lastblockname"));
		objRecBlock.SetBlockText(BlockText);
//		System.out.println(element.toString());
		if(element instanceof Block){
			//check element is directive or block type
			objRecBlock.addBlock((Block)element);
		}else{
			objRecBlock.addDirective((Directive)element);
		}
		editBlString = objRecBlock.toString();
		
		String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlString+"\n"
				+GetSufBlockText(confText,nblockNameNum+BlockLength);

		// write to local conf file
		WriteConf(newConfText);
		WriteRemoteConf();
	}
	
	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
	    if((null == element.toString()) || ("" == element.toString())){
	    	return;
	    }
	    
	    // outerBlockNames is "" search all conf
	    if("".equals(outerBlockNames.trim())){
	    	confText = ReadConf();
	    	//confText not contains element, return @
			String editconfText = BlockDeleteElement(confText,element);
			// write to local conf file
			WriteConf(editconfText);
	    }
	    else{
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
	
	    	//BlockText not contains element, return @
		    String editBlockText = BlockDeleteElement(BlockText,element);
			
			String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlockText
					+GetSufBlockText(confText,nblockNameNum+BlockLength);
	
			// write to local conf file
			WriteConf(newConfText);
	    }
	    
		WriteRemoteConf();
	}
	
	/**
	 * delete the Block's Element
	 * @return Block text which parameter element is deleted.
	 * */
	private String BlockDeleteElement(String BlockText,Element element){
		
		String strRtn = ReplaceString(BlockText,element.toString(), "");
		
		return strRtn;
	}
	/**
	 * ReplaceString strFrom with strTo in the text strSource.
	 * @param strSource
	 * @param strFrom
	 * @param strTo
	 * @return the text after replace
	 */
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
	    if("".equals(outerBlockNames.trim())){
	    	confText = ReadConf();
	    	//confText not contains element, return @
			String editconfText = BlockInsertAfter(confText,element,after);
			// write to local conf file
			WriteConf(editconfText);
			WriteRemoteConf();
			return;
	    }
	    
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
	    
		String editBlockText = BlockInsertAfter(BlockText,element,after);
		
		String newConfText = GetPreBlockText(confText,nblockNameNum)+editBlockText
				+GetSufBlockText(confText,nblockNameNum+BlockLength);
		
//		System.out.println(editBlockText);
		// write to local conf file
		WriteConf(newConfText);
		WriteRemoteConf();
	}
	
	/**
	 * put after element after BlockText's element element
	 * @param BlockText target Block Text
	 * @param element BlockText's element element
	 * @param after insert element
	 * @return edited Block Text
	 * @throws RemoteException
	 */
	private String BlockInsertAfter(String BlockText,Element element, Element after) throws RemoteException {
	    StringBuilder sbtext = new StringBuilder();
	    
		//Returns the index within this block text String of the first occurrence of the specified substring element.toString()
		int nelementLocation = BlockText.indexOf(element.toString());
		if(-1 == nelementLocation){
			// if no have element
			return BlockText;
		}
		
		sbtext.append(BlockText.substring(0, nelementLocation));
		sbtext.append(element.toString());
//		sbtext.append("\n");
		sbtext.append(after.toString());
		sbtext.append("\n");
		sbtext.append(BlockText.substring(nelementLocation+element.toString().length()));
		return sbtext.toString();
	}
	
	/**
	 * do function's common part
	 * @param outerBlockNames Indicate the target block position.
	 * @return block info in HashMap
	 * @throws RemoteException
	 */
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
		if(outerBlockNames.contains("|")){
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
		
		InfoHashMap.put("lastblockname", blname);
		InfoHashMap.put("blocktext", BlockText);
		InfoHashMap.put("blocklength",Integer.toString(BlockLength));
		InfoHashMap.put("nblocknamenum", Integer.toString(nblockNameNum));
		
		return InfoHashMap;
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
	    // outerBlockNames is "" search all conf
	    if("".equals(outerBlockNames.trim())){
	    	confText = ReadConf();
	    	//confText not contains element, return @
			String editconfText =BlockReplaceElement(confText,oldElement, newElement);
			// write to local conf file
			WriteConf(editconfText);
			WriteRemoteConf();
			return;
	    }

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
		
		WriteRemoteConf();
	}
	
	/**
	 * Replace the specific old element with the new element in the specific block which Block’s Text is BlockText
	 * @param BlockText
	 * @param oldElement
	 * @param newElement
	 * @return return the replaced result
	 */
	private String BlockReplaceElement(String BlockText,Element oldElement,Element newElement){
		
		String strRtn = ReplaceString(BlockText,oldElement.toString(), newElement.toString());
		
		return strRtn;
	}
	
	@Override
	public List<Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
		String BlockName = null;
		String BlockText = null;
		
		if( outerBlockNames == null){
			return null;
		}
		
	    if((null == blockName.toString()) || ("" == blockName.toString())){
	    	return null;
	    }
	    
	    GetRemoteConf(remoteConf);
	    
		if("".equals(outerBlockNames.trim())){
			// when outerBlockNames is "" ,search all nginx.conf file.
			if((null==confText) ||(""==confText) ){
				confText = ReadConf();				
			}
		    BlockName = "nginx.conf";
			BlockText = confText;
		}
		else{
		    //If there is no index in outerBlockNames, default the first block.
		    if(!outerBlockNames.contains(":")){
		    	outerBlockNames += ":0";
		    }
		    HashMap<String,String> objHashMap=EditCommon(outerBlockNames);
		    BlockName = objHashMap.get("lastblockname");
		    BlockText = objHashMap.get("blocktext");
		}
		
		RecBlock objRecBlock = new RecBlock();
		objRecBlock.setName(BlockName);
		objRecBlock.SetBlockText(BlockText);
//		System.out.println("BlockText:"+BlockText);
		return objRecBlock.getBlocks(blockName);
	}
	
	@Override
	public Block getRootBlock() throws RemoteException {
	    GetRemoteConf(remoteConf);
	    confText = ReadConf();
		RecBlock objRecBlock = new RecBlock();
		objRecBlock.setName("nginx.conf");
		objRecBlock.SetBlockText(confText);
		return objRecBlock;
	}
	
	/**
	 * Check the validate of OuterBlockNames.
	 * @param outerBlockNames
	 * @return
	 */
	private boolean CheckOuterBlockNames(String outerBlockNames) {
		if( outerBlockNames == null || "".equals(outerBlockNames.trim())){
			return true;
		}
		
		return false;
	}

	/**
	 * Get Block Length before nStartLine
	 * @param blname Indicates block's name
	 * @param nStartLine Indicates the start line
	 * @param nbindex Indicates the block's number
	 * @return the length before target block
	 * @throws RemoteException
	 */
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
	
	/**
	 * get the block's name
	 * @param linetxt may contain the block's name,or not return null.
	 * @return block's name,if linetxt do not have block's name return null
	 */
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
	 * Check the text is comment or not
	 * @param s :target text
	 * @return true:is comment,false:is not comment
	 */
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
	private void GetRemoteConf(String remoteFile) throws RemoteException
	{
        if(null == localConfPath || "" == localConfPath){
        	throw new RemoteException("local localConfPath is not correct.please set SetConfpathWithName()");
        }
		
		/* Now connect */
		try {
			oldConfDatestamp = getFileModifyTime();
			
			SCPClient scpc=conn.createSCPClient();
			scpc.get(remoteFile, localConfPath);
			/*no Close the connection */

		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * add local config Path with "nginx.conf"
	 * @return local config Path with "nginx.conf"
	 */
	private String GetlocalConfFullName(){
		return localConfPath + "nginx.conf";
	}
	
	/**
	 * Write the Remote nginx.conf file which is select.
	 * @throws RemoteException 
	 * */
	private void WriteRemoteConf() throws RemoteException{
        if(null == localConfPath || "" == localConfPath){
        	throw new RemoteException("local localConfPath is not correct.please set SetConfpathWithName()");
        }
        
		try {
			if(!CanCommitFile()){
	        	throw new RemoteException("the remote Nginx.conf has modified before.Please check remote Nginx.conf timestamp");
			}
			SCPClient scpc=conn.createSCPClient();
			
			String localFile = GetlocalConfFullName();
			String remoteConfDirectory = remoteConf.substring(0,
					remoteConf.lastIndexOf("/"));
			scpc.put(localFile, remoteConfDirectory);
			
			//update local datetimestamp
			oldConfDatestamp = getFileModifyTime();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * check weather the config file can commit
	 * @return false: can't commit,true:can commit
	 * @throws RemoteException
	 */
	private boolean CanCommitFile() throws RemoteException{
		String nowConfDatestamp = getFileModifyTime();
		if(StringToDate(oldConfDatestamp).compareTo(StringToDate(nowConfDatestamp)) != 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Format the string to date
	 * @param s indicates the target string 
	 * @return target string's date value
	 * @throws RemoteException
	 */
	private Date StringToDate(String s) throws RemoteException{
		if((null == s) || ("" == s)){
			throw new RemoteException("Datestamp error,Please check Configurator.getBlocks and get date.");
		}

		Date time=new Date();
		SimpleDateFormat sd=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		try{
			 time=sd.parse(s);
		}
		catch (ParseException e) {
			throw new RemoteException(e.getMessage());
		}
		return time;
	}
	
	/**
	 * get modify time of the remote server's config File
	 * @return nginx.conf's modify time
	 * @throws RemoteException
	 */
	private String getFileModifyTime() throws RemoteException {
		Session sess = null;
		try {
			/* Connect to the remote host and establish a Session */
			sess = conn.openSession();
			
			InputStream stdout = null;
			BufferedReader br = null;

			boolean cmdflag = true;
			
			/* Execute a command */
			String cmd = "stat "+remoteConf+" | grep -i Modify | awk -F. '{print $1}'";
//			System.out.println(cmd);
			sess.execCommand(cmd);

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			
		    StringBuilder sb = new StringBuilder();
		    String s ="";
		    
			while( (s = br.readLine()) != null) {
				sb.append(s);
				cmdflag = false;
			}
			
			stdout.close();
			br.close();
			
			if(cmdflag)
			{
				throw new RemoteException("Command stat "+remoteConf+" Execution failed.");
			}
			
			String af = sb.toString().substring(8).replace('-', '/');
//			System.out.println(af);
			return af;
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}finally{
			sess.close();
		}
	}

	/**
	 * Get the conf text which conf path is confPathWithName.
	 * @param config path with fullname.
	 * @return config text.
	 * @throws RemoteException 
	 * */
	private String ReadConf() throws RemoteException{
	    try {
		    // read conf
		    File file = new File(GetlocalConfFullName());
		    StringBuilder sb = new StringBuilder();
		    String s ="";

		    BufferedReader br = new BufferedReader(new FileReader(file));
		    
			while( (s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			sb.deleteCharAt(sb.lastIndexOf("\n"));
		    br.close();
		    String str = sb.toString();
		    return str;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * write the config file to local path.
	 * @param wcConftext Indicates config file's text string
	 * @throws RemoteException
	 */
	private void WriteConf(String wcConftext) throws RemoteException{
		try {
	    	FileWriter fw = null;
	    	fw = new FileWriter(GetlocalConfFullName());

		    fw.write(wcConftext);
			fw.close();

		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * get the text which before target text,and location is from special line. 
	 * @param Text：target text
	 * @param Index: special line
	 * @return the text which before target text
	 * @throws RemoteException
	 */
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
	
	/**
	 * get the text from line index indicates
	 * @param Text Indicates target text
	 * @param Index line index
	 * @return the text from line Index indicates
	 * @throws RemoteException
	 */
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
	
	/**
	 * Get Block Text which Block Name is gBlockName.before call GetBlockText you must set conf text.
	 * @param blname Block name to be search.
	 * @param Index :start location.
	 * @return block's Text
	 * @throws RemoteException 
	 * */
	private String GetBlockTextWithIndex(String blname,int Index) throws RemoteException{
		RecBlock rb = new RecBlock();
		rb.setName("nginx.conf");
		rb.SetBlockText(confText);
		
		String BlockText = rb.GetBlockText(blname,Index);
//		System.out.println("BlockText:"+BlockText);
		return BlockText;
	}
	
	/**
	 * the line count of Block's Text String
	 * @param BlockText :Block's Text String
	 * @return the line count of Block's Text String
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
}
