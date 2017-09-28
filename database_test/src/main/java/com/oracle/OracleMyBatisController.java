package com.oracle;

import com.oracle.mybatis.TestUser;
import com.oracle.mybatis.TestUserMapper;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
@Controller
public class OracleMyBatisController {
	@RequestMapping("/oracleMybatis")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Oracle Mybabits</h2>");
        stringBuffer.append("delete from test_user where id = ?<br>");
        stringBuffer.append("insert into test_user (id, name, birthday, longcontent) values (?, ?, ?, ?)<br> ");
        MybatisUtil mybatisUtil=new MybatisUtil("oracleMybatis-config.xml");
        TestUser testUser=new TestUser();
        testUser.setId(new BigDecimal(100));
        testUser.setBirthday("2016");
        testUser.setName("mybatis");
        testUser.setLongcontent("aaaaaa");

        SqlSession sqlSession2=mybatisUtil.getSession();
        TestUserMapper testUserMapper2=sqlSession2.getMapper(TestUserMapper.class);
        testUserMapper2.deleteByPrimaryKey(new BigDecimal(100));
        sqlSession2.commit();

        SqlSession sqlSession=mybatisUtil.getSession();
        TestUserMapper testUserMapper=sqlSession.getMapper(TestUserMapper.class);
        testUserMapper.insert(testUser);
        sqlSession.commit();



        stringBuffer.append("<h3>save & delete execute finished</h3> </body></html>");
        return stringBuffer.toString();
    }


}
