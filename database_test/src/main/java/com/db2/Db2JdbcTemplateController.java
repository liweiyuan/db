package com.db2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class Db2JdbcTemplateController{
    @Resource
    private JdbcTemplate Db2Template;

    String insertsql = "insert into test_user(id,name,birthday) values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'))";
    String updatesql = "update test_user set name=? where id=1";
    String callsql = "{call proc_update(?)}";
    String deletesql = "delete from test_user where name like ?";
    String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";

    @RequestMapping("/db2JdbcTemplate")
    @ResponseBody
    public String test(HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Db2 JdbcTemplate</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";

        Db2Template.update(deletesql, new Object[]{"%ps%"});
        Db2Template.update(insertsql, new Object[]{1, "PreparedStatement"});
        Db2Template.update(updatesql, new Object[]{"%update_ps%"});
        Db2Template.update(callsql, new Object[]{"ps"});
        SqlRowSet rs = Db2Template.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
        while (rs.next()) {
            // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
            queryResult=rs.getString(1) + " - " + rs.getString(2) + "<br>";
        }

        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

}
