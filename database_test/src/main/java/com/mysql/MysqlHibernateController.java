package com.mysql;

import com.utils.HibernateUtil;
import com.vo.TestMysqlUserVO;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MysqlHibernateController {
	@RequestMapping("/mysqlHibernate")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>mysql Hibernate</h2>");
//        String queryResult = "<a style=\"color: red\">error</a>";
        HibernateUtil hibernateUtil = new HibernateUtil("mysqlHibernate.cfg.xml");
      
        stringBuffer.append("insert into javatest.test_user (birthday, longcontent, name, id) values (?, ?, ?, ?)<br>");
        stringBuffer.append("select testuseren_.id, testuseren_.birthday as birthday2_0_, testuseren_.longcontent as longcont3_0_, testuseren_.name as name4_0_ from javatest.test_user testuseren_ where testuseren_.id=?<br>");
        stringBuffer.append("delete from javatest.test_user where id=?<br>");
        TestMysqlUserVO userEntity = new TestMysqlUserVO();
        userEntity.setId(1001);
        userEntity.setBirthday("2016");
        userEntity.setName("hibernate");
        userEntity.setLongcontent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Session session2 = hibernateUtil.getSessionFactory().openSession();
        session2.beginTransaction();
        session2.delete(userEntity);
        session2.getTransaction().commit();

        Session session = hibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(userEntity);
        session.getTransaction().commit();

        stringBuffer.append("<h3>save & delete execute finished</h3> </body></html>");
        session.close();
        return stringBuffer.toString();
    }


}
