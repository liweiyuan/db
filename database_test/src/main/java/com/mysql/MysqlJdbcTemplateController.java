package com.mysql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
@Controller
public class MysqlJdbcTemplateController {

    @Resource
    private JdbcTemplate MysqlTemplate;

    String insertsql = "insert into test_user(id,name,birthday) values (?,?,now())";
    String updatesql = "update test_user set name=? where id=1";
    String callsql = "{call proc_update(?)}";
    String deletesql = "delete from test_user where name like ?";
    String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";
    @RequestMapping("/mysqlJdbcTemplate")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>mysql JdbcTemplate</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        MysqlTemplate.update(deletesql, new Object[]{"%ps%"});
        MysqlTemplate.update(insertsql, new Object[]{10, "ps_PreparedStatement"});
        MysqlTemplate.update(updatesql, new Object[]{"%update_ps%"});
        MysqlTemplate.update(callsql, new Object[]{"ps"});
        SqlRowSet rs =MysqlTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
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
