package com.example.springbootdemo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnotationDao {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AnnotationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void getJdbcInDao(int id) {
        System.out.println("in jdbc dao: " + Thread.currentThread().getName());
        String var = " SELECT * FROM m_material mm WHERE mm.record_id IN (:idList)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("idList", List.of(1000, 1023, 1056, 555, 1116));
        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(var, new MapSqlParameterSource(params));
        System.out.println(list);
    }
}
