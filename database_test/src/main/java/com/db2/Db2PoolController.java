package com.db2;

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
public class Db2PoolController {
    @Resource
    JdbcTemplate db2c3p0jdbcTemplate;
    @Resource
    JdbcTemplate db2dbcpjdbcTemplate;
    @Resource
    JdbcTemplate db2proxooljdbcTemplate;

    @RequestMapping(value="db2C3p0")
    @ResponseBody
    public String c3p0test() {
        return sqlexecute(db2c3p0jdbcTemplate,"C3p0");
    }

    @RequestMapping(value="db2Dbcp")
    @ResponseBody
    public String dbcptest() {
        return sqlexecute(db2dbcpjdbcTemplate,"Dbcp");
    }

    @RequestMapping(value="db2Proxool")
    @ResponseBody
    public String proxooltest() {
       return sqlexecute(db2proxooljdbcTemplate,"Proxool");
    }

    public String sqlexecute(JdbcTemplate jdbcTemplate, String type){
    	String insertsql = "insert into test_user(id,name,birthday) values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'))";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        String deletesql = "delete from test_user where name like ?";
        String mulselctsql="select * from (select name,birthday from test_user where name like ?)";

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>db2 "+type+"</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";

        jdbcTemplate.update(deletesql, new Object[]{"%ps%"});
        jdbcTemplate.update(insertsql, new Object[]{500, "ps_PreparedStatement"});
        jdbcTemplate.update(updatesql, new Object[]{"update_ps"});
        jdbcTemplate.update(callsql, new Object[]{"ps"});
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
