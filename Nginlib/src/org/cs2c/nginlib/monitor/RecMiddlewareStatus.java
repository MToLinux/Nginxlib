package org.cs2c.nginlib.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.cs2c.nginlib.RemoteException;

import com.trilead.ssh2.StreamGobbler;

public class RecMiddlewareStatus implements MiddlewareStatus {
	
	private String NginxStatusPath = "status";
	private String NginxUsername = "user";
	private String NginxPasswd = "qwer1234";
	private RecNginxStatus NginxStatus = null;
	
	public void setNginxStatusPath(String NginxStatusPath) {
		this.NginxStatusPath = NginxStatusPath;
	}
	public String getNginxStatusPath() {
		return NginxStatusPath;
	}
	
	public void setNginxUsername(String NginxUsername) {
		this.NginxUsername = NginxUsername;
	}
	public String getNginxUsername() {
		return NginxUsername;
	}
	
	public void setNginxPasswd(String NginxPasswd) {
		this.NginxPasswd = NginxPasswd;
	}
	public String getNginxPasswd() {
		return NginxPasswd;
	}
	
	public void setNginxStatus(RecNginxStatus NginxStatus) {
		this.NginxStatus = NginxStatus;
	}
	public RecNginxStatus getNginxStatus() {
		return NginxStatus;
	}
	
	public RecNginxStatus getNginxStatusValue(RecMonitor monitor) throws RemoteException
	{
		try
		{
			/* Connect to the remote host and establish a Session */
			monitor.establishConnection();
			monitor.establishSession();
			
			int ActiveConnections = 0;
			int ServerAccepts = 0;
			int ServerHandled = 0;
			int ServerRequests = 0;
			int NginxReading = 0;
			int NginxWriting = 0;
			int KeepAliveConnections = 0;
			InputStream stdout = null;
			BufferedReader br = null;
			
			/* Execute a command */
			monitor.getSession().execCommand(
					"GET http://" + monitor.getHostname() +  "/" +
					NginxStatusPath + 
					"-C" +
					NginxUsername + ":" +
					NginxPasswd);
			
			stdout = new StreamGobbler(monitor.getSession().getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			int linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				String line = null;
				if (linein == null)
				{
					break;
				}
				//System.out.println(linein);
				linenum++;
				if(linenum == 1)
				{
					if(linein.length()>=1)
					{
						line = monitor.deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						continue;
					}
					String linesplit[] = line.split(":");
					ActiveConnections = Integer.parseInt(monitor.deleteExtraSpace(linesplit[linesplit.length-1]));
				}
				else if(linenum==3)
				{
					if(linein.length()>=1)
					{
						line = monitor.deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						continue;
					}
					String linesplit[] = line.split(" ");

					ServerAccepts = Integer.parseInt(linesplit[0]);
					ServerHandled = Integer.parseInt(linesplit[1]);
					ServerRequests = Integer.parseInt(linesplit[2]);
				}
				else if(linenum==4)
				{
					if(linein.length()>=1)
					{
						linein = linein.replace(":", " ");
						line = monitor.deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						continue;
					}
					String linesplit[] = line.split(" ");
					NginxReading = Integer.parseInt(monitor.deleteExtraSpace(linesplit[1]));
					NginxWriting = Integer.parseInt(monitor.deleteExtraSpace(linesplit[3]));
					KeepAliveConnections = Integer.parseInt(monitor.deleteExtraSpace(linesplit[5]));
				}
			}
			stdout.close();
			br.close();

			/* RecNginxStatus */
			RecNginxStatus ngsta = new RecNginxStatus();
			ngsta.setActiveConnections(ActiveConnections);
			ngsta.setKeepAliveConnections(KeepAliveConnections);
			ngsta.setNginxReading(NginxReading);
			ngsta.setNginxWriting(NginxWriting);
			ngsta.setServerAccepts(ServerAccepts);
			ngsta.setServerHandled(ServerHandled);
			ngsta.setServerRequests(ServerRequests);

			/* Close the session and disconnect to the remote host */
			monitor.closeSession();
			monitor.closeConnection();
			
			this.NginxStatus = ngsta;
			return ngsta;
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		
		return null;
	}
//	@Override
//	public RecNginxStatus getNginxStatus(RecMonitor monitor)
//			throws RemoteException {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
