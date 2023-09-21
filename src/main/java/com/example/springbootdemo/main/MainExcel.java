package com.example.springbootdemo.main;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.*;

public class MainExcel {
    public static void main(String[] args) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet firstSheet = workbook.createSheet("First sheet");
            XSSFSheet secondSheet = workbook.createSheet("Second sheet");
            XSSFSheet thirdSheet = workbook.createSheet("Third sheet");

            CellStyle headerStyle = WorkbookTemplate.getHeaderCellStyle(workbook);
            CellStyle cellStyle = WorkbookTemplate.getCellStyle(workbook);

            XSSFRow headerRow = firstSheet.createRow(0);
            headerRow.setHeight((short) 1000);

            XSSFCell headerCell = headerRow.createCell(0);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("header 0");
            headerCell = headerRow.createCell(1);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("header 1");

            XSSFCell cell;
            XSSFRow row = null;
            for (int i = 1; i < 10; i++) {
                row = firstSheet.createRow(i);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(i + " - 1");

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(i + " - 2");
            }

            for (int i = 0; i < row.getLastCellNum(); i++) {
                firstSheet.autoSizeColumn(i);
            }


            FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
            OutputStream bos = new BufferedOutputStream(outputStream);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            ByteArrayInputStream bais = new ByteArrayInputStream(outputStream.toByteArray());
            oos.writeObject("");
//            workbook.write(new FileOutputStream("C:\\Users\\Nam\\Desktop\\test.xlsx"));
            workbook.write(outputStream);
            System.out.println(outputStream.toString());
            System.out.println(outputStream.toByteArray().length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
