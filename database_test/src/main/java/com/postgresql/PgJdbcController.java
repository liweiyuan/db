package com.postgresql;

import com.utils.DatabaseJndiConn;
import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.Random;

@Controller
public class PgJdbcController {
    SqlExcuteUtil sqlExcute = new SqlExcuteUtil();

    @RequestMapping("/postgresqlJdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append("<html><head><title></title></head><body><h2>Postgresql JDBC</h2>");

        try {
            stringBuffer.append(sqlExcute.excuteSql("postgresql", "Jdbc", ""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }

    //过去方法
    @RequestMapping("/postgresqlJdbcPs1")
    @ResponseBody
    public String testPs() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Postgresql jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user values (?,?,'2016',?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
//        String deletesql = "delete from test_user where name like ?";
//        String deletesql2 = "delete from test_user where id = ?";
        String deletesql = "delete from test_user";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";
        try {
            Connection conn = sqlExcute.getConnPs("postgresql", "Jdbc");
//            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
//            preparedStatement.setString(1, "%proc%");
//            preparedStatement.execute();
//            PreparedStatement preparedStatement2 = conn.prepareStatement(deletesql2);
//            preparedStatement2.setInt(1,1002);
//            preparedStatement2.execute();

            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1002);
            preparedStatement.setString(2, "PreparedStatement");
            String random = sqlExcute.getRandomString(25000);
            preparedStatement.setString(3, random);
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
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

    @RequestMapping("/postgresqlJndi")
    @ResponseBody
    public String testJndi() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>postgresql jndi preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user values (?,?,current_timestamp,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";
        String jndiName = "jdbc/postgresql";
        ServletContext servletContext = DatabaseJndiConn.getServletContext();
        if (servletContext != null) {
            String serverInfo = servletContext.getServerInfo();
            if (serverInfo != null && serverInfo.toLowerCase().contains("jboss")) {
                jndiName = "java:jboss/jdbc/postgresql";
            }
        }
        stringBuffer.append("JNDI name :" + jndiName + "<br>");
        try {
            Connection conn = DatabaseJndiConn.getConnection(jndiName);
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setString(1, "%proc%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            String random = sqlExcute.getRandomString(25000);
            preparedStatement.setString(3, random);
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
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryResult.contains("error")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append("</body></html>");
        return stringBuffer.toString();
    }

    //李伟元
    @RequestMapping("/postgresqlJdbcPs")
    @ResponseBody
    public String getPsPostgresql() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>postgresql jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">error</a>";
        String insertsql = "insert into test_user values (20,'aaupdate_psbbb','2016','java')";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";

        try {
            Connection conn = sqlExcute.getConnPs("postgresql", "Jdbc");
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setString(1, "%proc%");
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql);
            //preparedStatement.setInt(1, 100);
            //preparedStatement.setString(2, "aaupdate_psbbb");
            //preparedStatement.setString(3, getRandomString(25));
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
                queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (queryResult.contains("error")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

    private String getRandomString(int length) {
        String base = "你我他的地のabcxyz0123789~!@#$%^&*();/<>|\\][{}（），。、《》？；‘：“”’";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <= length - 2; i++) {
            int number = random.nextInt(base.length());
            stringBuffer.append(base.charAt(number));
        }
        return stringBuffer.toString();
    }


}
