package com.utils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Iterator;
import java.util.Random;

public class SqlExcuteUtil {
    private String dataType, sourceType, sqlType;
    private StringBuffer resultbuffer = new StringBuffer();
    private DataSource dataSource;
    private Connection connection;
    private Statement stmt;

    public StringBuffer excuteSql(String dataType, String sourceType) throws SQLException {
        getConn(dataType, sourceType);
        Iterator iterator = DataConf.getSqlMap(connection).keySet().iterator();
        while (iterator.hasNext()) {
            sqlType = (String) iterator.next();
            if (sqlType.equals("select")) {
                executeQuery(toSql(sqlType));
            } else if (sqlType.equalsIgnoreCase("call")) {
                executeProc(toSql(sqlType));
            } else if (!"create".equalsIgnoreCase(sqlType)&&!"alter".equalsIgnoreCase(sqlType)) {
                executeUpdate(toSql(sqlType));
            }
        }
        disConn();
        return resultbuffer;
    }

    public StringBuffer excuteSql(String dataType, String sourceType, String sqlType) throws SQLException {
        if (sqlType.length() == 0) {
            excuteSql(dataType, sourceType);
        } else {
            this.sqlType = sqlType;
            getConn(dataType, sourceType);
            if (sqlType.equals("select")) {
                executeQuery(toSql(sqlType));
            } else if (sqlType.equalsIgnoreCase("call")) {
                executeProc(toSql(sqlType));
            } else
                executeUpdate(toSql(sqlType));
        }
        disConn();
        return resultbuffer;
    }

    public StringBuffer excuteSql(String dataType, String sourceType, String sqlType, String sqlString) throws SQLException {
        getConn(dataType, sourceType);
        this.sqlType = sqlType;
        if (sqlType.equals("select")) {
            executeQuery(sqlString);
        } else if (sqlType.equalsIgnoreCase("call")) {
            executeProc(sqlType);
        } else
            executeUpdate(sqlString);
        disConn();
        return resultbuffer;
    }


    private void getConn(String dataType, String sourceType) throws SQLException {
        this.resultbuffer.delete(0, resultbuffer.length());
        this.dataSource = DataConf.getDataSource(dataType, sourceType);
        this.connection = dataSource.getConnection();
        this.stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        //resultbuffer.append("<br>***************  getConnection success  **************<br>");
        this.dataType = dataType;
        this.sourceType = sourceType;
    }

    public Connection getConnPs(String dataType, String sourceType) throws SQLException {
        this.dataSource = DataConf.getDataSource(dataType, sourceType);
        this.connection = dataSource.getConnection();
        return this.connection;
    }

    private void disConn() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            resultbuffer.append("<font color=\"red\">" + dataType + "_" + sourceType + "_" + sqlType + "</font>" + "  disconnection - connection close error , ErrorCode is " + e.getErrorCode() + "  . msg is " + e.getMessage() + "<br>");
            e.printStackTrace();
        }
        // resultbuffer.append("***********************  disConnection success  ***********************<br>");
    }

    private void executeProc(String sqlString) {
        try {
            CallableStatement cstmt = connection.prepareCall(sqlString);
            //cstmt.setString("source", sourceType);
            // cstmt.setString(1, sourceType);
            Long date1 = System.currentTimeMillis();
            int result = cstmt.executeUpdate();
            Long date2 = System.currentTimeMillis();
            Long during = date2 - date1;
            resultbuffer.append("executeProc druing :" + during + " ms<br>");
            if (cstmt != null) {
                cstmt.close();
            }
            //   resultbuffer.append("<p>" + sqlString + ";<br>" + dataType + "_" + sourceType + "--execute finished, </p><br>");// " + result + "  rows .</p><br>");
        } catch (SQLException e) {
            resultbuffer.append("<font color=\"red\">" + sqlString + "</font>" + "  " + dataType + "_" + sourceType + "  SQL Execute Failed , ErrorCode is " + e.getErrorCode() + "  . msg is " + e.getMessage() + "<br>");
            e.printStackTrace();
        }
    }

    private void executeUpdate(String sqlString) {
        try {
            Long date1 = System.currentTimeMillis();
            stmt.executeUpdate(sqlString);
            Long date2 = System.currentTimeMillis();
            Long during = date2 - date1;
            resultbuffer.append("executeUpdate druing :" + during + " ms<br>");
            //int result = stmt.executeUpdate(sqlString);
               /* if (sqlType == "insert" || sqlType == "update" || sqlType == "delete") {
                    resultbuffer.append("<p>" + sqlString + ";<br>" + dataType + "_" + sourceType + "--execute finished,  " + result + "  rows affected.</p><br>");
                } else {
                    resultbuffer.append("<p>" + sqlString + ";<br>" + dataType + "_" + sourceType + "--execute finished, </p><br>");
                }*/
        } catch (SQLException e) {
            resultbuffer.append("<font color=\"red\">" + sqlString + "</font>" + "  " + dataType + "_" + sourceType + "  SQL Execute Failed , ErrorCode is " + e.getErrorCode() + "  . msg is " + e.getMessage() + "<br>");
            e.printStackTrace();
        }

    }

    private void executeQuery(String sqlString) {
        ResultSet resultSet;
        try {
            Long date1 = System.currentTimeMillis();
            resultSet = stmt.executeQuery(sqlString);
            Long date2 = System.currentTimeMillis();
            Long during = date2 - date1;
            resultbuffer.append("executeUpdate druing :" + during + " ms<br>");
            while (resultSet.next()) {
                resultbuffer.append("<font color=\"green\">" + resultSet.getString(1) + "--" + resultSet.getString(2) + "--" + resultSet.getString(3) + "</font><br>");
            }
            // resultbuffer.append("<p>" + sqlString + ";<br>" + dataType + "_" + sourceType + "--execute finished,</p><br>");
            resultSet.close();
        } catch (SQLException e) {
            resultbuffer.append("<font color=\"red\">" + sqlString + "</font>" + "  " + dataType + "_" + sourceType + "  SQL Execute Failed , ErrorCode is " + e.getErrorCode() + "  . msg is " + e.getMessage() + "<br>");
            e.printStackTrace();
        }
    }

    private String toSql(String sqlType) {
        String sql = (String) DataConf.getSqlMap(connection).get(sqlType);
        resultbuffer.append(sql + "; - - - - ");
        return sql;
    }

    public String getRandomString(int length) { //length表示生成字符串的长度
        String base = "你我他的地のabcxyz0123789~!@#$%^&*();/<>|\\][{}（），。、《》？；‘：“”’";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            stringBuffer.append(base.charAt(number));
        }
        return stringBuffer.toString();
    }

}