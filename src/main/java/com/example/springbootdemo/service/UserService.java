package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.UserDto;
import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.repository.UserRepository;
import com.example.springbootdemo.utils.CheckFileType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ReadFile<User> readFile;
    @Autowired
    private ObjectMapper objectMapper;

    //    @Async
    public CompletableFuture<List<User>> importData2(MultipartFile[] files) {
        List<User> totalUsers = new ArrayList<>();
        for (MultipartFile file : files) {
            List<User> users = null;
            switch (CheckFileType.checkHttpFile(file)) {
                case EXEL -> users = readExcelFile(file);
//                case JSON -> users = readJsonFile(file);
                case JSON -> users = readFile.getList(file, User.class);
                case CSV -> users = readCsvFile(file);
                case SQL -> users = readSQLFile(file);
                default -> log.error(file.getOriginalFilename() + " is not a supported file type");
            }
            if (users != null && !users.isEmpty())
                totalUsers.addAll(users);
        }
        userRepo.saveAll(totalUsers);
        log.info("In user service, thread {}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(totalUsers);
    }

    private List<User> readExcelFile(MultipartFile excelFile) {
        try (InputStream is = excelFile.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            List<User> users = new LinkedList<>();
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    User user = new User();
                    if (row.getRowNum() == 0) continue;
                    for (Cell cell : row) {
                        switch (cell.getColumnIndex()) {
                            case 0 -> user.setEmail(cell.getStringCellValue());
                            case 1 -> user.setName(cell.getStringCellValue());
                            case 2 -> user.setGender(cell.getStringCellValue());
                            case 3 -> user.setAddress(cell.getStringCellValue());
                            case 4 -> user.setPhone(cell.getStringCellValue());
                            case 5 -> user.setAge((int) cell.getNumericCellValue());
                        }
                    }
                    users.add(user);
                }
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> readJsonFile(MultipartFile jsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = new LinkedList<>();
        try {
            List<UserDto> userDto = objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
            });
            return userDto.stream().map(this::toEntity).collect(Collectors.toList());
//            return objectMapper.readValue(jsonFile.getInputStream(), new TypeReference<>() {
//            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<User> readCsvFile(MultipartFile csvFile) {
        List<User> users = new LinkedList<>();
        try (CSVParser csvRecords = CSVParser.parse(csvFile.getInputStream(), Charset.defaultCharset(), CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : csvRecords) {
                if (csvRecord.getRecordNumber() == 1) continue;
                UserDto dto = UserDto.builder()
                        .email(csvRecord.get(0))
                        .name(csvRecord.get(1))
                        .gender(csvRecord.get(2))
                        .address(csvRecord.get(3))
                        .phone(csvRecord.get(4))
                        .age(Integer.parseInt(csvRecord.get(5)))
                        .build();
                users.add(this.toEntity(dto));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private List<User> readSQLFile(MultipartFile sqlFile) {

        return null;
    }

    private User toEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }
}
