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
	 * @author LiuQin The implement class of AuthInfo
	 * @see AuthInfo
	 */
public class RecController implements Controller {

	public RecAuthInfo reauthInfo;
	String midwarePath;
	String serverName;
	String confFile;
	public static int flag=0;

	/** Construct a RecController with specified properties */
	public RecController(RecAuthInfo reauthInfo, String midwarePath) {
		this.reauthInfo = reauthInfo;
		this.midwarePath = midwarePath;
		this.confFile = "nginx.conf";
		this.serverName = "nginx";
	}
	
	/** Construct a RecController with specified properties */
	public RecController(RecAuthInfo reauthInfo, String midwarePath,
			String serverName, String confFile) {
		this.reauthInfo = reauthInfo;
		this.midwarePath = midwarePath;
		this.confFile = confFile;
		this.serverName = serverName;
	}

	@Override
	public void start() throws RemoteException {
		// TODO Auto-generated method stub
		// determine the configure file is existed or not
		
		if (isExistedFile(midwarePath + "conf", confFile) == false) {
			flag=1;
			throw new RemoteException("There is no " + confFile);
		}
		// determine the server is running or not
		if (isRunning() == true) {
			flag=2;
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
				+ "conf/nginx.conf" ;//+ " && cat " + midwarePath+ "logs/nginx.pid"
		//String cmd = midwarePath + "sbin/nginx -v " ;//+ " && cat " + midwarePath+ "logs/nginx.pid"
				
		System.out.println(cmd);
		this.reauthInfo.execCommand(cmd, result, errorResult);
		
		if (result.isEmpty()) {
			if (!errorResult.isEmpty())
			{
				flag=3;
				throw new RemoteException(errorResult.toString());
			}
			else
			{
				flag=4;
				System.out.println("The Nginx start successfully");
			}
		} 
		else
		{
			flag=5;
			System.out.println(result.toString());
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
		this.reauthInfo.execCommand(cmd, result, errorResult);
		if (isRunning() == false) {
			System.out.println("The " + serverName
					+ " is shut down successfully. ");
		} else {
			result.clear();
			errorResult.clear();
			// shut down the nginx forcefully
			this.reauthInfo.execCommand("killall nginx", result, errorResult);

			if (isRunning() == false) {
				System.out.println("The " + serverName
						+ " is shut down forcefully. ");
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
		}
		else
		{
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
		}
		else
		{

			ArrayList<String> result = new ArrayList<String>(0);
			ArrayList<String> errorResult = new ArrayList<String>(0);
			String cmd = midwarePath + "sbin/nginx -s reload";
	
			// reload the nginx
			this.reauthInfo.execCommand(cmd, result, errorResult);
			if (result.isEmpty()) {
				if (!errorResult.isEmpty()) {
					throw new RemoteException(errorResult.toString());
				} else
					System.out.println("Reload sucessfully.");
			}
		}
	}

	@Override
	public void deploy(File zipFile, String targetPath) throws IOException,
			RemoteException {
		// TODO Auto-generated method stub

		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);

		// copy the local file zipFile to targetPath of remote host
		if (this.scopy(zipFile, targetPath) == true) {
			if (reDirName(
					targetPath,
					zipFile.getName().substring(0,
							zipFile.getName().toString().indexOf(".zip")),
					"_bak") == true) {
				// unzip the zipFile in the remote host
				this.reauthInfo.execCommand(
						"unzip -q " + targetPath + zipFile.getName(), result,
						errorResult);
				if (result.isEmpty() && errorResult.isEmpty()) {
					System.out.println("Unzip successfully!");
				} else {
					throw new RemoteException(errorResult.toString());
				}

				if (deleteFile(targetPath, zipFile.getName()) == false) {
					throw new RemoteException("zipFile deleted failed.");
				} else
					System.out.println("zipFile deleted successfully!");
			} else
				throw new RemoteException("Rename failed");
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
	public boolean scopy(File zipFile, String targetPath) throws IOException,
			RemoteException {
		String zipFileName = zipFile.toString();

		Connection conn = new Connection(this.reauthInfo.getHostname());
		/* Now connect */
		conn.connect();

		/*
		 * Authenticate. If you get an IOException saying something like
		 * "Authentication method password not supported by the server at this stage."
		 * then please check the FAQ.
		 */
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.reauthInfo.getUsername(), this.reauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");

		SCPClient scpc = conn.createSCPClient();
		scpc.put(zipFileName, targetPath);
		if (isExistedFile(targetPath, zipFile.getName()) == false)
			throw new RemoteException("File scp failed");

		return true;
	}

	/**
	 * Determine the server is running or not
	 * 
	 * @return The server is running return true;or else return false
	 * */
	
	public boolean isRunning() {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);

		this.reauthInfo.execCommand("pgrep -u " + this.reauthInfo.getUsername()
				+ " " + serverName, result, errorResult);
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
	public boolean reDirName(String targetPath, String DirName, String subfix)
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
		System.out.println(cmd);
		this.reauthInfo.execCommand(cmd, result, errorResult);
		System.out.println(cmd);
		if (!errorResult.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Determined the specified file is existed or not
	 * 
	 * @return The file exists return true;or else return false
	 * */
	public Boolean isExistedFile(String targetPath, String fileName) {
		// result:Used to store the result information of the command execution
		ArrayList<String> result = new ArrayList<String>(0);
		// errorResult:Used to store the error information of the command
		// execution
		ArrayList<String> errorResult = new ArrayList<String>(0);

		// To determine whether the configuration file exists
		String cmd = "ls " + targetPath + " | grep " + fileName;
		System.out.println(cmd);
		this.reauthInfo.execCommand(cmd, result, errorResult);
		if (result.isEmpty())
			return false;
		else
			return true;
	}

	/**
	 * Determined the specified directory is existed or not
	 * 
	 * @return The directory exists return true;or else return false
	 * */
	public boolean isExistedDirectory(String targetPath, String DirName) {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		String cmd = "cd " + targetPath + " && ls -l | grep ^d | grep "
				+ DirName;
		System.out.println(cmd);
		this.reauthInfo.execCommand(cmd, result, errorResult);
		System.out.println(result.toString());
		System.out.println(errorResult.toString());
		if ((!result.isEmpty()) && errorResult.isEmpty()) {
			System.out.println(result.toString());
			return true;
		} else
			return false;
	}

	/**
	 * Delete the specified directory
	 * 
	 * @return Delete successfully return true;or else return false
	 * */
	public boolean deleteDirectory(String targetPath, String dirName) {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		String cmd = "cd " + targetPath + " && rm -rf " + dirName;
		System.out.println(cmd);
		this.reauthInfo.execCommand(cmd, result, errorResult);
		if (result.isEmpty() && errorResult.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * Delete the specified file
	 * 
	 * @return Delete successfully return true;or else return false
	 * */
	public boolean deleteFile(String targetPath, String fileName) {
		ArrayList<String> result = new ArrayList<String>(0);
		ArrayList<String> errorResult = new ArrayList<String>(0);
		this.reauthInfo.execCommand("cd " + targetPath + " && rm -f "
				+ fileName, result, errorResult);
		if (isExistedFile(targetPath, fileName) == false) {
			return true;
		} else
			return false;
	}
}
