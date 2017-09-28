package com.postgresql;

import com.utils.HibernateUtil;
import com.vo.TestPgUserVO;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PgHibernateController {
	@RequestMapping("/postgresqlHibernate")
    @ResponseBody
    public String test() {
		 HibernateUtil hibernateUtil=new HibernateUtil("postgresqlHibernate.cfg.xml");
         StringBuffer stringBuffer=new StringBuffer();
	     stringBuffer.append("<html><head><title></title></head><body><h2>Postgresql hibernate</h2>");
	     stringBuffer.append("insert into javatest.public.test_user (birthday, longcontent, name, id) values (?, ?, ?, ?)<br>");
	     stringBuffer.append("select testuseren_.id, testuseren_.birthday as birthday2_0_, testuseren_.longcontent as longcont3_0_, testuseren_.name as name4_0_ from javatest.public.test_user testuseren_ where testuseren_.id=?<br>");
	     stringBuffer.append("delete from javatest.public.test_user where id=?");
	     TestPgUserVO userEntity = new TestPgUserVO();
	     userEntity.setId(2001);
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
