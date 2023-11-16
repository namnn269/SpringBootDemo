package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.ReportDto;
import com.example.springbootdemo.entity.Employee;
import com.example.springbootdemo.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private EmployeeRepository employeeRepo;

    final String path = "C:\\Users\\Nam\\Desktop";

    public String exportEmployeeReport(String format) throws FileNotFoundException, JRException {


        List<Employee> employees = employeeRepo.findAll();
        File file = ResourceUtils.getFile("classpath:reports/employees.jrxml");
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

    public Object exportReport() throws IOException, JRException {
        String s = "\\lol.pdf";
        JasperReport report = JasperCompileManager.compileReport(new ClassPathResource("reports/main_report.jrxml").getInputStream());
        JasperReport subReport = JasperCompileManager.compileReport(new ClassPathResource("reports/sub_report.jrxml").getInputStream());

        HashMap<String, Object> params = new HashMap<>();
        params.put("supplierName", "S00091");
        params.put("listDataSource", new JRBeanCollectionDataSource(getReportDtoList("list 1: ")));
        params.put("listDataSource2", new JRBeanCollectionDataSource(getReportDtoList("list 2: ")));
        params.put("pieChartDataSource", new JRBeanCollectionDataSource(getReportDtoList("pie: ")));

        params.put("subReport", subReport);
        params.put("subReportDataSource", new JRBeanCollectionDataSource(getReportDtoList("supReport: ")));
        params.put("subReportParams", getSubReportParams());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getReportDtoList("main: "));


        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + s);
        return path + s;
    }

    private Map<String, Object> getSubReportParams() {
        Map<String, Object> subReportParams = new HashMap<>();
        subReportParams.put("localDateTime", LocalDateTime.now().withNano(0));

        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("userName", "namnn");
        mapParams.put("email", "namnn@gmail.com");
        subReportParams.put("mapParams", mapParams);
        return subReportParams;
    }

    private List<Map<String, Object>> getDataList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("name", "name" + i);
            map.put("age", 10 + i);
            map.put("dob", LocalDate.of(2022, 1, i));
            list.add(map);
        }
        return list;
    }

    private List<ReportDto> getReportDtoList(String name) {
        List<ReportDto> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            list.add(new ReportDto(i, name + i, 20 + i, LocalDate.of(2024, 10, i)));
        }
        return list;
    }
}
