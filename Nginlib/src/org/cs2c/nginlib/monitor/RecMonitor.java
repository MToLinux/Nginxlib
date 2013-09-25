package org.cs2c.nginlib.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

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
	Connection conn;
	Session sess;
	
	/* Constructor */
	public RecMonitor()
	{
		this.hostname = "127.0.0.1";
		this.username = "root";
		this.password = "qwer1234";
		this.port = 22;
	}
	public RecMonitor(String hostname)
	{
		this.hostname = hostname;
		this.username = "root";
		this.password = "qwer1234";
		this.port = 22;
	}
	public RecMonitor(String hostname, String username, String password)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = 22;
	}
	public RecMonitor(String hostname, String username, String password, int port)
	{
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = port;
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
	
	public boolean establishConnection()
	{
		try
		{
			/* Create a connection instance */
			conn = new Connection(hostname, port);
			
			/* Now connect */
			conn.connect();

			/* Authenticate. */
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		return true;
	}
	
	public boolean establishSession()
	{
		try
		{
			/* Create a session */
			sess = conn.openSession();
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		return true;
	}
	
	public void closeSession()
	{
		/* Close this session */
		sess.close();
	}
	
	public void closeConnection()
	{
		/* Close the connection */
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
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					break;
				}
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
			
			return cpusta;
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		
		return null;
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
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					break;
				}
				String line = null;
				if(linein.length() >= 1)
				{
					line = deleteExtraSpace(linein);
				}
				if(line.length()<=1 || line.charAt(0) == 'p' || line.charAt(0) == 'r')
				{
					continue;
				}
				//System.out.println(line);
				String linesplit[] = line.split(" ");
				BlockInPerSec = Float.parseFloat(linesplit[8]);
				BlockOutPerSec = Float.parseFloat(linesplit[9]);
			}
			stdout.close();
			br.close();
			
			closeSession();
			establishSession();

			/* Devices */
			/* Execute a command */
			sess.execCommand("iostat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			/* Skip the first serveral lines */
			while (true)
			{
				String linein = br.readLine();
				if(linein == null)
				{
					break;
				}
				
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
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					break;
				}
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
			
			return iosta;
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		
		return null;
	}
	
	public static boolean isNumeric(String str)
	{
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
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
			
			/*
			 * 
[root@server1 ~]# ifstat
bash: ifstat: command not found
[root@server1 ~]# 
			 */

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			int linenum = 0;
			int eth_num = 0;
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
			
			return nwsta;
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		
		return null;
	}

	@Override
	public MemoryStatus getMemoryStatus() throws RemoteException {
		
		try
		{
			/* Connect to the remote host and establish a Session */
			establishConnection();
			establishSession();
			
			int UsedSwap = 0;//free 2
			int SwapIn = 0;//vmstat 6
			int SwapOut = 0;//vmstat 7
			int Free = 0;//free 3
			int Buffers = 0;//free 5
			int Shared = 0;//free 4
			int Cached = 0;//free 6
			int Used = 0;//free 2
			InputStream stdout;
			BufferedReader br;
			
			/* Execute a command */
			sess.execCommand("vmstat");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					break;
				}
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
			establishSession();

			/* Execute a command */
			sess.execCommand("free");

			stdout = new StreamGobbler(sess.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			while (true)
			{
				String linein = br.readLine();
				if (linein == null)
				{
					break;
				}
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
			
			return memsta;
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
			System.exit(2);
		}
		
		return null;
	}

	@Override
	public MiddlewareStatus getMiddlewareStatus(int flag) throws RemoteException {
		try{
			RecMiddlewareStatus mwstat = new RecMiddlewareStatus();
			if(flag == 0)//nginx
			{
				//RecNginxStatus nginxstat = new RecNginxStatus();
				//nginxstat = 
				mwstat.getNginxStatusValue(this);
				
				/* RecNginxStatus into MiddlewareStatus */
//				mwstat.setNginxStatus(nginxstat);
			}
			else if(flag == 1)
			{
				;
			}
			else
			{
				;
			}
			return mwstat;
		}
		catch(RemoteException e)
		{
		}
		return null;
	}
}
