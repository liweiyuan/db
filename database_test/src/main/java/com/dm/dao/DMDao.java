package com.dm.dao;

import com.dm.util.CloseUtil;

import java.sql.*;






public class DMDao {

	private static String DM7_driver = "dm.jdbc.driver.DmDriver";
	private static String DM7_connectUrl = "jdbc:dm://192.168.2.214:5236/SYSTEM";
    private static String DM7_name = "SYSDBA";
    private static String DM7_password = "tingyun2o13";
    
    private static String optkey = "dm7";
    private static String dateString = "now()";
    
    private static String DELETE = "delete from TEST_TABLE where USER_ID=1";
    private static String INSERT = "insert into TEST_TABLE (USER_ID,USER_NAME,USER_BIRTHDAY) values (1, '" + optkey + "_insert'," + dateString + ")";
    private static String SELECT_INSERT = "select USER_ID,USER_NAME,USER_BIRTHDAY from TEST_TABLE where USER_NAME like '%" + optkey + "%'";
    private static String UPDATE = "update TEST_TABLE set USER_NAME='" + optkey + "_update' where USER_NAME = '" + optkey + "_insert'";
    private static String SELECT_UPDATE = "select USER_ID,USER_NAME,USER_BIRTHDAY from TEST_TABLE where USER_NAME like '%" + optkey + "%'";
   // private static String SELECT_ALL = "select * from TEST_TABLE";
    
    private Connection conn ;
    
    private Statement stmt;
    
	public DMDao() {
	    try {
	    	Class.forName(DM7_driver);
			conn = DriverManager.getConnection(DM7_connectUrl,DM7_name,DM7_password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public  String excuteSql() throws SQLException{
		this.stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String resultDelete = executeUpdate(DELETE);
		String resultInsert = executeUpdate(INSERT);
		String resultSelectInsert = executeQuery(SELECT_INSERT);
		String resultUpdate = executeUpdate(UPDATE);
		String resultSelectupdate = executeQuery(SELECT_UPDATE);
		//String resultSelect = executeQuery(SELECT_ALL);
		CloseUtil.CloseAll(stmt, conn);
		return resultDelete + resultInsert+ resultSelectInsert + resultUpdate  + resultSelectupdate;
	}
	
	
    private String executeQuery(String sqlString) {
        ResultSet resultSet;
        String result = null;
        try {
            Long date1 = System.currentTimeMillis();
            resultSet = stmt.executeQuery(sqlString);
            Long date2 = System.currentTimeMillis();
            Long during = date2 - date1;
            
            while (resultSet.next()){
            	result = sqlString + "---"+ during + "ms <br>" +
            			"<font color=\"green\">" + resultSet.getString(1) + 
            			"--" + resultSet.getString(2) + 
            			"--" + resultSet.getString(3) + "</font><br>";
            }
            resultSet.close();
        } catch (SQLException e) {
        	result = "<font color=\"red\">" + sqlString + 
        			"</font>" + 
        			"  " + "DM7" + 
        			"_" + "jdbc" + 
        			"  SQL Execute Failed , ErrorCode is " + e.getErrorCode() + 
        			"  . msg is " + e.getMessage() + "<br>";
        	e.printStackTrace();
        }
        return result;
    }
	
    private String executeUpdate(String sqlString) {
    	String result = null;
        try {
            Long date1 = System.currentTimeMillis();
            stmt.executeUpdate(sqlString);
            Long date2 = System.currentTimeMillis();
            Long during = date2 - date1;
            result = sqlString +"---" + "executeUpdate druing :" + during + " ms<br>";
        } catch (SQLException e) {
        	result= "<font color=\"red\">" + sqlString + 
        			"</font>" + "  " + "DM7" + 
        			"_" + "JDBC" + 
        			"  SQL Execute Failed , ErrorCode is " + e.getErrorCode() + 
        			"  . msg is " + e.getMessage() + "<br>";
            e.printStackTrace();
        }
        return result;
    }
	
}
