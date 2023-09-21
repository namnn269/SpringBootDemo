package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.MaterialDto_2;
import com.example.springbootdemo.entity.Employee;
import com.example.springbootdemo.repository.MMaterialRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jdbc")
public class JdbcController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    MMaterialRepository materialRepo;

    @Autowired
    EntityManager entityManager;

    @GetMapping()
    public ResponseEntity<?> customers() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM categories WHERE name LIKE ?", "%\\%ca\\%%");
        jdbcTemplate.queryForObject("", new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee employee = new Employee();
                employee.setName(rs.getString(1));
                employee.setId(rs.getInt(2));
                employee.setNote(rs.getString(3));
                employee.setBirthDay(rs.getDate(4));
                return employee;
            }
        });

        /////////////////////////////////
        Map<String, Object> params = new HashMap<>();
        params.put("name", "%\\%ca\\%t%");
        List<Map<String, Object>> list1 = namedParameterJdbcTemplate.queryForList("SELECT * FROM categories WHERE name LIKE :name", params);

        return new ResponseEntity<>(list1, HttpStatus.OK);
    }

    @GetMapping(value = "/list-map")
    public Object getMap() {
//        String sql = "SELECT mm.material_code, mm.material_name, mm.supplier_code " +
//                " FROM m_material mm " +
//                " WHERE mm.material_code LIKE ? " +
//                " LIMIT ?";
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, "%M001%", 5);
        String sql = "SELECT mm.material_code, mm.material_name, mm.supplier_code " +
                " FROM m_material mm " +
                " WHERE mm.material_code LIKE :materialCode " +
                " LIMIT :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("materialCode", "%M001%");
        params.put("limit", 5);
        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, params);
        System.out.println(list);
        return list;
    }

    @GetMapping(value = "/list-row-mapper")
    public Object getListBean() {
//        String sql = "SELECT mm.material_code, mm.material_name, mm.supplier_code " +
//                " FROM m_material mm " +
//                " WHERE mm.material_code LIKE ? " +
//                " LIMIT ?";
//        List<MaterialDto_2> list = jdbcTemplate.query
//                (sql, BeanPropertyRowMapper.newInstance(MaterialDto_2.class), "%M001%", 5);
        String sql = "SELECT mm.material_code, mm.material_name, mm.supplier_code " +
                " FROM m_material mm " +
                " WHERE mm.material_code LIKE :materialCode " +
                " LIMIT :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("materialCode", "%M002%");
        params.put("limit", 5);
        List<MaterialDto_2> list = namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(MaterialDto_2.class));
        System.out.println(list);
        return list;
    }

    @GetMapping(value = "/native-jpa")
    @Transactional
    public Object nativeQuery(@RequestBody List<Integer> ids) throws InterruptedException {

        String sql = " SELECT mm.material_code, mm.material_name, mm.supplier_code " +
                "             FROM m_material mm " +
                "             WHERE mm.record_id IN (:ids)";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter("ids", ids.isEmpty() ? null : ids);
        List<Map<String, Object>> resultList = nativeQuery.getResultList();
        return resultList;

//        Object materials = materialRepo.selectMaterialNativeQuery(ids);
//        Thread.sleep(5000);
//        return materials;

//        String sql = "SELECT mm.material_code, mm.material_name, mm.supplier_code " +
//                " FROM m_material mm " +
//                " WHERE mm.record_id IN (:ids) ";
//        Map<String, Object> params = new HashMap<>();
//        params.put("ids", ids.isEmpty() ? null : ids);
//        List<MaterialDto_2> list = namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(MaterialDto_2.class));

//        return list;

    }

    @GetMapping(value = "/native-order-null")
    @Transactional
    public Object queryWithOrderNull(
            @RequestParam(required = false, defaultValue = "AsC") String order,
            @RequestBody List<Integer> ids) {
        String sql = " SELECT mm.record_id, mm.material_code, mm.material_name, mm.supplier_code " +
                " FRom m_material mm " +
                " WHERE mm.record_id IN (:ids) " +
                " ORDER BY NULL, mm.record_id " + order;
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter("ids", ids.isEmpty() ? null : ids);
        List<Map<String, Object>> resultList = nativeQuery.getResultList();
        return resultList;
    }

    @GetMapping(value = "/jpa-order-null")
    @Transactional
    public Object jpaWithOrderNull(
            @RequestParam(required = false, defaultValue = "ASC") String order,
            @RequestBody List<Integer> ids) {
        String sql = " SELECT mm.recordId, mm.materialCode, mm.materialName, mm.supplierCode " +
                " FROM MMaterial mm " +
                " WHERE mm.recordId IN (:ids) " +
                " ORDER BY NULL, mm.recordId " + order;
        Query nativeQuery = entityManager.createQuery(sql);
        nativeQuery.setParameter("ids", ids.isEmpty() ? null : ids);
        List<Map<String, Object>> resultList = nativeQuery.getResultList();
        return resultList;
    }

    @GetMapping("/row-mapper")
    public Object jdbcWithRowMapper() {
        String sql = "SELECT mm.material_code AS materialCode, mm.material_name, mm.supplier_code " +
                " FROM m_material mm " +
                " WHERE mm.material_code LIKE :materialCode " +
                " LIMIT :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("materialCode", "%M00%");
        params.put("limit", 5);

        List<MaterialDto_2> list = namedParameterJdbcTemplate.query(sql, params, new MaterialDto2RowMapper());
        return list;
    }

    static class MaterialDto2RowMapper implements RowMapper<MaterialDto_2> {

        @Override
        public MaterialDto_2 mapRow(ResultSet rs, int rowNum) throws SQLException {
//            String materialCode = rs.getString("materialCode");
//            String materialName = rs.getString("material_name");
//            String supplierCode = rs.getString("supplier_code");
            String materialCode = rs.getString(1);
            String materialName = rs.getString(2);
            String supplierCode = rs.getString(3);
            if (materialCode.equals("MATERM0001")) materialCode = "MATERM0001_______";
            return MaterialDto_2.builder()
                    .materialName(materialName)
                    .materialCode(materialCode)
                    .supplierCode(supplierCode)
                    .build();
        }
    }

}


