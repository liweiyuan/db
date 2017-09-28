package com.oracle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class OracleDruidController {
    @Resource
    private DataSource druidDSOracle;

    String insertsql = "insert into test_user(id,name,birthday) values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'))";
    String updatesql = "update test_user set name=? where id=1";
    //String callsql = "{call proc_update(?)}";
    String deletesql = "delete from test_user where id= ?";
    String mulselctsql="select * from (select name,birthday from test_user where name like ?)";
    @RequestMapping("/oracleDruid")
    @ResponseBody
    public String test(HttpServletResponse resp){
        resp.setContentType("text/html;charset=UTF-8");
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>oracle Druid</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";

        try {
            Connection conn=druidDSOracle.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(deletesql);
            preparedStatement.setInt(1, 1);
            preparedStatement.execute();
            preparedStatement = conn.prepareStatement(insertsql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "PreparedStatement");
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(updatesql);
            preparedStatement.setString(1, "%update_ps%");
            preparedStatement.execute();

           /* CallableStatement cstmt = conn.prepareCall(callsql);
            cstmt.setString(1, "call");
            cstmt.execute();
            cstmt.close();*/

            preparedStatement = conn.prepareStatement(mulselctsql);
            preparedStatement.setString(1, "%update_ps%");
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
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" +/*+ callsql +*/ "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
