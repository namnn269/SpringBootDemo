package com.example.springbootdemo.controller;

import com.example.springbootdemo.infrastructure.database.logicom.repository.MMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/escape-char")
public class EscapeCharController {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    MMaterialRepository materialRepository;

    @GetMapping
    public Object nativeQuery(@RequestParam String param) {
//        param = escapeChar(param);
        String sql = " SELECT mm.material_code AS materialCodeNamedJdbc, " +
                " mm.material_name AS materialNameNamedJdbc " +
                " FROM m_material mm " +
                " WHERE (mm.material_name) LIKE :param ";
        List<Map<String, Object>> mapList = namedParameterJdbcTemplate
                .queryForList(sql, Map.of("param", escapeChar(param)));
        return Map.of(
                "native", materialRepository.nativeQuery(escapeChar(param)),
                "jpa", materialRepository.jpaQuery(param),
                "named",mapList);
    }

    private String escapeChar(String s) {
        return s.replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\")
                .replaceAll("\\\\", "\\\\\\\\") // namedParam and native must replace this, jpql not need
                .replaceAll("%", "\\\\%")
                .replaceAll("_", "\\\\_");
    }

    public static void main(String[] args) {
        System.out.println("'poo".replaceAll("'", "\\\\'"));
        System.out.println("\\".replaceAll("\\\\", "\\\\\\\\\\\\\\\\"));
    }
}
