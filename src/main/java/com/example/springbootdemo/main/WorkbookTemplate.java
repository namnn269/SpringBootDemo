package com.example.springbootdemo.main;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookTemplate {
    private final static Workbook sourceWorkbook;
    private final static Font sourceHeaderFont;
    private final static Font sourceFont;
    private static final CellStyle sourceCellStyle;
    private final static CellStyle sourceHeaderCellStyle;

    static {
        sourceWorkbook = new XSSFWorkbook();

        sourceHeaderFont = sourceWorkbook.createFont();
        sourceHeaderFont.setBold(true);
        sourceHeaderFont.setColor(IndexedColors.BLUE.index);
        sourceHeaderFont.setItalic(true);
        sourceHeaderFont.setFontName("Arial");
        sourceHeaderFont.setFontHeightInPoints((short) 20);

        sourceFont = sourceWorkbook.createFont();
        sourceFont.setColor(IndexedColors.GREEN.index);
        sourceFont.setBold(false);
        sourceFont.setFontName("Avo");
        sourceFont.setFontHeightInPoints((short) 15);

        sourceHeaderCellStyle = sourceWorkbook.createCellStyle();
        sourceHeaderCellStyle.setFont(sourceHeaderFont);
        sourceHeaderCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        sourceHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        sourceHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
        sourceHeaderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        sourceHeaderCellStyle.setBorderRight(BorderStyle.DASH_DOT_DOT);
        sourceHeaderCellStyle.setBorderLeft(BorderStyle.DOTTED);

        sourceCellStyle = sourceWorkbook.createCellStyle();
        sourceCellStyle.setFont(sourceFont);
        sourceCellStyle.setBorderRight(BorderStyle.DASHED);
        sourceCellStyle.setBorderBottom(BorderStyle.DOUBLE);
        sourceCellStyle.setAlignment(HorizontalAlignment.CENTER);
        sourceCellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
    }

    private WorkbookTemplate() {
    }

    public static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.cloneStyleFrom(sourceHeaderCellStyle);
        return headerCellStyle;
    }

    public static CellStyle getCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.cloneStyleFrom(sourceCellStyle);
        return cellStyle;
    }
}
