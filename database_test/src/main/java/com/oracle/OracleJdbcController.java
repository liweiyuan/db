package com.oracle;

import com.utils.DatabaseJndiConn;
import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.Random;

@Controller
public class OracleJdbcController {
    SqlExcuteUtil sqlExcute = new SqlExcuteUtil();

    @RequestMapping("/oracleJdbc")
    @ResponseBody
    public String test() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append("<html><head><title></title></head><body><h2>Oracle JDBC</h2>");

        try {
            stringBuffer.append(sqlExcute.excuteSql("oracle", "Jdbc"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

    @RequestMapping("/oracleJdbcPs1")
    @ResponseBody
    public String testPs() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Oracle jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql1 = "insert into test_user (id, name, birthday, longcontent)  values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'),?)";
        String insertsql2 = "insert into test_user(ID,NAME,BIRTHDAY,LONGCONTENT2) values (?,?,?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String deletesql = "delete from test_user";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";
        try {
            Connection conn = sqlExcute.getConnPs("oracle", "Jdbc");


            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql1);
            preparedStatement.setInt(1, 1002);
            preparedStatement.setString(2, "PreparedStatement");
            Clob clob = conn.createClob();
            String random = sqlExcute.getRandomString(25000);
            clob.setString(1, random);
            preparedStatement.setClob(3, clob);
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%update_ps%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql1 + "<br>" + insertsql2 + "<br>" + updatesql + "<br>" +/* callsql +*/ "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }

    @RequestMapping("/oracleJndi")
    @ResponseBody
    public String testJndi() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>oracle jndi preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user (id, name, birthday, longcontent)  values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'),?)";
        String updatesql = "update test_user set name=? where id=1";
        //String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";
        String jndiName = "jdbc/oracle";
        ServletContext servletContext = DatabaseJndiConn.getServletContext();
        if (servletContext != null) {
            String serverInfo = servletContext.getServerInfo();
            if (serverInfo != null && serverInfo.toLowerCase().contains("jboss")) {
                jndiName = "java:jboss/jdbc/oracle";
            } else if (serverInfo != null && serverInfo.toLowerCase().contains("tomcat")) {
                jndiName = "java:comp/env/jndi/oracle";
            }
        }
        stringBuffer.append("JNDI name :" + jndiName + "<br>");
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
            String random = sqlExcute.getRandomString(25000);
            clob.setString(1, random);
            preparedStatement.setClob(3, clob);
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            /*CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "call");
            cstmt.execute();
            cstmt.close();*/

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%update_ps%");
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
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" /*+ callsql */+ "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }


    //李伟元

    @RequestMapping("/oracleJdbcPs")
    @ResponseBody
    public String getOraclePs() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>oracle jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">error</a>";
        String insertsql1 = "insert into test_user (id, name, birthday, longcontent)  values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'),?)";
        String insertsql2 = "insert into test_user(ID,NAME,BIRTHDAY,LONGCONTENT2) values (?,?,?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String deletesql1 = "delete from test_user where name like ?";
        String deletesql2 = "delete from test_user where id = ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";


        try {
            Connection conn = sqlExcute.getConnPs("oracle", "Jdbc");
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql1);
            preparedStatement.setString(1, "%proc%");
            preparedStatement.execute();

            PreparedStatement preparedStatement2 = conn.prepareStatement(deletesql2);
            preparedStatement2.setInt(1, 2);
            preparedStatement2.execute();

            preparedStatement = conn.prepareStatement(insertsql1);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "update_ps");
            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%update_ps%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql1 + "<br>" + insertsql2 + "<br>" + updatesql + "<br>" /*+ callsql*/ + "<br>" + mulselctsql + "<br>" + "<br>查询结果：<br>");
        }
        stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }

    private String getRandomString(int length) {

        String base = "你我他的地のabcxyz0123789~!@#$%^&*();/<>|\\][{}（），。、《》？；‘：“”’";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <= length - 1; i++) {
            int number = random.nextInt(base.length());
            stringBuffer.append(base.charAt(number));
        }
        return stringBuffer.toString();
    }

}
