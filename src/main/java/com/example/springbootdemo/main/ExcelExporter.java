package com.example.springbootdemo.main;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ExcelExporter<T> {

    private final Function<Object, T> mapper;
    private final List<T> list;
    private final Workbook workbook;
    private CellAddress startCell = new CellAddress(0, 0);
    private Map<CellAddress, Object> params;

    public ExcelExporter(InputStream inputStream, Function<Object, T> mapper) throws IOException {
        this.mapper = mapper;
        list = new ArrayList<>();
        workbook = WorkbookFactory.create(inputStream);
    }

    public ExcelExporter(InputStream inputStream, Function<Object, T> mapper, String password) throws IOException {
        this.mapper = mapper;
        list = new ArrayList<>();
        workbook = WorkbookFactory.create(inputStream, password);
    }

    public void setObjectList(List<Object> objectList) {
        for (Object o : objectList) {
            T el = mapper.apply(o);
            this.list.add(el);
        }
    }

    public void setParams(Map<CellAddress, Object> params) {
        this.params = params;
    }

    public void setParams2(Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            CellAddress cellAddress = new CellAddress(entry.getKey());
            this.params.put(cellAddress, entry.getValue());
        }
    }

    public void addParams(String cellAddress, Object value) {
        if (params == null) params = new HashMap<>();
        params.put(new CellAddress(cellAddress), value);
    }

    public void addParams(int row, int col, Object value) {
        if (params == null) params = new HashMap<>();
        params.put(new CellAddress(row, col), value);
    }

    public void setStartCell(CellAddress cellAddress) {
        this.startCell = cellAddress;
    }

    public void setStartCell(int column, int row) {
        this.startCell = new CellAddress(row, column);
    }

    public void setStartCell(String cellAddress) {
        this.startCell = new CellAddress(cellAddress);
    }

    public byte[] export(String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(startCell.getRow() + i);
            T record = list.get(i);
            Field[] fields = record.getClass().getDeclaredFields();
            int columnNum = fields.length;
            for (int j = 0; j < columnNum; j++) {
                Field field = fields[j];
                field.setAccessible(true);
                try {
                    Object cellValue = field.get(record);
                    Cell cell = row.createCell(startCell.getColumn() + j);
                    setValue(cell, cellValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (params != null) {
            for (Map.Entry<CellAddress, Object> entry : params.entrySet()) {
                CellAddress address = entry.getKey();
                Row row = sheet.getRow(address.getRow());
                Cell cell = row.createCell(address.getColumn());
                setValue(cell, entry.getValue());
            }
        }

        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        try {
            workbook.write(new FileOutputStream("")); /////
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        }
    }
}
