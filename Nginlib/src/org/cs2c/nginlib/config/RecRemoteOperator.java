package org.cs2c.nginlib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
//	private Map<String, String> identityMap = new IdentityHashMap<String, String>(); 
	
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
		String nameAndIndex = null;
		String blname = null;
	    int nindex = 0;
	    int nLineCount = 0;
	    int nblockNameNum = 0;
	    
	    //If there is no index in outerBlockNames, default the first block.
	    if(!outerBlockNames.contains(":")){
	    	outerBlockNames += ":0";
	    }
	    
	    CheckOuterBlockNames(outerBlockNames);
	    
	    if(null == element.toString()){
	    	return;
	    }

	    try {
			GetRemoteConf(confPathWithName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//ReadConf ok
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
//				System.out.println("nblockNameNum ："+nblockNameNum);
			}
		}else{
			String[] lineArray=outerBlockNames.split(":");
			blname = lineArray[0];
			nindex = Integer.parseInt(lineArray[1]);
			nLineCount = GetPreBlockLength(blname,nblockNameNum,nindex);
			nblockNameNum += nLineCount;
		}
		
		String BlockText = GetBlockTextWithIndex(blname,nblockNameNum);
		int BlockLength = GetBlockLenth(BlockText);

		String newConfText = GetPreBlockText(nblockNameNum)+BlockText+element.toString()
				+GetSufBlockText(nblockNameNum+BlockLength);

		// write to conf file
		WriteConf(newConfText);
		try {
			WriteRemoteConf();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void delete(Element element, String outerBlockNames)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAfter(Element element, Element after,
			String outerBlockNames) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replace(Element oldElement, Element newElement,
			String outerBlockNames) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Block> getBlocks(String blockName, String outerBlockNames)
			throws RemoteException {
		// TODO Auto-generated method stub
		Block objblock = new RecBlock();
		objblock.setName(outerBlockNames);
		objblock.getBlocks();
		
		return null;
	}

	private void CheckOuterBlockNames(String outerBlockNames) {
		// TODO Auto-generated method stub
		// 正则？outerBlockNames = "http:0|server:0|location /:0";
	}


//	private int GetPreBlockLength(String blname, int nblockStartLine) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	private int GetPreBlockLength(String blname,int nStartLine,int nbindex) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
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
	 * */
	public String ReadConf(){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void WriteConf(String wcConftext){
		try {
	    	FileWriter fw = null;
	    	fw = new FileWriter(confPathWithName);

		    fw.write(wcConftext);
			fw.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String GetPreBlockText(int Index){
		String SufBlockText = null;
	    String linetxt =null;
	    int nPreCount = 0;
	    boolean bStartBlock = false;
	    
	    BufferedReader br = new BufferedReader(new StringReader(confText));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SufBlockText;
	}
	
	private String GetSufBlockText(int Index){
		String SufBlockText = null;
	    String linetxt =null;
	    int nPreCount = 0;
	    boolean bStartBlock = false;
	    
	    BufferedReader br = new BufferedReader(new StringReader(confText));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
