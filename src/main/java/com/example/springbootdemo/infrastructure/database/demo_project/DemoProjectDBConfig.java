package com.example.springbootdemo.infrastructure.database.demo_project;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static com.example.springbootdemo.infrastructure.database.demo_project.DemoProjectDBConfig.ENTITY_MANAGER_FACTORY;
import static com.example.springbootdemo.infrastructure.database.demo_project.DemoProjectDBConfig.TRANSACTION_MANAGER;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.springbootdemo.infrastructure.database.demo_project.repository",
        entityManagerFactoryRef = ENTITY_MANAGER_FACTORY,
        transactionManagerRef = TRANSACTION_MANAGER
)
public class DemoProjectDBConfig {

    public static final String DATA_SOURCE = "demoDataSource";
    public static final String ENTITY_MANAGER_FACTORY = "demoEntityManagerFactory";
    public static final String TRANSACTION_MANAGER = "demoTransactionManager";

    @Bean(name = DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.demo-project")
    public DataSource demoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean demoEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier(DATA_SOURCE) DataSource dataSource) {

        return builder.dataSource(dataSource)
                .persistenceUnit("demo")
                .packages("com.example.springbootdemo.infrastructure.database.demo_project")
                .build();
    }

    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager demoTransactionManager(@Qualifier(ENTITY_MANAGER_FACTORY)
                                                        EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
