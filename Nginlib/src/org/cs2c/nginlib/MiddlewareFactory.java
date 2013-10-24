/**
 * 
 */
package org.cs2c.nginlib;
import org.cs2c.nginlib.config.*;
import org.cs2c.nginlib.ctl.*;
import org.cs2c.nginlib.monitor.*;
import org.cs2c.nginlib.RecMiddlewareFactory;

import java.util.*;
import java.io.*;
/**
 * @author Mikes
 * <P/>The MiddlewareFactory is the entry of the library. Each instance of this class is associated with 
 * a specific remote middleware (not remote host). Through the instance of the class, we can further 
 * get to configure, control and monitor the remote middleware.
 */
public abstract class MiddlewareFactory {

	/**
	 * Get an instance of the class based on remote middleware home path.
	 * @param authInfo Remote host access information
	 * @return the instance binded with the specific remote middleware.
	 * @throws RemoteException When authentication fails, network error or nginx does not exist.
	 * */
	static public MiddlewareFactory getInstance(AuthInfo authInfo, String middlewareHome) throws RemoteException{
		// TODO
		RecMiddlewareFactory recmiddleware=new RecMiddlewareFactory(authInfo,middlewareHome);
		return recmiddleware;
	}
	/**
	 * If remote middleware is not there, we can call this method to install it remotely.
	 * Before installing, a local setup file should be prepared.
	 * @param authInfo Authentication info in order to access remote host.
	 * @param gzFile middleware setup file in local. Format must be tar.gz.
	 * @param targetPath The path in remote host where the middleware will be installed.
	 * @param modules Which modules should be included when middleware (such as Nginx) is installed.
	 * @return the instance of the class, which is binded with the middleware installed.
	 * @throws IOException When reading local gzFile fails.
	 * @throws RemoteException When installing remote nginx fails.
	 * */
	static public MiddlewareFactory install(AuthInfo authInfo, File gzFile, String targetPath, List<Module> modules) 
		throws IOException, RemoteException{
	//static public MiddlewareFactory install(AuthInfo authInfo, File gzFile, String targetPath, String s1,String s2) 
	//		throws IOException, RemoteException{
		// TODO
		//result:Used to store the result information of the command execution
		ArrayList<String> result=new ArrayList<String>(0);	
		//errorResult:Used to store the error information of the command execution
		ArrayList<String> errorResult=new ArrayList<String>(0);
		
		/**Convert authInfo into RecAuthInfo forcefully,in order 
		 * to call the function execCommand of RecAuthInfo
		 */
		RecAuthInfo recAuthInfo=(RecAuthInfo)authInfo;
		
		//Instantiate the RecController,and call scopy() to copy local file to the remote target path
		RecController controller=new RecController(recAuthInfo,targetPath);
		controller.scopy(gzFile, targetPath);
		
		//uncompress the gzFile in the remote host
		String cmd="cd "+targetPath+" && tar zxf "+gzFile.getName()+" && cd "+gzFile.getName().substring(0,gzFile.getName().indexOf(".tar.gz"))+"&& pwd";
		recAuthInfo.execCommand(cmd,result,errorResult);
		//the result is similar to "/usr/local/nginx/nginx-1.0.5"
		if(result.isEmpty()) 
		{
			//if result is empty throw the RemoteException
			if(!errorResult.isEmpty())
			{
				throw new RemoteException(errorResult.toString());
			}
			
		}
		else
		{
			if(result.get(0).toString().compareTo(targetPath+gzFile.getName().substring(0,gzFile.getName().indexOf(".tar.gz")))==0)
			{
				result.clear();
				errorResult.clear();
				
				//get the compile option from the parameter modules
				List<String> optionList=new ArrayList<String>(0) ;
				for(int i=0;i<modules.size();i++)
				{
					optionList.add("--with-"+modules.get(i).getName().substring(4)+" ");
				}
				//optionList.add("--with-"+s1.substring(4)+" --with-"+s2.substring(4));
				
				//configure the middleware src
				cmd="cd "+targetPath+gzFile.getName().substring(0,gzFile.getName().indexOf(".tar.gz"))+" && ./configure --prefix="+targetPath+" "+optionList.toString().substring(optionList.toString().indexOf('[')+1, optionList.toString().indexOf(']'));
				recAuthInfo.execCommand(cmd,result,errorResult);
				//if having error throw the exception
				if(result.toString().indexOf("configure: error:")==-1)
				{
					//install the middleware
					cmd="cd "+targetPath+gzFile.getName().substring(0,gzFile.getName().indexOf(".tar.gz"))+" && make && make install";
					result.clear();
					errorResult.clear();
					recAuthInfo.execCommand(cmd,result,errorResult);
					if(result.toString().indexOf("error")==-1)
					{
						if(errorResult.isEmpty())
							System.out.println("Nginx is installed successfully");
						else 
							throw new RemoteException(errorResult.toString());
					}
					else
						throw new RemoteException(result.toString());
				}
				else
				{
						throw new RemoteException(result.toString());//"There is no nginx.conf"
				}
			}
			else
			{
				throw new RemoteException("The Current path is invalid");
			}
		}
		//After installing the middleware,return a instance
		RecMiddlewareFactory recmiddleware=new RecMiddlewareFactory(authInfo,targetPath);
		return recmiddleware;
		
	}
	/**
	 * Get the controller interface in order to operate Nginx' running. The Nginx is binded with the instance.
	 * @return The Controller interface of the middleware (Nginx).
	 * */
	abstract public Controller getController();
	/**
	 * Get the configurator interface in order to operate Nginx configure file. The Nginx is binded with the instance.
	 * @return The configurator interface of the middleware (Nginx).
	 * */
	abstract public Configurator getConfigurator();
	/**
	 * Get the monitor interface in order to fetch the remote host and middleware's running status. The Nginx is binded with the instance.
	 * @return The monitor interface of the middleware (Nginx).
	 * */
	abstract public Monitor getMonitor();
	/**
	 * Create an instance of AuthInfo without any info.
	 * Then you can set the instance info through AuthInfo interface.
	 * @return An instance of AuthInfo without any info.
	 * */
	static public AuthInfo newAuthInfo(){
		// TODO
		 RecAuthInfo recauthinfo=new RecAuthInfo();
			return recauthinfo;
	}
}
