package com.example.springbootdemo.repository;

import com.example.springbootdemo.infrastructure.database.logicom.entity.MMaterial;
import com.example.springbootdemo.infrastructure.database.logicom.repository.MMaterialRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MMaterialRepositoryTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36");

    @Autowired
    MMaterialRepository materialRepo;

    /**
     * use this method or annotation @ServiceConnection to connect to container mysql
     * otherwise it will connect to database configured in yaml
     */
//    @DynamicPropertySource
//    static void dynamicPropertySource(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//    }

    @Test
    void testContainer() {
        assertThat(mySQLContainer.isCreated()).isTrue();
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @Test
    @Sql(scripts = {"classpath:database/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void insertOne() {
        MMaterial entity = new MMaterial(null, "01", "10", "S", "M", "n", "m", "a", "a", 1, 1, 1, "", 1, new Date(), 1, new Date(), 0);
        materialRepo.save(entity);
        List<MMaterial> list = materialRepo.findAll();
        assertThat(list).hasSize(1);
    }

    @Test
    @Sql(scripts = {"classpath:database/schema.sql", "classpath:database/import_data.sql"})
    void testListRepo() {
        List<MMaterial> list = materialRepo.findAll();
        assertThat(list).hasSize(10);
    }
}