package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.Employee;
import com.example.springbootdemo.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportService {

    @Autowired
    private EmployeeRepository employeeRepo;

    public String exportEmployeeReport(String format) throws FileNotFoundException, JRException {

        String path = "C:\\Users\\Nam\\Desktop";

        List<Employee> employees = employeeRepo.findAll();
        File file = ResourceUtils.getFile("classpath:employees.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lol", "lol");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, data);
        if (format.equals("html"))
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
        else
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
        return "path: " + path;
    }

}
