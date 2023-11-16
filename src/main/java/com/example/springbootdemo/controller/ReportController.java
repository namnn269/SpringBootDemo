package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/{format}")
    public ResponseEntity<?> report(@PathVariable("format") String format) throws JRException, FileNotFoundException {
        return new ResponseEntity<>(reportService.exportEmployeeReport(format), HttpStatus.OK);
    }

    @GetMapping(value = "/complexity")
    public ResponseEntity<?> report2(@RequestParam("format") String format) throws JRException, IOException {
        return new ResponseEntity<>(reportService.exportReport(), HttpStatus.OK);
    }
}
