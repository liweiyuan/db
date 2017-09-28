package com.sqlserver;

import com.utils.HibernateUtil;
import com.vo.TestSqlserverUserVO;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SqlserverHibernateController {
	@RequestMapping("/sqlserverHibernate")
    @ResponseBody
    public String test() {
		  StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append("<html><head><title></title></head><body><h2>sqlserver Hibernate</h2>");
//	        String queryResult = "<a style=\"color: red\">error</a>";
	        HibernateUtil hibernateUtil = new HibernateUtil("sqlserverHibernate.cfg.xml");
	        stringBuffer.append("<html><head><title></title></head><body><h2>sqlserver Hibernate</h2>");
	        stringBuffer.append("insert into dbo.test_user (ID, birthday, name, longcontent) values (?,'1984-06-12', ?,?)<br>");
	        stringBuffer.append("select test_user.ID, test_user.birthday as BIRTHDAY2_0_, test_user.longcontent as LONGCONT3_0_, test_user.NAME as name from dbo.test_user test_user where test_user.ID=?<br>");
	        stringBuffer.append("delete from dbo.test_user where ID=?<br>");
	        TestSqlserverUserVO userEntity = new TestSqlserverUserVO();
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
