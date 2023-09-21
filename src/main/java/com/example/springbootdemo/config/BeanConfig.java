package com.example.springbootdemo.config;

import com.example.springbootdemo.dto.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class BeanConfig {

    @Bean(name = "blo")
    public Customer customer1() {
        Customer customer = new Customer();
        customer.setId(11);
        customer.setName("customer 1");
        return customer;
    }

    @Bean(name = "alo")
    public Customer customer2() {
        Customer customer = new Customer();
        customer.setId(22);
        customer.setName("customer 2");
        return customer;
    }

//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        final String URL = "jdbc:mysql://localhost:3306/logicom_local";
//        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();
//        managerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        managerDataSource.setUrl(URL);
//        managerDataSource.setUsername("root");
//        managerDataSource.setPassword("12345678");
//        return managerDataSource;
//    }

}
