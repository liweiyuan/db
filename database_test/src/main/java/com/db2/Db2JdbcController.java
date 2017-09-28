package com.db2;

import com.utils.DatabaseJndiConn;
import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;

@Controller
public class Db2JdbcController {
    @RequestMapping("/db2Jdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
        stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append("<html><head><title></title></head><body><h2>DB2 JDBC</h2>");

		try {
			stringBuffer.append(sqlExcute.excuteSql("db2","Jdbc",""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
    @RequestMapping("/db2JdbcPs")
    @ResponseBody
    public String testPs() {
    	SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
    	StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>db2 jdbc preparedStatement</h2>");
        String queryResult="<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user(id,name,birthday,LONGCONTENT) values (?,?,current_timestamp,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
       // String selectsql = "select * from test_user where name like ? and rownum<=?";
//        String deletesql = "delete from test_user where name like ?";
        String deletesql = "delete from test_user where id=1";
        String mulselctsql="select * from (select name,birthday from test_user where name like ?)";
        try {
            Connection conn =sqlExcute.getConnPs("db2","Jdbc");

            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
//            preparedStatement.setString(1, "%ps%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            String str="abc";//getRandomString(2);
            //preparedStatement.setAsciiStream(3, new ByteArrayInputStream(str.getBytes("UTF-8")),str.length());
            //InputStream inputStream=new ByteArrayInputStream(str.getBytes("UTF-8"));
            //preparedStatement.setAsciiStream(3, inputStream,str.length());
            //preparedStatement.setCharacterStream(3, new StringReader(str),str.length());
         	preparedStatement.setBlob(3,new SerialBlob(str.getBytes("UTF-8")));
			
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "ps");
            cstmt.execute();
            cstmt.close();

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%update_ps%");
            //preparedStatement.setInt(2, 5);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
                queryResult=rs.getString(1)+" - "+rs.getString(2)+"<br>";
            }

            conn.close();
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
    @RequestMapping("/db2Jndi")
    @ResponseBody
    public String testJndi() {
    	StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>DB2 Jndi preparedStatement</h2>");
        String queryResult="<a style=\"color: red\">norecord</a>";
        String jndiName="jdbc/db2";
        String insertsql = "insert into test_user(id,name,birthday,LONGCONTENT) values (?,?,current_timestamp,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql="select * from (select name,birthday from test_user where name like ?)";
        ServletContext servletContext=DatabaseJndiConn.getServletContext();  
        if(servletContext!=null){
        	String serverInfo=servletContext.getServerInfo();
        	if(serverInfo!=null&&serverInfo.toLowerCase().contains("jboss")){
                jndiName="java:jboss/jdbc/db2";
            }else if(serverInfo!=null&&serverInfo.toLowerCase().contains("tomcat")){
            	jndiName="java:comp/env/jndi/db2";
            }
        }
       stringBuffer.append("JNDI name :"+jndiName+"<br>");
        try {

            //李伟元--jboss
            //Context initCtx = new InitialContext();
            //DataSource ds = (DataSource) initCtx.lookup("DB2DS");

            //Connection conn=ds.getConnection();
            //Connection conn = DatabaseJndiConn.getConnection("DB2DS");

            //weblogic
            Connection conn = DatabaseJndiConn.getConnection("DB2");
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setString(1, "%ps%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            String str="abc";//getRandomString(2);

            preparedStatement.setBlob(3,new SerialBlob(str.getBytes("UTF-8")));
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "ps");
            cstmt.execute();
            cstmt.close();

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%ps%");
            //preparedStatement.setInt(2, 5);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
                queryResult=rs.getString(1)+" - "+rs.getString(2)+"<br>";
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
