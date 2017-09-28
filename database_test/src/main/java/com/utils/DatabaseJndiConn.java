package com.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseJndiConn {
    public static synchronized Connection getConnection(String jndiName) throws Exception {
        try
        {
            Context ctx= new InitialContext(); //得到初始化下文
            Object obj=ctx.lookup(jndiName);//查找连接池
            DataSource ds= (DataSource) obj;//转换成DataSource
            return ds.getConnection();
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(NamingException e)
        {
            throw e;
        }
    }
    public static ServletContext getServletContext(){
    	WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
        ServletContext servletContext = webApplicationContext.getServletContext(); 
        return servletContext;
    }
}