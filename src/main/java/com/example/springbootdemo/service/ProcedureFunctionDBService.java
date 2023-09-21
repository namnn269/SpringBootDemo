package com.example.springbootdemo.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@AllArgsConstructor
public class ProcedureFunctionDBService {

    private static final String URL = "jdbc:mysql://localhost:3306/logicom_local";
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initial() {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "12345678");
        dataSource = new DriverManagerDataSource(URL, properties);
    }

    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "12345678");
        return DriverManager.getConnection(URL, properties);
    }

    public Object callProcedureIndex() {
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement call = connection.prepareCall("{CALL ProcedureWithReturn(?, ?, ?, ?)}");
            call.setInt(1, 1);
            call.setInt(3, 4);
            call.setInt(4, 2);
            call.registerOutParameter(2, Types.INTEGER);
            call.registerOutParameter(3, Types.INTEGER);
            call.registerOutParameter(4, Types.INTEGER);
            call.execute();
            int int2 = call.getInt(2);
            int int3 = call.getInt(3);
            int int4 = call.getInt(4);
            System.out.println(int2);
            System.out.println(int3);
            System.out.println(int4);
            return List.of(int2, int3, int4);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object callFunctionIndex() {
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement call = connection.prepareCall("{? = CALL CalculateDiscount(?, ?)}");
            call.setString(2, "M0001");
            call.setInt(3, 5);
            call.registerOutParameter(1, Types.INTEGER);
            call.execute();
            int result = call.getInt(1);
            System.out.println(result);
            return List.of(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object callProcedureWithCreatorFactory() {
        String sqlCall = "{CALL ProcedureWithReturn(?, ?, ?, ?)}";
        CallableStatementCreatorFactory factory = new CallableStatementCreatorFactory(sqlCall);
        factory.addParameter(new SqlParameter("in_var1", Types.INTEGER));
        factory.addParameter(new SqlOutParameter("out_var", Types.INTEGER));
        factory.addParameter(new SqlInOutParameter("inout_var", Types.INTEGER));
        factory.addParameter(new SqlInOutParameter("inout_var2", Types.INTEGER));
        Map<String, Object> params = new HashMap<>();
        params.put("in_var1", 1);
        params.put("inout_var", 4);
        params.put("inout_var2", 2);
        try {
            CallableStatementCreator callableStatementCreator = factory.newCallableStatementCreator(params);
            CallableStatement call = callableStatementCreator.createCallableStatement(dataSource.getConnection());
            call.execute();
            int int2 = call.getInt("out_var");
            int int3 = call.getInt("inout_var");
            int int4 = call.getInt("inout_var2");
            List<Integer> list = List.of(int2, int3, int4);
            System.out.println(list);
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object callFunctionWithJdbcTemplate() {
        String sql = "SELECT CalculateDiscount(?, ?)";
        Object[] params = {"M0001", 6};
        int[] types = {Types.CHAR, Types.INTEGER};
        Integer integer = jdbcTemplate.queryForObject(sql, params, types, Integer.class);
//        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, "M0001", 6);
        System.out.println(integer);
        return integer;
    }

    public Object callFunctionWithSimpleJdbcCall() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource);
        jdbcCall.withFunctionName("CalculateDiscount");
        BigDecimal price = jdbcCall.executeFunction(BigDecimal.class, "M0001", 7);
        System.out.println(price);
        return price;
    }

    public Object callProcedureWithSimpleJdbcCall() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource);
        jdbcCall.withProcedureName("ProcedureWithReturn");
        Map<String, Object> params = new HashMap<>();
        params.put("in_var", 1);
        params.put("inout_var", 4);
        params.put("inout_var2", 2);
        Map<String, Object> result = jdbcCall.execute(params);
        System.out.println(result);
        return result;
    }

}
