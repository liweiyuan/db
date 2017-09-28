package com.dm;

import com.utils.SqlExcuteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller
public class DmJdbcController {
    @RequestMapping("/dmJdbc")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        SqlExcuteUtil sqlExcute = new SqlExcuteUtil();
        stringBuffer.delete(0, stringBuffer.length());
		stringBuffer.append("<html><head><title></title></head><body><h2>Dm JDBC</h2>");

		try {
			stringBuffer.append(sqlExcute.excuteSql("dm","Jdbc",""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stringBuffer.append(" </body></html>");
        return stringBuffer.toString();
    }
}
