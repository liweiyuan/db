package com.derby;

import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class DerbyJdbcController {
    @RequestMapping("/derbyJdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
        stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append("<html><head><title></title></head><body><h2>Derby JDBC</h2>");

		try {
			stringBuffer.append(sqlExcute.excuteSql("derby","Jdbc",""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
    @RequestMapping("/derbyJdbcPs")
    @ResponseBody
    public String testPs() {
    	SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
    	StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>derby jdbc preparedStatement</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        String insertsql = "insert into test_user(id,name,birthday) values (?,?,current_timestamp)";
        String updatesql = "update test_user set name=? where id=1";
        /*
            String callsql = "{call proc_update(?)}";
        */
        String deletesql = "delete from test_user where id= ?";
        String selectsql = "select name,birthday from test_user where name like ?";
        try {
            Connection conn =sqlExcute.getConnPs("derby","Jdbc");

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

            preparedStatement = conn.prepareStatement(selectsql);
            preparedStatement.setString(1, "%ps%");
            //preparedStatement.setInt(2, 5);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                queryResult = rs.getString("name") + "- -" + rs.getString("birthday") + "<br>";
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else{
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + "<br>" + selectsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        }
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
