package com.sqlserver;

import com.utils.DatabaseJndiConn;
import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.sql.*;

@Controller
public class SqlserverJdbcController {
	SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
    @RequestMapping("/sqlserverJdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append("<html><head><title></title></head><body><h2>Sqlserver JDBC</h2>");

		try {
			stringBuffer.append(sqlExcute.excuteSql("sqlserver","Jdbc",""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
    @RequestMapping("/sqlserverJdbcPs")
    @ResponseBody
    public String testPs() {
    	StringBuffer stringBuffer = new StringBuffer();
    	stringBuffer.append("<html><head><title></title></head><body><h2>Sqlserver jdbc preparedStatement</h2>");
        String queryResult="<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user(id,name,birthday) values (1,?,'2016')";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
       // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user ";
        String selctsql="select * from test_user";
		try {
			Connection conn =sqlExcute.getConnPs("sqlserver","Jdbc");
			PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setString(1, "PreparedStatement");
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "ps");
            cstmt.execute();
            cstmt.close();

            preparedStatement = conn.prepareStatement(selctsql);
          //  preparedStatement.setString(1, "%proc%");
            //preparedStatement.setInt(2, 5);
            ResultSet rs = preparedStatement.executeQuery();
            queryResult="no <br>";
            while (rs.next()) {
                queryResult=rs.getString("id")+" - "+rs.getString("name")+"- -"+rs.getString("birthday");
                //queryResult=rs.getString(1)+" - "+rs.getString(2)+"<br>";
            }
            conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(queryResult.contains("norecord")){
	       stringBuffer.append(queryResult);
	    }else{
	       stringBuffer.append(insertsql+"<br>"+updatesql+"<br>"+callsql+"<br>"+selctsql+"<br>"+deletesql+"<br>查询结果：<br>"+queryResult);
	    }
		stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }
    @RequestMapping("/sqlserverJndi")
    @ResponseBody
    public String testJndi() {
    	StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>oracle jndi preparedStatement</h2>");
        String queryResult="<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user values (?,'1984-06-12', ?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";
        String jndiName="jdbc/SqlServerDS";
        ServletContext servletContext=DatabaseJndiConn.getServletContext();  
        if(servletContext!=null){
        	String serverInfo=servletContext.getServerInfo();
        	if(serverInfo!=null&&serverInfo.toLowerCase().contains("jboss")){
//                jndiName="java:jboss/jdbc/SqlServerDS";
                jndiName="java:/SqlServerDS";
            }else if(serverInfo!=null&&serverInfo.toLowerCase().contains("tomcat")){
            	jndiName="java:comp/env/jndi/SqlServerDS";
            }
        }
        stringBuffer.append("JNDI name :"+jndiName+"<br>");
        try {
            Connection conn = DatabaseJndiConn.getConnection(jndiName);
            // Statement stat = conn.createStatement();

            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setString(1, "%proc%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            //preparedStatement.setClob(3, new StringReader(getRandomString(25000)));
            Clob clob = conn.createClob();
            String random=sqlExcute.getRandomString(25000);
            clob.setString(1,random);
            preparedStatement.setClob(3,clob);
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "call");
            cstmt.execute();
            cstmt.close();

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%call%");
            //preparedStatement.setInt(2, 5);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(queryResult.contains("norecord")){
            stringBuffer.append(queryResult);
        }else{
        	stringBuffer.append(insertsql+"<br>"+updatesql+"<br>"+callsql+"<br>"+mulselctsql+"<br>"+deletesql+"<br>查询结果：<br>"+queryResult);
        }
    	stringBuffer.append("</body></html>");
    	return stringBuffer.toString();
    }
}
