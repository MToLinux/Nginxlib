package org.cs2c.nginlib.ctl;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;

import java.io.File;
import java.io.IOException;

import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.RecAuthInfo;
public class RecController implements Controller {

	public RecAuthInfo reauthInfo;
	String midwarePath;
	public RecController(RecAuthInfo reauthInfo,String midwarePath)
	{
		this.reauthInfo=reauthInfo;
		this.midwarePath=midwarePath;
	}
	@Override
	public void start() throws RemoteException {
		// TODO Auto-generated method stub
			// && ./nginx -h /usr/local/nginx/sbin/
			//this.reauthinfo.execComond("/usr/local/nginx/sbin/nginx -s reopen");
			//this.reauthinfo.execComond("/usr/local/nginx/sbin/nginx -s reload");
			this.reauthInfo.execComond(midwarePath+"/nginx -c /usr/local/nginx/conf/nginx.conf"); 
	}

	@Override
	public void shutdown() throws RemoteException {
		// TODO Auto-generated method stub
		//this.reauthInfo.execComondreturn(midwarePath+"/nginx -s quit");
		System.out.println("the result as follow:");
		String returnstr=this.reauthInfo.execComondreturn("pidof nginx");
		System.out.println(returnstr);

	}

	@Override
	public void restart() throws RemoteException {
		// TODO Auto-generated method stub
		this.reauthInfo.execComond(midwarePath+"/nginx -s stop");
		this.reauthInfo.execComond(midwarePath+"/nginx -c /usr/local/nginx/conf/nginx.conf"); 

	}

	@Override
	public void reload() throws RemoteException {
		// TODO Auto-generated method stub
		//this.reauthinfo.execComond("/usr/local/nginx/sbin/nginx -s reopen");
		this.reauthInfo.execComond(midwarePath+"/nginx -s reload");
		//this.reauthinfo.execComond("/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf"); 

	}

	@Override
	public void deploy(File zipFile, String targetPath) throws IOException,
			RemoteException {
		// TODO Auto-generated method stub
		String middleWareName=zipFile.getName();
		String zipFileName=zipFile.toString();
		Connection conn = new Connection(this.reauthInfo.getHostname());
		/* Now connect */
		conn.connect();
		
		/* Authenticate.
		 * If you get an IOException saying something like
		 * "Authentication method password not supported by the server at this stage."
		 * then please check the FAQ.
		 */
		boolean isAuthenticated = conn.authenticateWithPassword(this.reauthInfo.getUsername(), this.reauthInfo.getPassword());
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");

		SCPClient scpc=conn.createSCPClient();
		//System.out.println(zipFileName);
		Session sess = conn.openSession();
		//sess.requestPTY()
		//sess.startShell();
		sess.execCommand("ls "+targetPath+" | grep "+middleWareName);
		
		System.out.println("Here is some information about the remote host:");

		scpc.put(zipFileName, targetPath);
		
		//conn.close();
		//this.reauthInfo.execComond("scp "+zipFile+" root@"targetPath);
		//String cmd="cd "+targetPath+" && cp "+zipFile+" root@");
		//this.reauthInfo.execComond("cd "+targetPath);
		

	}

}
