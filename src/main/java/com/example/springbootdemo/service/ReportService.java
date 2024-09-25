package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.ReportDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    static final String PATH = "/Users/namnn/Desktop";
    static final String IMAGE_PATH = "/image/image_2.png";
    static final String JASPER_PATH = "/reports/my_report.jrxml";
    static final String SUB_JASPER_PATH = "/reports/my_subreport.jrxml";

    public Object exportReport() throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(JASPER_PATH));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, getParameters(), getJrDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, PATH + "/report.pdf");
        return "===> export pdf OK";
    }

    private Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "My Jasper Report");
        params.put("date_time", "date_time format");
        params.put("map_param", new HashMap<>(Map.of("name", "MAIN REPORT")));
        params.put("image", getImageInput());
        params.putAll(getParamsForList());
        params.putAll(getParamsForCharts());
        params.putAll(getParamsForSubReport());
        return params;
    }

    private byte[] getImageByteArr() {
        try (InputStream imageStream = getClass().getResourceAsStream(IMAGE_PATH)) {
            return imageStream != null ? imageStream.readAllBytes() : new byte[]{};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ByteArrayInputStream getImageInput() {
        return new ByteArrayInputStream(getImageByteArr());
    }

    private JRDataSource getJrDataSource() {
        List<ReportDto> data = new ArrayList<>();
        data.add(new ReportDto("group_1", 1, "customer 1", "customer 1_1", "111"));
        data.add(new ReportDto("group_1", 1, "customer 2", "customer 2_2", "222"));
        data.add(new ReportDto("group_2", 1, "customer 3", "customer 3_3", "333"));
        data.add(new ReportDto("group_2", 1, "customer 4", "customer 4_4", "444"));
        data.add(new ReportDto("group_2", 2, "customer 5", "customer 5_5", "555"));
        data.add(new ReportDto("group_2", 2, "customer 6", "customer 6_6", "666"));
        data.add(new ReportDto("group_2", 3, "customer 7", "customer 7_7", "777"));
        data.add(new ReportDto("group_2", 4, "customer 8", "customer 8_8", "888"));
        return new JRBeanCollectionDataSource(data);
    }

    private Map<String, Object> getParamsForList() {
        Map<String, Object> paramForList = new HashMap<>();
        paramForList.put("_size_of_list", 88);
        paramForList.put("_map_of_list", Map.of("address", "TB"));
        paramForList.put("_data_of_list", getDataForList());
        return paramForList;
    }

    private JRDataSource getDataForList() {
        List<ReportDto.ListDto> dataForList = new ArrayList<>();
        dataForList.add(new ReportDto.ListDto("list name 1", 12));
        dataForList.add(new ReportDto.ListDto("list name 2", 13));
        dataForList.add(new ReportDto.ListDto("list name 3", 14));
        dataForList.add(new ReportDto.ListDto("list name 4", 15));
        dataForList.add(new ReportDto.ListDto("list name 5", 16));
        dataForList.add(new ReportDto.ListDto("list name 6", 16));
        return new JRBeanCollectionDataSource(dataForList);
    }

    private Map<String, Object> getParamsForSubReport() {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, Object> paramForSubReport = new HashMap<>();
        paramForSubReport.put("_subreport_source", getSubReport());
        paramForSubReport.put("_data_of_subreport", getDataForSubReport());
        paramForSubReport.put("_map_of_subreport", Map.of("created_date", "2024-09-09"));
        paramForSubReport.put("_name_of_subreport", "MY SUB_REPORT");
        return paramForSubReport;
    }

    private JRBeanCollectionDataSource getDataForSubReport() {
        List<ReportDto.SubReportDto> dataForSubReport = new ArrayList<>();
        dataForSubReport.add(new ReportDto.SubReportDto(1, "cell 1"));
        dataForSubReport.add(new ReportDto.SubReportDto(2, "cell 2"));
        dataForSubReport.add(new ReportDto.SubReportDto(3, "cell 3"));
        dataForSubReport.add(new ReportDto.SubReportDto(4, "cell 4"));
        dataForSubReport.add(new ReportDto.SubReportDto(5, "cell 5"));
        dataForSubReport.add(new ReportDto.SubReportDto(6, "cell 6"));
        return new JRBeanCollectionDataSource(dataForSubReport);
    }

    private JasperReport getSubReport() {
        InputStream resourceAsStream = getClass().getResourceAsStream(SUB_JASPER_PATH);
        try {
            return JasperCompileManager.compileReport(resourceAsStream);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getParamsForCharts() {
        List<ReportDto.ChartDto> list = new ArrayList<>();
        list.add(new ReportDto.ChartDto(2, 3, 4, "label_1", "c_1"));
        list.add(new ReportDto.ChartDto(3, 2, 3, "label_2", "c_2"));
        list.add(new ReportDto.ChartDto(1, 3, 4, "label_3", "c_3"));
        list.add(new ReportDto.ChartDto(5, 4, 5, "label_4", "c_4"));
        list.add(new ReportDto.ChartDto(3, 5, 2, "label_5", "c_5"));
        list.add(new ReportDto.ChartDto(2, 3, 6, "label_6", "c_6"));
        list.add(new ReportDto.ChartDto(4, 4, 6, "label_7", "c_7"));

        Map<String, Object> paramForSpiderChart = new HashMap<>();
        paramForSpiderChart.put("_spider_chart_data", new JRBeanCollectionDataSource(list));
        paramForSpiderChart.put("_pie_chart_data", new JRBeanCollectionDataSource(list));
        paramForSpiderChart.put("_bar_chart_data", new JRBeanCollectionDataSource(list));
        return paramForSpiderChart;
    }

}
