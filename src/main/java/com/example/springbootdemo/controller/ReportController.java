package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping(value = "/complexity")
    public ResponseEntity<?> report2() throws JRException {
        return new ResponseEntity<>(reportService.exportReport(), HttpStatus.OK);
    }
}
