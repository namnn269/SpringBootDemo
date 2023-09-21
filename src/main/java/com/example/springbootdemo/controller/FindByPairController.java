package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.FindByPairService;
import com.example.springbootdemo.utils.MyPair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("find-by-pair")
@RestController
@RequiredArgsConstructor
public class FindByPairController {
    final FindByPairService findByPairService;

    @GetMapping("jpa")
    public Object findByPairJpa(@RequestBody List<MyPair<String, String>> pairs) {
        return findByPairService.getMaterialJpa(pairs);
    }

    @GetMapping("jdbc")
    public Object findByPairJdbc(@RequestBody List<MyPair<String, String>> pairs) {
        return findByPairService.getMaterialJdbc(pairs);
    }

    @GetMapping("row-num")
    public Object findByRowNum(@RequestBody List<MyPair<String, String>> pairs) {
        return findByPairService.getRowNum();
    }

    @GetMapping("row-num-jpa")
    public Object findByRowNumJpa(@RequestBody List<MyPair<String, String>> pairs) {
        return findByPairService.getRowNumJpa();
    }

    @GetMapping("sub-query-jpa")
    public Object findSubQueryJpa(@RequestBody List<MyPair<String, String>> pairs) {
        return findByPairService.getSubQueryJpa();
    }
}
