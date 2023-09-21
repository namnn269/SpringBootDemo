package com.example.springbootdemo.controller;

import com.example.springbootdemo.UserDao;
import com.example.springbootdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;
    private final UserDao userDao;

    @GetMapping
    public Object all() {
        return userRepo.findAll();
    }

    @GetMapping(value = "/dto")
    public Object allDto() {
        return userRepo.findUserDtoSubQueryInSelect();
    }

    @GetMapping(value = "/dto/select")
    public Object allDtoDao() {
        return userDao.findUserDtoSubQueryInSelectDao();
    }

    // not work
    @GetMapping(value = "/dto/from")
    public Object allDtoDaoFrom() {
        return userDao.findUserDtoSubQueryInFromDao();
    }

    @GetMapping(value = "/dto/select-union")
    public Object findWithUnion() {
        return userRepo.findStringWithUnionJpa();
    }
}
