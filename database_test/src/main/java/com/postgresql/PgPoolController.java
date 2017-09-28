package com.postgresql;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2016/6/30.
 */
@Controller
public class PgPoolController {
    @Resource
    JdbcTemplate postgresqlc3p0jdbcTemplate;
    @Resource
    JdbcTemplate postgresqldbcpjdbcTemplate;
    @Resource
    JdbcTemplate postgresqlproxooljdbcTemplate;

    @RequestMapping(value="postgresqlC3p0")
    @ResponseBody
    public String c3p0test() {
        return sqlexecute(postgresqlc3p0jdbcTemplate,"C3p0");
    }

    @RequestMapping(value="postgresqlDbcp")
    @ResponseBody
    public String dbcptest() {
        return sqlexecute(postgresqldbcpjdbcTemplate,"Dbcp");
    }

    @RequestMapping(value="postgresqlProxool")
    @ResponseBody
    public String proxooltest() {
        System.out.print("ass");
       return sqlexecute(postgresqlproxooljdbcTemplate,"Proxool");
    }

    public String sqlexecute(JdbcTemplate jdbcTemplate, String type){
        String insertsql = "insert into test_user(id,name,birthday) values (?,?,current_timestamp)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update('ps')}";
        String deletesql = "delete from test_user where id= ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)as foo";

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>postgresql "+type+"</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";

        jdbcTemplate.update(deletesql, new Object[]{1});
        jdbcTemplate.update(insertsql, new Object[]{1, "PreparedStatement"});
        jdbcTemplate.update(updatesql, new Object[]{"%update_ps%"});
        
        SqlRowSet rs =jdbcTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
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
