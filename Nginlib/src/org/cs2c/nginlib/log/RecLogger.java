/**
 * 
 */
package org.cs2c.nginlib.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.cs2c.nginlib.RemoteException;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

/**
 * @author Administrator
 *
 */
public class RecLogger implements Logger {
	private Connection conn;
	private String home;
	/**
	 * 
	 */
	public RecLogger(Connection conn, String home) {
		this.conn=conn;
		this.home=home;
	}

	/* (non-Javadoc)
	 * @see org.cs2c.nginlib.log.Logger#getLogFileNames()
	 */
	@Override
	public List<LogProfile> getLogFileNames() throws RemoteException {
		List<LogProfile> logs=new LinkedList<LogProfile>();
		try {
			Session session=this.conn.openSession();
			String cmd="ls -l "+this.home+"/logs";
			session.execCommand(cmd);
			InputStream stdout=new StreamGobbler(session.getStdout());
			BufferedReader br=new BufferedReader(new InputStreamReader(stdout));
			String line=br.readLine();
			while(true){
				line=br.readLine();
				if(line==null){
					break;
				}
				LogProfile log=new LogProfile();
				String[] attrs=line.split(" ");
				log.setName(attrs[7]);
				log.setOwner(attrs[2]);
				log.setGroup(attrs[3]);
				log.setSize(Long.parseLong(attrs[4]));
				logs.add(log);
			}
			br.close();
			session.close();
		} catch (IOException e) {
			throw new RemoteException(e);
		}
		return logs;
	}

	/* (non-Javadoc)
	 * @see org.cs2c.nginlib.log.Logger#getLogContent(java.lang.String)
	 */
	@Override
	public String getLogContent(String filename) throws RemoteException {
		StringBuilder sb=new StringBuilder();
		try {
			Session session=this.conn.openSession();
			session.execCommand("cat "+this.home+"/logs/"+filename);
			InputStream stdout=new StreamGobbler(session.getStdout());
			BufferedReader br=new BufferedReader(new InputStreamReader(stdout));
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			br.close();
			session.close();
		} catch (IOException e) {
			throw new RemoteException(e);
		}
		return sb.toString();
	}

	@Override
	public void delete(String filename) throws RemoteException {
		try {
			Session session=this.conn.openSession();
			session.execCommand("rm -f "+this.home+"/logs/"+filename);
			session.close();
		} catch (IOException e) {
			throw new RemoteException(e);
		}
	}

	@Override
	public void truncate(String filename) throws RemoteException {
		try {
			Session session=this.conn.openSession();
			session.execCommand("> "+this.home+"/logs/"+filename);
			session.close();
		} catch (IOException e) {
			throw new RemoteException(e);
		}
	}

}
