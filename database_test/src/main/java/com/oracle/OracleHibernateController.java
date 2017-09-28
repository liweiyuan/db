package com.oracle;

import com.utils.HibernateUtil;
import com.vo.TestOracleUserVO;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class OracleHibernateController {
	@RequestMapping("/oracleHibernate")
    @ResponseBody
    public String test() {
		  StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append("<html><head><title></title></head><body><h2>oracle Hibernate</h2>");
//	        String queryResult = "<a style=\"color: red\">error</a>";
	        HibernateUtil hibernateUtil = new HibernateUtil("oracleHibernate.cfg.xml");
	       stringBuffer.append("<html><head><title></title></head><body><h2>Oracle Hibernate</h2>");
	        stringBuffer.append("insert into TEST_USER (BIRTHDAY, LONGCONTENT, NAME, ID) values (?, ?, ?, ?)<br>");
	        stringBuffer.append("select testuseren_.ID, testuseren_.BIRTHDAY as BIRTHDAY2_0_, testuseren_.LONGCONTENT as LONGCONT3_0_, testuseren_.NAME as NAME4_0_ from TEST_USER testuseren_ where testuseren_.ID=?<br>");
	        stringBuffer.append("delete from TEST_USER where ID=?<br>");
	        TestOracleUserVO userEntity = new TestOracleUserVO();
	        userEntity.setId(1001);
	        userEntity.setBirthday("2016");
	        userEntity.setName("hibernate");

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
