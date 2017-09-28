package com.mysql;

import com.utils.DatabaseJndiConn;
import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Random;

@Controller
public class MysqlJdbcController {
    SqlExcuteUtil sqlExcute = new SqlExcuteUtil();

    @RequestMapping("/mysqlJdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append("<html><head><title></title></head><body><h2>MySQL JDBC</h2>");

        try {
            stringBuffer.append(sqlExcute.excuteSql("mysql", "Jdbc", ""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

    @RequestMapping("/mysqlJdbcPs1")
    @ResponseBody
    public String testPs() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Mysql jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql1 = "insert into test_user(id,name,birthday,longcontent) values (?,?,now(),?)";
        String insertsql2 = "insert into test_user(id,name,birthday,longcontent) values (?,?,?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
//        String deletesql1 = "delete from test_user where name like ?";
//        String deletesql2 = "delete from test_user where id = ?";.
        String deletesql = "delete from test_user";
        String mulselctsql = "select name,birthday,sleep(10) from (select name,birthday from test_user where name like ?)as foo";
        try {
            Connection conn = sqlExcute.getConnPs("mysql", "Jdbc");

//            PreparedStatement preparedStatement = conn.prepareStatement(deletesql1);
//            preparedStatement.setString(1, "%proc%");
//            preparedStatement.execute();
//
//            PreparedStatement preparedStatement2 = conn.prepareStatement(deletesql2);
//            preparedStatement2.setInt(1,1002);
//            preparedStatement2.execute();
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.execute();

            preparedStatement = conn.prepareStatement(insertsql1);
            preparedStatement.setInt(1, 1002);
            preparedStatement.setString(2, "PreparedStatement");
            String random = sqlExcute.getRandomString(25000);
            preparedStatement.setString(3, random);
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertsql2);
            int i = 1;
            Object[] values = {2, "PreparedStatement", "2017-01-20", null};
            for (Object o : values) {
                int key = i++;
                if (o == null) {
                    preparedStatement.setObject(key, o, java.sql.Types.VARCHAR);
                } else {
                    System.out.println("-------key===" + key + "---------value========" + o);
                    preparedStatement.setObject(key, o);
                }
            }
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
            stringBuffer.append(insertsql1 + "<br>" + insertsql2 + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

    @RequestMapping("/mysqlJndi")
    @ResponseBody
    public String testJndi(HttpServletRequest req, HttpServletResponse resp) {
        SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>mysql jndi preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user(id,name,birthday,longcontent) values (?,?,now(),?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql = "delete from test_user where id= ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";
        String jndiName = "jdbc/mysql";
        ServletContext servletContext = DatabaseJndiConn.getServletContext();
        if (servletContext != null) {
            String serverInfo = servletContext.getServerInfo();
            if (serverInfo != null && serverInfo.toLowerCase().contains("jboss")) {
                jndiName = "java:jboss/jdbc/mysql";
            } else if (serverInfo != null && serverInfo.toLowerCase().contains("tomcat")) {
                jndiName = "java:comp/env/jndi/mysql";

            }
        }
        stringBuffer.append("JNDI name :" + jndiName + "<br>");
        try {
            //Connection conn = DatabaseJndiConn.getConnection(jndiName);

            //李伟元修改
            Connection conn = null;
            //1、初始化名称查找上下文  第二次
            //Context ctx = new InitialContext();
            //DataSource ds = (DataSource)ctx.lookup(jndiName);
            //conn=ds.getConnection();
            //System.err.println("MySQL Connection pool connected !!");



            //Context envContext  = (Context)ctx.lookup("java:/comp/env");//固定，不需要修改
            //DataSource ds = (DataSource)envContext.lookup("jdbc/");







            //第三次---tomcat
            Context initCtx = new InitialContext();
            Context ctx = (Context) initCtx.lookup("java:comp/env");

            Object obj = (Object) ctx.lookup("jdbc/mysql");
            javax.sql.DataSource ds = (javax.sql.DataSource) obj;
            conn=ds.getConnection();


            //第四次---jboss--可以
            //Context initCtx = new InitialContext();
            //Context ctx = (Context) initCtx.lookup("java:comp/env");

            //Object obj = (Object) initCtx.lookup("java:/MySqlDS");
            //javax.sql.DataSource ds = (javax.sql.DataSource) obj;
            //DataSource ds = (DataSource) initCtx.lookup("MySqlDataSource");
            //conn=ds.getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setInt(1, 1);
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


    @RequestMapping("/mysqlJdbcPs")
    @ResponseBody
    public String getMysqlPs() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Mysql jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql1 = "insert into test_user(id,name,birthday,longcontent) values (?,?,now(),?)";
        String insertsql2 = "insert into test_user(id,name,birthday,longcontent) values (?,?,?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        // String selectsql = "select * from test_user where name like ? and rownum<=?";
        String deletesql1 = "delete from test_user where name like ?";
        String deletesql2 = "delete from test_user where id = ?";
        String mulselctsql = "select name,birthday,sleep(10) from (select name,birthday from test_user where name like ?)as foo";

        try {
            Connection conn = sqlExcute.getConnPs("mysql", "Jdbc");
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql1);
            preparedStatement.setString(1, "%proc%");
            preparedStatement.execute();

            PreparedStatement preparedStatement2 = conn.prepareStatement(deletesql2);
            preparedStatement2.setInt(1, 2);
            preparedStatement2.execute();

            preparedStatement = conn.prepareStatement(insertsql1);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            preparedStatement.setString(3, getRandomString(25000));
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(insertsql2);
            int i = 1;
            Object[] values = {2, "PreparedStatement", "2017-01-20", null};
            for (Object o : values) {
                int key = i++;
                if (o == null) {
                    preparedStatement.setObject(key, o, java.sql.Types.VARCHAR);
                } else {
                    System.out.println("-------key===" + key + "--------value========" + o);
                    preparedStatement.setObject(key, o);
                }
            }
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
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else {
            stringBuffer.append(insertsql1 + "<br>" + insertsql2 + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql1 + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append(" </body></html>");

        return stringBuffer.toString();
    }

    private String getRandomString(int length) {

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
