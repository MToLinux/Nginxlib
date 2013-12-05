package org.cs2c.nginlib.ctl;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.RecAuthInfo;

/**
 * @author LiuQin 
 *         The implement class of AuthInfo
 * @see AuthInfo
 */
public class RecController implements Controller {

	RecAuthInfo reauthInfo = null;
	String midwarePath = "";
	String serverName = "";
	String confFile = "";
	Connection conncontroller = null;

	/** Construct a RecController with specified properties */
	public RecController(RecAuthInfo reauthInfo, String midwarePath,
			Connection conn) {
		this.reauthInfo = reauthInfo;
		this.midwarePath = pathStrConvert(midwarePath);
		this.confFile = "nginx.conf";
		this.serverName = "nginx";
		this.conncontroller = conn;
	}

	/** Construct a RecController with specified properties */
	public RecController(RecAuthInfo reauthInfo, String midwarePath,
			String serverName, String confFile, Connection conn) {
		this.reauthInfo = reauthInfo;
		this.midwarePath = pathStrConvert(midwarePath);
		this.confFile = confFile;
		this.serverName = serverName;
		this.conncontroller = conn;
	}

	@Override
	public void start() throws RemoteException {
		// TODO Auto-generated method stub
		// determine the configure file is existed or not

		if (isExistedFile(midwarePath + "conf", confFile) == false) {

			throw new RemoteException("There is no " + confFile);
		}
		// determine the server is running or not
		if (isRunning() == true) {

			throw new RemoteException("The " + serverName
					+ " is running already. ");
		}

		// result:Used to store the result information of the command execution
		ArrayList<String> result = new ArrayList<String>(0);
		// errorResult:Used to store the error information of the command
		// execution
		ArrayList<String> errorResult = new ArrayList<String>(0);

		// start the nginx
		String cmd = midwarePath + "sbin/nginx -c " + midwarePath
				+ "conf/nginx.conf";// + " && cat " + midwarePath+
									// "logs/nginx.pid"
		// String cmd = midwarePath + "sbin/nginx -v " ;//+ " && cat " +
		// midwarePath+ "logs/nginx.pid"

		// System.out.println(cmd);
		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);

		if (result.isEmpty()) {
			if (!errorResult.isEmpty()) {

				throw new RemoteException(errorResult.toString());
			} else {

				// System.out.println("The Nginx start successfully");
			}
		} else {

			// System.out.println(result.toString());
		}
	}

	@Override
	public void shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		// determine the server is running or not
		if (isRunning() == false) {
			throw new RemoteException("The " + serverName + " is not running. ");
		}

		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);

		// shut down the nginx
		String cmd = midwarePath + "sbin/nginx -s quit";
		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		if (isRunning() == false) {
			// System.out.println("The " + serverName
			// + " is shut down successfully. ");
		} else {
			result.clear();
			errorResult.clear();
			// shut down the nginx forcefully
			this.reauthInfo.execCommand(conncontroller, "killall nginx",
					result, errorResult);

			if (isRunning() == false) {
				// System.out.println("The " + serverName
				// + " is shut down forcefully. ");
			} else {
				throw new RemoteException(errorResult.toString());
			}
		}

	}

	@Override
	public void restart() throws RemoteException {
		// TODO Auto-generated method stub
		if (isRunning() == false) {
			start();
		} else {
			shutdown();
			start();
		}
	}

	@Override
	public void reload() throws RemoteException {
		// TODO Auto-generated method stub
		// determine the configure file is existed or not
		if (isExistedFile(midwarePath + "conf", confFile) == false) {
			throw new RemoteException("There is no " + confFile
					+ "and can't reload");
		}
		// determine the server is running or not
		if (isRunning() == false) {
			start();
		} else {

			ArrayList<String> result = new ArrayList<String>(0);
			ArrayList<String> errorResult = new ArrayList<String>(0);
			String cmd = midwarePath + "sbin/nginx -s reload";

			// reload the nginx
			this.reauthInfo.execCommand(conncontroller, cmd, result,
					errorResult);
			if (result.isEmpty()) {
				if (!errorResult.isEmpty()) {
					throw new RemoteException(errorResult.toString());
				} else {
					// System.out.println("Reload sucessfully.");
				}
			}
		}
	}

	@Override
	public void deploy(File zipFile, String targetPath) throws IOException,
			RemoteException {
		// TODO Auto-generated method stub
		targetPath = pathStrConvert(targetPath);
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		
		// copy the local file zipFile to targetPath of remote host
		if (this.scopy(conncontroller, zipFile, targetPath) == true) {
			//System.out.println("222---"+targetPath+zipFile.getName().substring(0,zipFile.getName().toString().indexOf(".zip")));
		boolean unzipflag=isUnzipAddDirectory(targetPath,zipFile.getName());
		if (isExistedDirectory(
					targetPath,
					zipFile.getName().substring(0,
							zipFile.getName().toString().indexOf(".zip")))) {

				if (reDirName(
						targetPath,
						zipFile.getName().substring(0,
								zipFile.getName().toString().indexOf(".zip")),
						"_bak") == false) {
					
					throw new RemoteException(errorResult.toString());
				}
			}
			//System.out.println(zipFile.getName());
			
			result.clear();
			errorResult.clear();
			String cmd;
			if(unzipflag==false)
			{
				cmd="cd "+targetPath+" && unzip -q "+ zipFile.getName();
			}
			else
			{
				cmd="cd "+targetPath+" && unzip -q -d "+ zipFile.getName().substring(0,
						zipFile.getName().toString().indexOf(".zip"))+" " + zipFile.getName();
			}
			//System.out.println("333----"+cmd);
			this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);

			if (result.isEmpty() && errorResult.isEmpty()) {
				// System.out.println("Unzip successfully!");
				if (deleteFile(targetPath, zipFile.getName()) == false) {
					throw new RemoteException("zipFile deleted failed.");
				} else {
					// System.out.println("zipFile deleted successfully!");
				}
			} else
				throw new RemoteException("unzip failed");
		}

	}

	/**
	 * Copy the local file named zipFile to remote host and the the target
	 * location is targetPath.
	 * 
	 * @param zipFile
	 *            local zipFile
	 * @param targetPath
	 *            target location in the remote host
	 * @throws RemoteException
	 *             When this remote operation fails for any non-local reason.
	 * @throws IOException
	 *             When connect remote host by ssh2
	 * */
	public boolean scopy(Connection conn, File zipFile, String targetPath)
			throws IOException, RemoteException {
		String zipFileName = zipFile.toString();
		targetPath = pathStrConvert(targetPath);

		try
		{
			SCPClient scpc = conn.createSCPClient();
			mkdirTargetPath(targetPath);
			scpc.put(zipFileName, targetPath);
		
		}catch(IllegalStateException e)
		{
			throw new RemoteException(e.getMessage());
		}
		//System.out.println(zipFileName+"   "+targetPath);
		//mkdir targetPath
		
		if (isExistedFile(targetPath, zipFile.getName()) == false)
			throw new RemoteException("File scp failed");

		return true;
	}

	/**
	 * Determine the server is running or not
	 * 
	 * @return The server is running return true;or else return false
	 * @throws RemoteException 
	 * */

	boolean isRunning() throws RemoteException {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);

		this.reauthInfo.execCommand(conncontroller, "pgrep -u "
				+ this.reauthInfo.getUsername() + " " + serverName, result,
				errorResult);
		if (result.isEmpty())
			return false;
		else
			return true;
	}

	/**
	 * Rename a specified directory existed, and the new name is added a
	 * specified subfix
	 * 
	 * @return Rename successfully return true;or else return false
	 * */
	boolean reDirName(String targetPath, String DirName, String subfix)
			throws RemoteException {

		if (isExistedDirectory(targetPath, DirName + subfix) == true) {
			if (deleteDirectory(targetPath, DirName + subfix) == false) {
				throw new RemoteException(subfix + " directory can't deleted.");
			}
		}
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		String cmd = "cd " + targetPath + " && mv " + DirName + " " + DirName
				+ subfix;
		//System.out.println(cmd);
		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		
		if (!errorResult.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Determined the specified file is existed or not
	 * 
	 * @return The file exists return true;or else return false
	 * @throws RemoteException 
	 * */
	boolean isExistedFile(String targetPath, String fileName) throws RemoteException {
		// result:Used to store the result information of the command execution
		ArrayList<String> result = new ArrayList<String>(0);
		// errorResult:Used to store the error information of the command
		// execution
		ArrayList<String> errorResult = new ArrayList<String>(0);

	
		String cmd = "cd " + targetPath + " && find -name " + "\""+fileName+"\"";
		//String cmd = "ls " + targetPath + " | grep " + fileName;
		// System.out.println(cmd);
		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		if (result.isEmpty())
			return false;
		else
			return true;
	}

	/**
	 * Determined the specified directory is existed or not
	 * 
	 * @return The directory exists return true;or else return false
	 * @throws RemoteException 
	 * */
	boolean isExistedDirectory(String targetPath, String DirName) throws RemoteException {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		//String cmd = "cd " + targetPath + " && ls -l | grep ^d | grep "
		//		+ DirName;
		String cmd = "cd " + targetPath + " && find -name "
						+"\"" +DirName+"\"";

		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		
		if ((!result.isEmpty()) && errorResult.isEmpty()) {
			return true;
		} else
			return false;
	}

	/**
	 * Delete the specified directory
	 * 
	 * @return Delete successfully return true;or else return false
	 * @throws RemoteException 
	 * */
	boolean deleteDirectory(String targetPath, String dirName) throws RemoteException {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		String cmd = "cd " + targetPath + " && rm -rf " + dirName;

		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		if (result.isEmpty() && errorResult.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * Delete the specified file
	 * 
	 * @return Delete successfully return true;or else return false
	 * @throws RemoteException 
	 * */
	boolean deleteFile(String targetPath, String fileName) throws RemoteException {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		this.reauthInfo.execCommand(conncontroller, "cd " + targetPath
				+ " && rm -f " + fileName, result, errorResult);
		if (isExistedFile(targetPath, fileName) == false) {
			return true;
		} else
			return false;
	}

	String pathStrConvert(String pathstr) {
		String pathstrend = pathstr;
		if (pathstr.charAt(pathstr.length() - 1) != '/') {
			pathstrend = pathstr + "/";
		}
		
		return pathstrend;
	}
	
	
	boolean isUnzipAddDirectory(String targetPath, String fileName) throws RemoteException {
		// result:Used to store the result information of the command execution
		ArrayList<String> result = new ArrayList<String>(0);
		// errorResult:Used to store the error information of the command
		// execution
		ArrayList<String> errorResult = new ArrayList<String>(0);

		String cmd = "cd " + targetPath + " && zipinfo -1 " +fileName;
	
		this.reauthInfo.execCommand(conncontroller, cmd, result, errorResult);
		if (result.isEmpty())
			return false;
		else 
		{
			for(int i=0;i<result.size();i++)
			{
				if(result.get(i).contains("/"))
				{
					if(!result.get(i).startsWith(fileName.substring(0,fileName.indexOf(".zip"))))
					{
						return true;
					}
				}
			}
			return false;
		}
	}
	
	
	boolean mkdirTargetPath(String targetPath) throws RemoteException {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);

		this.reauthInfo.execCommand(conncontroller, "mkdir -p "+targetPath, result,
				errorResult);
		if (result.isEmpty() && errorResult.isEmpty())
			return true;
		else
			return false;
	}
}
