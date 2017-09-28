package com.postgresql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PgJdbcTemplateController {
    @Resource
    private JdbcTemplate PostgresqlTemplate;

    String insertsql = "insert into test_user(id,name,birthday) values (?,?,current_timestamp)";
    String updatesql = "update test_user set name=? where id=1";
    String callsql = "{call proc_update('ps')}";
    String deletesql = "delete from test_user where id= ?";
    String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";

    @RequestMapping("/postgresqlJdbcTemplate")
    @ResponseBody
    public String test(HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>postgresql JdbcTemplate</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        PostgresqlTemplate.update(deletesql, new Object[]{1});
        PostgresqlTemplate.update(insertsql, new Object[]{1, "PreparedStatement"});
        PostgresqlTemplate.update(updatesql, new Object[]{"%update_ps%"});
/*
        PostgresqlTemplate.update(callsql);
*/
        SqlRowSet rs =PostgresqlTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
        while (rs.next()) {
            // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
            queryResult =rs.getString(1) + " - " + rs.getString(2) + "<br>";
        }

        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }

}
