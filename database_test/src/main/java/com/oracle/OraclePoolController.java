package com.oracle;

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
public class OraclePoolController {
    @Resource
    JdbcTemplate oraclec3p0jdbcTemplate;
    @Resource
    JdbcTemplate oracledbcpjdbcTemplate;
    @Resource
    JdbcTemplate oracleproxooljdbcTemplate;

    @RequestMapping(value = "oracleC3p0")
    @ResponseBody
    public String c3p0test() {

        return sqlexecute(oraclec3p0jdbcTemplate, "C3p0");
    }

    @RequestMapping(value = "oracleDbcp")
    @ResponseBody
    public String dbcptest() {
        return sqlexecute(oracledbcpjdbcTemplate, "Dbcp");
    }

    @RequestMapping(value = "oracleProxool")
    @ResponseBody
    public String proxooltest() {
        return sqlexecute(oracleproxooljdbcTemplate, "Proxool");
    }

    public String sqlexecute(JdbcTemplate jdbcTemplate, String type) {
        String insertsql = "insert into test_user(id,name,birthday) values (?,?,to_char(sysdate,'YYYY-MM-DD HH24:MI:SS'))";
        String updatesql = "update test_user set name=? where id=10";
        //String callsql = "{call proc_update(?)}";
        String deletesql = "delete from test_user where id= ?";
        String mulselctsql = "select * from (select name,birthday from test_user where name like ?)";

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>oracle " + type + "</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        jdbcTemplate.update(deletesql, new Object[]{10});
        jdbcTemplate.update(insertsql, new Object[]{10,"ps_PreparedStatement"});
        jdbcTemplate.update(updatesql, new Object[]{"%update_ps%"});
        //jdbcTemplate.update(callsql, new Object[]{"call"});

        SqlRowSet rs = jdbcTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
        while (rs.next()) {
            // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
            queryResult = rs.getString(1) + " - " + rs.getString(2) + "<br>";
        }
        //System.out.println("**************" + queryResult);
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" /*+ callsql*/ + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
