package com.example.springbootdemo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public class CheckFileType {
    public static FileType checkSystemFile(File file) {
//        StringBuilder content = new StringBuilder();
//        try (InputStream inputStream = new FileInputStream(file);
//             BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//        } catch (IOException e) {
//            return FileType.UNKNOWN;
//        }

        // check CSV file
        try (CSVParser ignored = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.DEFAULT)) {
            return FileType.CSV;
        } catch (IOException ignored) {
        }

        // check JSON file
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(file, new TypeReference<>() {
            });
            return FileType.JSON;
        } catch (Exception ignored) {
        }

        // check EXEL file
        try (Workbook workbook = WorkbookFactory.create(file)) {
            return FileType.EXEL;
        } catch (Exception ignored) {
        }

        // check SQL file
        try {
            String content = String.valueOf(file);
            System.out.println(content);
            return FileType.SQL;
        } catch (Exception ignored) {
        }
        return FileType.UNKNOWN;
    }

    public static FileType checkHttpFile(MultipartFile file) {
        if (file == null || file.getSize() == 0 || file.getContentType() == null) return FileType.UNKNOWN;
        String type = file.getContentType();
        String extension = file.getOriginalFilename();
        switch (type) {
            case "text/csv" -> {
                if (extension != null && extension.endsWith(".csv")) return FileType.CSV;
            }
            case "application/json" -> {
                if (extension != null && extension.endsWith(".json")) return FileType.JSON;
            }
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> {
                if (extension != null && extension.endsWith(".xlsx")) return FileType.EXEL;
            }
            case "application/x-sql", "application/sql", "text/sql" -> {
                if (extension != null && extension.endsWith(".sql")) return FileType.SQL;
            }
        }
        return FileType.UNKNOWN;
    }

    public static boolean isCsv(MultipartFile file) {
        if (file == null || file.getSize() == 0 || file.getContentType() == null) return false;
        return file.getContentType().equals("text/csv") && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv");
    }

    public static boolean isExcel(MultipartFile file) {
        if (file == null || file.getSize() == 0 || file.getContentType() == null) return false;
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx");
    }

    public static boolean isSql(MultipartFile file) {
        if (file == null || file.getSize() == 0 || file.getContentType() == null) return false;
        String type = file.getContentType();
        return (type.equals("application/x-sql")
                || type.equals("application/sql")
                || type.equals("text/sql"))
                && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".sql");
    }

    public static boolean isJson(MultipartFile file) {
        if (file == null || file.getSize() == 0 || file.getContentType() == null) return false;
        return file.getContentType().equals("application/json") && Objects.requireNonNull(file.getOriginalFilename()).endsWith(".json");
    }
}
