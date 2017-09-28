package com.mysql;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.*;

@Controller
public class MysqlSpringJdbcController{
    @Resource
    private DataSource dataSourceMysql;

    String insertsql = "insert into test_user(id,name,birthday) values (?,?,now())";
    String updatesql = "update test_user set name=? where id=1";
    String callsql = "{call proc_update(?)}";
    String deletesql = "delete from test_user where name like ?";
    String mulselctsql="select * from (select name,birthday from test_user where name like ?)as foo";
    @RequestMapping("/mysqlSpringJdbc")
    @ResponseBody
    public String test(HttpServletResponse resp){
        resp.setContentType("text/html;charset=UTF-8");
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>mysql SpringJdbc</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";

        try {
            Connection conn=dataSourceMysql.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setString(1, "%ps%");
            preparedStatement.execute();
            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 10);
            preparedStatement.setString(2, "ps_PreparedStatement");
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%ps%");
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
        }

        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
