package com.example.springbootdemo.main;

import com.example.springbootdemo.service.ReportService;
import net.sf.jasperreports.engine.JRException;

public class ReportStarter {

    public static void main(String[] args) throws JRException {

        final ReportService reportService = new ReportService();
        reportService.exportReport();

    }
}
