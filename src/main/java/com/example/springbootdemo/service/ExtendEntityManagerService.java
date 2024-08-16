package com.example.springbootdemo.service;

import com.example.springbootdemo.controller.ExtendEntityManagerController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ExtendEntityManagerService {
    private final EntityManager entityManager;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExtendEntityManagerService(EntityManager entityManager,
                                      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.entityManager = entityManager;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Object useEntityManager() {
        String sql = " SELECT m.materialCode FROM MMaterial m WHERE m.recordId = :id ";
        TypedQuery<String> query = entityManager.createQuery(sql, String.class);
        query.setParameter("id", 5);
        return query.getResultList();
    }

    public Object useNamedParameterJdbcTemplate() {
        String sql = " SELECT m.material_code FROM m_material m WHERE m.record_id = :id ";
        RowMapper<String> rowMapper = (rs, rowNum) -> rs.getString("material_code");
        return namedParameterJdbcTemplate.query(sql, Map.of("id", 5), rowMapper);
    }

}
