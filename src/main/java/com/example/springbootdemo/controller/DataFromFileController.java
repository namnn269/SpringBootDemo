package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.User;
import com.example.springbootdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/data-files")
@Slf4j
public class DataFromFileController {
    @Autowired
    private UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<List<User>>> importDataFromFile(@RequestParam(value = "files") MultipartFile[] files) throws IOException {
        log.info("In user controller, thread {}", Thread.currentThread().getName());
        CompletableFuture<List<User>> userFuture = userService.importData2(files);
        return CompletableFuture.completedFuture(new ResponseEntity<>(userFuture.join(), HttpStatus.CREATED));
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
