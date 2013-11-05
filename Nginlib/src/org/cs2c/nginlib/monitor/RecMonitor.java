package org.cs2c.nginlib.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.monitor.RecCPUStatus;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class RecMonitor implements Monitor {
	
	private String hostname = "127.0.0.1";
	private String username = "root";
	private String password = "qwer1234";
	private int port = 22;
	private Connection conn = null;
	private Session sess = null;
	private String nginxpath = "/usr/local/nginx/";
	
	/* Constructor */
	public RecMonitor()
	{
		this.hostname = "127.0.0.1";
		this.username = "root";
		this.password = "qwer1234";
		this.port = 22;
		this.nginxpath = "/usr/local/nginx/";
	}
	public RecMonitor(String hostname)
	{
		this.hostname = hostname;
		this.username = "root";
		this.password = "qwer1234";
		this.port = 22;
		this.nginxpath = "/usr/local/nginx/";
	}
	public RecMonitor(String hostname, String username, String password)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = 22;
		this.nginxpath = "/usr/local/nginx/";
	}
	public RecMonitor(String hostname, String username, String password, int port)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = port;
		this.nginxpath = "/usr/local/nginx/";
	}
	public RecMonitor(String hostname, String username, String password, String nginxpath)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = 22;
		this.nginxpath = nginxpath;
	}
	public RecMonitor(String hostname, String username, String password, int port, String nginxpath)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = port;
		this.nginxpath = nginxpath;
	}
	public RecMonitor(RecAuthInfo ainfo, String nginxpath)
	{
		this.hostname = ainfo.getHostname();
		this.username = ainfo.getUsername();
		this.password = ainfo.getPassword();
		this.port = 22;
		this.nginxpath = nginxpath;
	}
	
	public String getHostname()
	{
		return hostname;
	}
	public String getUsername()
	{
		return username;
	}
	public String getPassword()
	{
		return password;
	}
	
	public void establishConnection() throws IOException
	{
		/* Create a connection instance */
		conn = new Connection(hostname, port);
		
		/* Connect */
		conn.connect();

		/* Authenticate. */
		boolean isAuthenticated = 
		conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
	}
	
	public void establishSession() throws IOException
	{
		sess = conn.openSession();
	}
	
	public void closeSession()
	{
		sess.close();
	}
	
	public void closeConnection()
	{
		conn.close();
	}

	public Connection getConnection()
	{
		return conn;
	}
	
	public Session getSession()
	{
		return sess;
	}
	
	public String deleteExtraSpace(String str)
	{  
        if(str==null)
        {  
            return null;  
        }  
        if(str.length()==0 || str.equals(" "))
        {  
            return new String();  
        }  
        char[] oldStr=str.toCharArray();  
        int len=str.length();  
        char[] tmpStr=new char[len];  
        boolean keepSpace=false;  
        int j=0;//the index of new string  
        for(int i=0;i<len;i++)
        {  
            char tmpChar=oldStr[i];  
            if(oldStr[i]!=' ')
            {  
                tmpStr[j++]=tmpChar;  
                keepSpace=true;  
            }
            else if(keepSpace)
            {  
                tmpStr[j++]=tmpChar;  
                keepSpace=false;  
            }  
        }  
          
        //unlike c/c++,no "\0" at the end of a string. So,do the copy again...  
        int newLen=j;  
        if(tmpStr[j-1]==' ')
        {  
            newLen--;  
        }  
        char[] newStr=new char[newLen];  
        for(int i=0;i<newLen;i++)
        {  
            newStr[i]=tmpStr[i];  
        }  
        return new String(newStr);
	}

	@Override
	public CPUStatus getCPUStatus() throws RemoteException {
		try
		{
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			int RunningNum = 0;
			int BlockingNum = 0;
			int InterruptNum = 0;
			int ContextSwitchNum = 0;
			float UserPercent = 0;
			float SystemPercent = 0;
			float IdlePercent = 0;
			float IOWaitPercent = 0;
			InputStream stdout;
			BufferedReader br;
			
			int linenum = 0;
			boolean cmdflag = true;
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				String line = null;
				if(linein.length() >= 1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line.length()<=1 || line.charAt(0) == 'p' || line.charAt(0) == 'r')
				{
					continue;
				}
				String linesplit[] = line.split(" ");
				RunningNum = Integer.parseInt(linesplit[0]);
				BlockingNum = Integer.parseInt(linesplit[1]);
				InterruptNum = Integer.parseInt(linesplit[10]);
				ContextSwitchNum = Integer.parseInt(linesplit[11]);
				UserPercent = Float.parseFloat(linesplit[12]);
				SystemPercent = Float.parseFloat(linesplit[13]);
				IdlePercent = Float.parseFloat(linesplit[14]);
				IOWaitPercent = Float.parseFloat(linesplit[15]);
			}
			stdout.close();
			br.close();

			/* RecCPUStatus */
			RecCPUStatus cpusta = new RecCPUStatus();
			cpusta.setBlockingNum(BlockingNum);
			cpusta.setContextSwitchNum(ContextSwitchNum);
			cpusta.setIdlePercent(IdlePercent);
			cpusta.setInterruptNum(InterruptNum);
			cpusta.setIOWaitPercent(IOWaitPercent);
			cpusta.setRunningNum(RunningNum);
			cpusta.setSystemPercent(SystemPercent);
			cpusta.setUserPercent(UserPercent);
								
			/* Close the session and disconnect to the remote host */
			closeSession();
			closeConnection();
			
			if(cmdflag == false)
			{
				throw new IOException("Command vmstat Execution failed.");
			}
			
			return cpusta;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public IOStatus getIOStatus() throws RemoteException {
		try
		{
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			float BlockInPerSec = 0;
			float BlockOutPerSec = 0;
			List<Device> Devices = new LinkedList<Device>();
			InputStream stdout;
			BufferedReader br;
			
			int linenum = 0;
			boolean cmdflag = true;
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				String line = null;
				if(linein.length() >= 1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line.length()<=1 || line.charAt(0) == 'p' || line.charAt(0) == 'r')
				{
					continue;
				}

				String linesplit[] = line.split(" ");
				BlockInPerSec = Float.parseFloat(linesplit[8]);
				BlockOutPerSec = Float.parseFloat(linesplit[9]);
			}
			stdout.close();
			br.close();
			
			closeSession();
			
			if(cmdflag == false)
			{
				throw new IOException("Command vmstat Execution failed.");
			}
			
			establishSession();

			/* Devices */
			/* Execute a command */
			sess.execCommand("iostat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			/* Skip the first serveral lines */
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if(linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				
				if (linein.length() >= 7)
				{
					String substr = linein.substring(0, 7);
					if(substr.equals("Device:"))
					{
						break;
					}
					else
					{
						continue;
					}
				}
			}
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if(linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				String line = null;
				if(linein.length()>=1)
				{
					line = deleteExtraSpace(linein);
				}
				if (line == null)
				{
					break;
				}
				if(line.length()<11)
				{
					continue;
				}
				String linesplit[] = line.split(" ");
				String Name = linesplit[0];
				float BlockWritenPerSec = Float.parseFloat(linesplit[3]);
				float BlockReadPerSec = Float.parseFloat(linesplit[2]);
				long BlockWriten = Long.parseLong(linesplit[5]);
				long BlockRead = Long.parseLong(linesplit[4]);
				float TPS = Float.parseFloat(linesplit[1]);
				
				/* RecDevice */
				RecDevice device = new RecDevice();
				device.setBlockRead(BlockRead);
				device.setBlockReadPerSec(BlockReadPerSec);
				device.setBlockWriten(BlockWriten);
				device.setBlockWritenPerSec(BlockWritenPerSec);
				device.setName(Name);
				device.setTPS(TPS);
				
				Devices.add(device);
			}
			stdout.close();
			br.close();
			
			/* RecIOStatus */
			RecIOStatus iosta = new RecIOStatus();
			iosta.setBlockInPerSec(BlockInPerSec);
			iosta.setBlockOutPerSec(BlockOutPerSec);
			iosta.setDevices(Devices);
								
			/* Close the session and disconnect to the remote host */
			closeSession();
			closeConnection();
			
			if(cmdflag == false)
			{
				throw new IOException("Command iostat Execution failed.");
			}
			
			return iosta;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public NetworkStatus getNetworkStatus() throws RemoteException {
		try
		{
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			float InputKbPerSec = 0;
			float OutputPerSec = 0;
			InputStream stdout;
			BufferedReader br;
			
			/* Execute a command */
			sess.execCommand("ifstat -T");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			int linenum = 0;
			int eth_num = 0;
			boolean cmdflag = true;
			while (true)
			{
				String linein = br.readLine();
				String line = null;
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				if(linenum == 1)
				{
					if(linein.length()>=1)
					{
						line = deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						continue;
					}
					String linesplit[] = line.split(" ");
					eth_num = linesplit.length - 1;
				}
				else if(linenum>=3)
				{
					if(linein.length()>=1)
					{
						line = deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						continue;
					}
					String linesplit[] = line.split(" ");

					InputKbPerSec = Float.parseFloat(linesplit[2*eth_num]);
					OutputPerSec = Float.parseFloat(linesplit[2*eth_num + 1]);
					sess.close();
					break;
				}
			}
			stdout.close();
			br.close();

			/* RecNetworkStatus */
			RecNetworkStatus nwsta = new RecNetworkStatus();
			nwsta.setInputKbPerSec(InputKbPerSec);
			nwsta.setOutputPerSec(OutputPerSec);

			/* Close the session and disconnect to the remote host */
			closeSession();
			closeConnection();
			
			if(cmdflag == false)
			{
				throw new IOException("Command ifstat Execution failed.");
			}
			
			return nwsta;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public MemoryStatus getMemoryStatus() throws RemoteException {
		try
		{
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			int UsedSwap = 0;
			int SwapIn = 0;
			int SwapOut = 0;
			int Free = 0;
			int Buffers = 0;
			int Shared = 0;
			int Cached = 0;
			int Used = 0;
			InputStream stdout;
			BufferedReader br;
			
			int linenum = 0;
			boolean cmdflag = true;
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				String line = null;
				if(linein.length() >= 1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line.length()<=1 || line.charAt(0) == 'p' || line.charAt(0) == 'r')
				{
					continue;
				}
				String linesplit[] = line.split(" ");
				SwapIn = Integer.parseInt(linesplit[6]);
				SwapOut = Integer.parseInt(linesplit[7]);
			}
			stdout.close();
			br.close();
			
			closeSession();
			
			if(cmdflag == false)
			{
				closeConnection();
				throw new IOException("Command vmstat Execution failed.");
			}

			establishSession();

			/* Execute a command */
			sess.execCommand("free");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				String line = null;
				if(linein.length()>=1)
				{
					line = deleteExtraSpace(linein);
				}
				if (line == null)
				{
					continue;
				}
				if(line.length()<7)
				{
					continue;
				}
				String linesplit[] = line.split(" ");
				if(linesplit[0].equals("Mem:"))
				{
					Free = Integer.parseInt(linesplit[3]);
					Buffers = Integer.parseInt(linesplit[5]);
					Shared = Integer.parseInt(linesplit[4]);
					Cached = Integer.parseInt(linesplit[6]);
					Used = Integer.parseInt(linesplit[2]);
				}
				else if(linesplit[0].equals("Swap:"))
				{
					UsedSwap = Integer.parseInt(linesplit[2]);
				}
				else
				{
					continue;
				}
			}
			stdout.close();
			br.close();
			
			/* RecMemoryStatus */
			RecMemoryStatus memsta = new RecMemoryStatus();
			memsta.setBuffers(Buffers);
			memsta.setCached(Cached);
			memsta.setFree(Free);
			memsta.setShared(Shared);
			memsta.setSwapIn(SwapIn);
			memsta.setSwapOut(SwapOut);
			memsta.setUsed(Used);
			memsta.setUsedSwap(UsedSwap);
								
			/* Close the session and disconnect to the remote host */
			closeSession();
			closeConnection();
			
			if(cmdflag == false)
			{
				throw new IOException("Command free Execution failed.");
			}
			
			return memsta;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public NginxStatus getNginxStatus(/*String nginx_path, */String nginx_statuspath, String nginx_username, String nginx_password) throws RemoteException {
		RecNginxStatus ngsta = new RecNginxStatus(this.nginxpath, nginx_statuspath, nginx_username, nginx_password);
		try
		{
			String nginxStatusCommond = "GET http://" + getHostname() +  "/" + ngsta.getNginxStatusPath();
			if(nginx_username!=null && nginx_password!=null)
			{
				String struserps = " -C " + nginx_username + ":" + nginx_password;
				nginxStatusCommond += struserps;
			}
			
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			int ActiveConnections = 0;
			int ServerAccepts = 0;
			int ServerHandled = 0;
			int ServerRequests = 0;
			int NginxReading = 0;
			int NginxWriting = 0;
			int KeepAliveConnections = 0;
			List<RecProcessStatus> listNginxPS = new LinkedList<RecProcessStatus>();
			String p_USER = null;
			int p_PID = -1;
			float p_CPU = 0;
			float p_MEM = 0;
			int p_VSZ = 0;
			int p_RSS = 0;
			String p_TTY = null;
			String p_STAT = null;
			String p_START = null;
			String p_TIME = null;
			String p_COMMAND = null;
			List<String> listNginxCA = new LinkedList<String>();
			InputStream stdout = null;
			InputStream stderr = null;
			BufferedReader br = null;
			
			int linenum = 0;
			boolean nginxflag = true;
			boolean cmdflag = true;
			boolean pscmdflag = true;
			
			/* Execute a command */
			sess.execCommand(nginxStatusCommond);
			
			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				String line = null;
				if (linein == null)
				{
					if(linenum == 0)
					{
						cmdflag = false;
					}
					break;
				}
				linenum++;
				if(linenum == 1)
				{
					if(linein.length()>=1)
					{
						line = deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						linenum--;
						continue;
					}
					String linesplit[] = line.split(":");
					String actconn = deleteExtraSpace(linesplit[0]);
					if(!actconn.equals("Active connections"))
					{
						nginxflag = false;
						break;
					}
					ActiveConnections = Integer.parseInt(deleteExtraSpace(linesplit[linesplit.length-1]));
				}
				else if(linenum==3)
				{
					if(linein.length()>=1)
					{
						line = deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						linenum--;
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
						line = deleteExtraSpace(linein);
					}
					if(line == null || line.length()<3)
					{
						linenum--;
						continue;
					}
					String linesplit[] = line.split(" ");
					NginxReading = Integer.parseInt(deleteExtraSpace(linesplit[1]));
					NginxWriting = Integer.parseInt(deleteExtraSpace(linesplit[3]));
					KeepAliveConnections = Integer.parseInt(deleteExtraSpace(linesplit[5]));
				}
			}
			stdout.close();
			br.close();
			
			/* Execute a command: nginx ps cmd */
			closeSession();
			establishSession();
			sess.execCommand("ps -aux|grep nginx");
			
			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				String line = null;
				RecProcessStatus nps = new RecProcessStatus();
				
				if (linein == null)
				{
					if(linenum == 0)
					{
						pscmdflag = false;
					}
					break;
				}
				linenum++;

				//Parse
				if(linein.length()>=1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line == null || line.length()<3)
				{
					linenum--;
					continue;
				}
				String linesplit[] = line.split(" ");
				if(linesplit[10].length() < 6)
				{
					continue;
				}
				String ps_nginx_flag = (deleteExtraSpace(linesplit[10])).substring(0, 5);
				if(!(ps_nginx_flag.equals("nginx")))
				{
					continue;
				}
				else
				{
					p_USER = null;
					p_PID = 0;
					p_CPU = 0;
					p_MEM = 0;
					p_VSZ = 0;
					p_RSS = 0;
					p_TTY = null;
					p_STAT = null;
					p_START = null;
					p_TIME = null;
					p_COMMAND = null;
					
					p_USER = linesplit[0];
					p_PID = Integer.parseInt(deleteExtraSpace(linesplit[1]));
					p_CPU = Float.parseFloat(deleteExtraSpace(linesplit[2]));
					p_MEM = Float.parseFloat(deleteExtraSpace(linesplit[3]));
					p_VSZ = Integer.parseInt(deleteExtraSpace(linesplit[4]));
					p_RSS = Integer.parseInt(deleteExtraSpace(linesplit[5]));
					p_TTY = deleteExtraSpace(linesplit[6]);
					p_STAT = deleteExtraSpace(linesplit[7]);
					p_START = deleteExtraSpace(linesplit[8]);
					p_TIME = deleteExtraSpace(linesplit[9]);
					int index = 10;
					p_COMMAND = "";
					while(index < linesplit.length)
					{
						p_COMMAND += " ";
						String str = deleteExtraSpace(linesplit[index]);
						p_COMMAND += str;
						index++;
					}
					p_COMMAND = deleteExtraSpace(p_COMMAND);
					
					nps.setProcessUser(p_USER);
					nps.setProcessID(p_PID);
					nps.setProcessCPU(p_CPU);
					nps.setProcessMem(p_MEM);
					nps.setProcessVSZ(p_VSZ);
					nps.setProcessRSS(p_RSS);
					nps.setProcessTTY(p_TTY);
					nps.setProcessSTAT(p_STAT);
					nps.setProcessSTART(p_START);
					nps.setProcessTIME(p_TIME);
					nps.setProcessCmd(p_COMMAND);
					listNginxPS.add(nps);
				}
			}
			stdout.close();
			br.close();
			
			/* Execute a command: nginx -V */
			closeSession();
			establishSession();
			String ngpath = ngsta.getNginxPath(); //String ngpath = this.nginxpath;
			String ngVcm = null;
			if(ngpath.charAt(ngpath.length()-1) == '/')
			{
				ngVcm = ngpath+"sbin/nginx -V";
			}
			else
			{
				ngVcm = ngpath+"/sbin/nginx -V";
			}
			sess.execCommand(ngVcm);
			
			stderr = new StreamGobbler(sess.getStderr());
			br = new BufferedReader(new InputStreamReader(stderr));
			linenum = 0;
			while (true)
			{
				String linein = br.readLine();
				String line = null;
				
				if (linein == null)
				{
					if(linenum == 0)
					{
						nginxflag = false;
					}
					break;
				}
				linenum++;

				//Parse
				if(linein.length()>=1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line == null || line.length()<3)
				{
					linenum--;
					continue;
				}
				String linesplit[] = line.split(" ");
				if(!linesplit[0].equals("configure"))
				{
					continue;
				}
				else
				{
					int index = 2;
					while(index < linesplit.length)
					{
						listNginxCA.add(linesplit[index]);
						index++;
					}
				}
			}
			stdout.close();
			br.close();

			/* RecNginxStatus */
			ngsta.setActiveConnections(ActiveConnections);
			ngsta.setKeepAliveConnections(KeepAliveConnections);
			ngsta.setNginxReading(NginxReading);
			ngsta.setNginxWriting(NginxWriting);
			ngsta.setServerAccepts(ServerAccepts);
			ngsta.setServerHandled(ServerHandled);
			ngsta.setServerRequests(ServerRequests);
			ngsta.setNginxPSList(listNginxPS);
			ngsta.setNginxCAList(listNginxCA);
			
			/* Close the session and disconnect to the remote host */
			closeSession();
			closeConnection();
			
			if(false == nginxflag)
			{
				throw new RemoteException("Nginx status command Execution failed.");
			}
			else if(cmdflag == false)
			{
				throw new IOException("Command \"GET http://\" Execution failed.");
			}
			else if(pscmdflag == false)
			{
				throw new IOException("Command ps Execution failed.");
			}
			
			return ngsta;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}
}
