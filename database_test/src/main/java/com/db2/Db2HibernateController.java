package com.db2;

import com.utils.HibernateUtil;
import com.vo.TestDb2UserVO;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class Db2HibernateController {
	@RequestMapping("/db2Hibernate")
    @ResponseBody
    public String test() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title></title></head><body><h2>db2 Hibernate</h2>");
//        String queryResult = "<a style=\"color: red\">error</a>";
        HibernateUtil hibernateUtil=new HibernateUtil("db2Hibernate.cfg.xml");
        stringBuffer.append("<html><head><title></title></head><body><h2>DB2 hibernate</h2>");
        stringBuffer.append("insert into DB2INST1.TEST_USER (BIRTHDAY, LONGCONTENT, NAME, ID) values (?, ?, ?, ?)<br>");
        stringBuffer.append("select testuseren_.ID, testuseren_.BIRTHDAY as BIRTHDAY2_0_, testuseren_.LONGCONTENT as LONGCONT3_0_, testuseren_.NAME as NAME4_0_ from DB2INST1.TEST_USER testuseren_ where testuseren_.ID=?<br>");
        stringBuffer.append("delete from DB2INST1.TEST_USER where ID=?<br>");
        TestDb2UserVO userEntity = new TestDb2UserVO();
        userEntity.setId(1001);
        userEntity.setBirthday("2016");
        userEntity.setName("hibernate");
        userEntity.setLongcontent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes());
        Session session3 = hibernateUtil.getSessionFactory().openSession();
        session3.beginTransaction();
        session3.delete(userEntity);
        session3.getTransaction().commit();

        Session session = hibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(userEntity);
        session.getTransaction().commit();
        
        //stringBuffer.append("<h3>save & delete execute finished</h3> </body></html>");
        stringBuffer.append("</body></html>");
        session.close();
        return stringBuffer.toString();
    }


}
