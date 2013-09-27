package org.cs2c.nginlib.ctl;

import org.cs2c.nginlib.MiddlewareFactory;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.AuthInfo;
public class TestController {

	public static void main(String[] args) {
				
		
	
		
			AuthInfo authInfo=MiddlewareFactory.newAuthInfo();
			authInfo.setHost("10.1.50.4");
			authInfo.setUsername("root");
			authInfo.setPassword("cs2csolutions");
			
			
			
			MiddlewareFactory instance1 = null;
			try {
				instance1 = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx/");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Controller reccontro=instance1.getController();
			if(reccontro==null)
				try {
					throw new RemoteException("reccontro=null");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				try {
					reccontro.reload();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		/*	MiddlewareFactory instance = null;
			try {
				instance = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx/");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File gzFile=new File("d:/Server.zip");
		
			if(gzFile.exists()==false)
				try {
					throw new IOException();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
			String targetPath="/root/";
			//MiddlewareFactory instance2=MiddlewareFactory.install(authInfo, gzFile, targetPath);//, modules
			MiddlewareFactory instance2 = null;
			try {
				instance2 = MiddlewareFactory.getInstance(authInfo, "/usr/local/nginx/");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//try{
			Controller reccontro1=instance2.getController();
			
			if(reccontro1==null)
				try {
					throw new RemoteException("reccontro1=null");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				try {
					//reccontro1.reload();
					reccontro1.deploy(gzFile,targetPath);
					System.out.println(gzFile.getName());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			*/
		/*	File gzFile=new File("d:/nginx-1.0.15.tar.gz");
			if(gzFile.exists()==false)
				try {
					throw new IOException();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
			try {
				MiddlewareFactory instance ;
				instance = MiddlewareFactory.install(authInfo, gzFile, "/root/nginx/", "ngx_http_xslt_module","ngx_http_gzip_static_module");
			} catch (IOException | RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			

			
	}
}