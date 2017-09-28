package com.sqlserver;

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
public class SqlserverPoolController {
    @Resource
    JdbcTemplate sqlserverc3p0jdbcTemplate;
    @Resource
    JdbcTemplate sqlserverdbcpjdbcTemplate;
    @Resource
    JdbcTemplate sqlserverproxooljdbcTemplate;

    @RequestMapping(value="sqlserverC3p0")
    @ResponseBody
    public String c3p0test() {
        return sqlexecute(sqlserverc3p0jdbcTemplate,"C3p0");
    }

    @RequestMapping(value="sqlserverDbcp")
    @ResponseBody
    public String dbcptest() {
        return sqlexecute(sqlserverdbcpjdbcTemplate,"Dbcp");
    }

    @RequestMapping(value="sqlserverProxool")
    @ResponseBody
    public String proxooltest() {
       return sqlexecute(sqlserverproxooljdbcTemplate,"Proxool");
    }

    public String sqlexecute(JdbcTemplate jdbcTemplate, String type){
        String insertsql = "insert into test_user values (?,'1984-06-12', ?,?)";
        String updatesql = "update test_user set name=? where id=1";
        String callsql = "{call proc_update(?)}";
        String deletesql = "delete from test_user where id= ?";
        String mulselctsql="select tmp.* from (select name,birthday from test_user where name like ?) as tmp";

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>sqlserver "+type+"</h2>");
        String queryResult = "<a style=\"color: red\">norecord</a>";
        jdbcTemplate.update(deletesql, new Object[]{CommonUtils.insertId});
        jdbcTemplate.update(insertsql, new Object[]{"PreparedStatement",CommonUtils.insertId,null});
        jdbcTemplate.update(updatesql, new Object[]{"%update_ps%"});
        jdbcTemplate.update(callsql, new Object[]{"ps"});
        SqlRowSet rs =jdbcTemplate.queryForRowSet(mulselctsql, new Object[]{"%ps%"});
        while (rs.next()) {
            // stringBuffer.append(rs.getString("name")+"- -"+rs.getString("birthday"));
            queryResult=rs.getString(1) + " - " + rs.getString(2) + "<br>";
        }
        System.out.println("**************"+queryResult);
        if (queryResult.contains("norecord")) {
            stringBuffer.append(queryResult);
        } else
            stringBuffer.append(insertsql + "<br>" + updatesql + "<br>" + callsql + "<br>" + mulselctsql + "<br>" + deletesql + "<br>查询结果：<br>" + queryResult);
        stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
