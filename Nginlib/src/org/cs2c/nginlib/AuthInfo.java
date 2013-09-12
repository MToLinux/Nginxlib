/**
 * 
 */
package org.cs2c.nginlib;

/**
 * @author Mikes
 * <P/>User use the AuthInfo interface to specify remote host and Authentication information.
 * The info includes host ip, username and password. The method of accessing remote host is ssh2 in default.
 * AuthInfo instance should be set properly for get instance of MiddlewareFactory.
 * @see MiddlewareFactory
 */
public interface AuthInfo {
	/**
	 * Set remote host name or ip address.
	 * @param hostName remote host name or ip address
	 * */
	void setHost(String hostName);
	/**
	 * Set remote host user name.
	 * @param username remote host user name
	 * */
	void setUsername(String username);
	/**
	 * Set remote password for specific user.
	 * */
	void setPassword(String password);
}
