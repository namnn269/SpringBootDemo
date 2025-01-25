package com.example.springbootdemo.infrastructure.database.logicom;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.springbootdemo.infrastructure.database.logicom.repository",
        entityManagerFactoryRef = LogicomDBConfig.ENTITY_MANAGER_FACTORY,
        transactionManagerRef = LogicomDBConfig.TRANSACTION_MANAGER
)
public class LogicomDBConfig {

    public static final String DATA_SOURCE = "logicomDataSource";
    public static final String ENTITY_MANAGER_FACTORY = "logicomEntityManagerFactory";
    public static final String TRANSACTION_MANAGER = "logicomTransactionManager";

    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.logicom")
    @Bean(name = DATA_SOURCE)
    public DataSource logicomDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean logicomEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                              @Qualifier(DATA_SOURCE) DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.springbootdemo.infrastructure.database.logicom")
                .persistenceUnit("logicom")
                .build();
    }

    @Primary
    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager logicomTransactionManager(@Qualifier(ENTITY_MANAGER_FACTORY)
                                                                EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
