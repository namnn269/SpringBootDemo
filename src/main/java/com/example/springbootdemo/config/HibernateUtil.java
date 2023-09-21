package com.example.springbootdemo.config;

import com.example.springbootdemo.entity.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;

    static {
        Properties properties = new Properties();
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.setProperty(Environment.URL, "jdbc:mysql://localhost:3306/demo_project");
        properties.setProperty(Environment.USER, "root");
        properties.setProperty(Environment.PASS, "12345678");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Employee.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);
    }
}
