/**
 * 
 */
package com.utils;
/**
 *
 */

//import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

//import java.util.logging.Logger;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.logging.Logger;
//import javax.sql.DataSource;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * @author renyp
 *
 */
public class JdbcDatasource implements DataSource {
	private static org.apache.commons.logging.Log Log = LogFactory.getLog(JdbcDatasource.class.getName());
	private String driverClass;
	private String connectUrl;
	private String username;
	private String password;


	public JdbcDatasource() {

	}

	public JdbcDatasource(String driverClass, String connectUrl, String username, String password) {
		super();
		this.driverClass = driverClass;
		this.connectUrl = connectUrl;
		this.username = username;
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	///TODO to be implemented
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	///TODO to be implemented
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * 从DataSource中获取一个数据库连接
	 */
	public Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			Class.forName(this.driverClass);
			connection = DriverManager.getConnection(this.connectUrl, this.username, this.password);
		} catch(ClassNotFoundException ex) {
			Log.error("class not found: " + this.driverClass);
		}
		return connection;
	}

	/**
	 * NOT implemented.
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection = null;
		try {
			Class.forName(this.driverClass);
			connection = DriverManager.getConnection(connectUrl, username, password);
		} catch(ClassNotFoundException ex) {
			Log.error("class not found: " + this.driverClass);
		}

		return connection;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
