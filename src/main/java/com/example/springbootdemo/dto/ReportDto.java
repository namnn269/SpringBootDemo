package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportDto {
    private String groupIndex;
    private Integer levelIndex;
    private String customerName;
    private String customerName2;
    private String phoneNumber;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ListDto {
        private String listName;
        private Integer listAge;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SubReportDto {
        private int lineNumber;
        private String cellValue;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ChartDto {
        private int index1;
        private int index2;
        private int index3;
        private String label;
        private String category;
    }
}
