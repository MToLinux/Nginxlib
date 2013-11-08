package org.cs2c.nginlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author LiuQin The implement class of AuthInfo
 * @see AuthInfo
 */
public class RecAuthInfo implements AuthInfo {

	private String hostName = "";
	private String userName = "";
	private String passWord = "";

	private BufferedReader br = null;
	private String line = "";
	private BufferedReader errorbr = null;
	private String errorline = "";

	@Override
	public void setHost(String hostName) {
		// TODO Auto-generated method stub
		this.hostName = hostName;
	}

	/**
	 * @SuppressWarnings("deprecation")
	 * @Test public void testsetHost() { RecAuthInfo testRecAuthInfo=new
	 *       RecAuthInfo(); testRecAuthInfo.setHost("10.1.50.6");
	 *       Assert.assertEquals(testRecAuthInfo.hostName, "10.1.50.6"); }
	 */
	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		this.userName = username;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.passWord = password;
	}

	/**
	 * Get remote host name or ip address.
	 * 
	 * @return The remote host name or ip address
	 * */
	public String getHostname() {
		// TODO Auto-generated method stub
		return this.hostName;
	}

	/**
	 * Get remote host user.
	 * 
	 * @return The remote host name
	 * */
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	/**
	 * Get remote password for specific user.
	 * 
	 * @return The remote password for specific user
	 * */
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.passWord;
	}

	/**
	 * Log in the remote host by ssh2 and execute the specified command.
	 * 
	 * @param cmd
	 *            the command will be executed list the result information
	 *            printed in the terminal after executing the "cmd" if there
	 *            are. errorList the error information printed in the terminal
	 *            after executing the "cmd" if there are.
	 * 
	 * */
	public void execCommand(Connection conn, String cmd,
			ArrayList<String> list, ArrayList<String> errorList) {

		try {
			/* Create a session */
			Session sess = conn.openSession();
			sess.execCommand(cmd);

			/*
			 * get the printed information of stdout and stderr.
			 */
			InputStream stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			InputStream stderr = new StreamGobbler(sess.getStderr());
			errorbr = new BufferedReader(new InputStreamReader(stderr));

			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				list.add(line);
			}

			while (true) {
				errorline = errorbr.readLine();
				if (errorline == null)
					break;
				errorList.add(errorline);
			}

			/* Close this session */
			sess.close();

		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}
	}
}
