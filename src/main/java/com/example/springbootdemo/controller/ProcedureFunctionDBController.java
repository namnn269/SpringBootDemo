package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.ProcedureFunctionDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procedure-function-db")
@RequiredArgsConstructor
public class ProcedureFunctionDBController {
    final private ProcedureFunctionDBService procedureFunctionDBService;

    @GetMapping(value = "procedure-index")
    public Object procedureIndex() {
        return procedureFunctionDBService.callProcedureIndex();
    }

    @GetMapping(value = "function-index")
    public Object functionIndex() {
        return procedureFunctionDBService.callFunctionIndex();
    }

    @GetMapping(value = "procedure-creator-factory")
    public Object procedureIndexJdbcTemplate() {
        return procedureFunctionDBService.callProcedureWithCreatorFactory();
    }

    @GetMapping(value = "function-creator-factory")
    public Object functionIndexJdbcTemplate() {
        return procedureFunctionDBService.callFunctionWithJdbcTemplate();
    }

    @GetMapping(value = "procedure-simple-jdbc-call")
    public Object procedureWithSimpleJdbcCall() {
        return procedureFunctionDBService.callProcedureWithSimpleJdbcCall();
    }

    @GetMapping(value = "function-simple-jdbc-call")
    public Object functionWithSimpleJdbcCall() {
        return procedureFunctionDBService.callFunctionWithSimpleJdbcCall();
    }
}
