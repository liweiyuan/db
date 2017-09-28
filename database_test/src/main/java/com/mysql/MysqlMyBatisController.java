package com.mysql;

import com.mysql.mybatis.TestUser;
import com.mysql.mybatis.TestUserMapper;
import com.mysql.mybatis.dao.User;
import com.mysql.mybatis.service.UserService;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MysqlMyBatisController {
	@RequestMapping("/mysqlMybatis")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Mysql Mybabits</h2>");
        stringBuffer.append("delete from test_user where id = ?<br>");
        stringBuffer.append("insert into test_user (id, name, birthday, longcontent) values (?, ?, ?, ?)<br> ");
        stringBuffer.append("update test_user set name=? where id=100 <br> ");
        stringBuffer.append("select * from test_user where id=? <br> ");
        MybatisUtil mybatisUtil=new MybatisUtil("mysqlMybatis-config.xml");

        SqlSession sqlSession2=mybatisUtil.getSession();
        TestUserMapper testUserMapper2=sqlSession2.getMapper(TestUserMapper.class);
        testUserMapper2.deleteByPrimaryKey(100);
        sqlSession2.commit();
        TestUser testUser=new TestUser();
        testUser.setId(100);
        testUser.setBirthday("2016");
        testUser.setName("mybatis");
        testUser.setLongcontent("aaaaaa");

        SqlSession sqlSession=mybatisUtil.getSession();
        TestUserMapper testUserMapper=sqlSession.getMapper(TestUserMapper.class);
        testUserMapper.insert(testUser);
        sqlSession.commit();

        SqlSession sqlSession3 = mybatisUtil.getSession();
        TestUserMapper testUserMapper3 = sqlSession3.getMapper(TestUserMapper.class);
        TestUser testUser1 = new TestUser();
        testUser1.setId(100);
        testUser1.setName("java");
        testUserMapper3.updateByPrimaryKey(testUser1);
        sqlSession3.commit();


        //查询
        SqlSession sqlSession4 = mybatisUtil.getSession();
        TestUserMapper testUserMapper4 = sqlSession4.getMapper(TestUserMapper.class);
        TestUser testUser3=testUserMapper4.selectByPrimaryKey(100);

        stringBuffer.append(testUser3+"<br>");

        stringBuffer.append("<h3>save & delete execute finished</h3> </body></html>");
        return stringBuffer.toString();
    }


    //@Resource
    private UserService userService;

    //mybatis测试代码
    @RequestMapping("/mysqlMybatis1")
    @ResponseBody
    public String getParams(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>Mysql Mybabits</h2>");
        stringBuffer.append("delete from test_user where id = ?<br>");
        stringBuffer.append("insert into test_user (id, name, birthday, longcontent) values (?, ?, ?, ?)<br> ");
        User user=userService.getUserById(1);
        System.out.println(user);
        return stringBuffer.toString();
    }

}
