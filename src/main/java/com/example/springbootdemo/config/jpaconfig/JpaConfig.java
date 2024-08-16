package com.example.springbootdemo.config.jpaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "myAuditorAware")
public class JpaConfig {

    @Bean(name = "myAuditorAware")
    public AuditorAware<Integer> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Bean
    @Order(1)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplateWrapper(dataSource);
    }
//
//    @Bean

    public static class NamedParameterJdbcTemplateWrapper extends NamedParameterJdbcTemplate {

        public NamedParameterJdbcTemplateWrapper(DataSource dataSource) {
            super(dataSource);
        }

        @Override
        public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
                throws DataAccessException {
            System.out.println("in NamedParameterJdbcTemplateWrapper");
            System.out.println("class caller: " + Thread.currentThread().getStackTrace()[2].getClassName());
            return query(sql, new MapSqlParameterSource(paramMap), rowMapper);
        }
    }
}
