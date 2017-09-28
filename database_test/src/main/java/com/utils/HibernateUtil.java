package com.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistryBuilder;
//import org.hibernate.service.ServiceRegistryBuilder;


/**
 * Created by admin on 2016/8/29.
 */
public class HibernateUtil {
    private final SessionFactory sessionFactory;
    public HibernateUtil(String configureFile) {
        try {
            //3版本
            sessionFactory = new Configuration().configure(configureFile).buildSessionFactory();    //.addResource("/hibernate.cfg.xml");
            //ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            //sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            //4版本的类
            //Configuration configuration = new Configuration().configure(configureFile);    //.addResource("/hibernate.cfg.xml");
            //ServiceRegistryBuilder builder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
            //sessionFactory = configuration.buildSessionFactory(builder.buildServiceRegistry());

            //5以后，没有该类了，取代的是import org.hibernate.boot.registry.StandardServiceRegistryBuilder
            //StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            //sessionFactory=configuration.buildSessionFactory(builder.getBootstrapServiceRegistry());
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}