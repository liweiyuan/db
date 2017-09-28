package com.sqlserver;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SqlserverJdbcTemplateController{
    @Resource
    private JdbcTemplate SqlserverTemplate;

    String insertsql = "insert into test_user values (?,'1984-06-12', ?,?)";
    String updatesql = "update test_user set name=? where id=1";
    String callsql = "{call proc_update(?)}";
    String deletesql = "delete from test_user where id= ?";
//    String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";
    String mulselctsql="select tmp.* from (select name,birthday from test_user where name like ?) as tmp";
    @RequestMapping("/sqlserverJdbcTemplate")
    @ResponseBody
    public String test(HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Sqlserver JdbcTemplate</h2>");
                String queryResult = "<a style=\"color: red\">norecord</a>";

        SqlserverTemplate.update(deletesql, new Object[]{CommonUtils.insertId});

        SqlserverTemplate.update(insertsql, new Object[]{"PreparedStatement",CommonUtils.insertId,null});
        SqlserverTemplate.update(updatesql, new Object[]{"%update_ps%"});
        SqlserverTemplate.update(callsql, new Object[]{"ps"});
        SqlRowSet rs = SqlserverTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
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
