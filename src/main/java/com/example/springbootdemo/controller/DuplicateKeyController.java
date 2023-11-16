package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.DuplicateKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/duplicate-key")
public class DuplicateKeyController {
    @Autowired
    DuplicateKeyService duplicateKeyService;

    @GetMapping
    public ResponseEntity<?> lol(@RequestParam Integer id,
                                 @RequestParam String materialCode) {
        return new ResponseEntity<>(duplicateKeyService.updateDuplicateKey(id, materialCode), HttpStatus.OK);
    }
}
