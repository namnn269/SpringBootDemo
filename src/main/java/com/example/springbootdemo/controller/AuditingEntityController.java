package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.Room;
import com.example.springbootdemo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auditing-entity")
public class AuditingEntityController {

    @Autowired
    private RoomRepository roomRepo;

    @PostMapping
    public Object post(@RequestBody Room room) {
        System.out.println("before create: " + room);
        Room save = roomRepo.save(room);
        System.out.println("after create: " + room);
        return save;
    }

    @PatchMapping
    public Object update(@RequestBody Room room) {
        System.out.println("before update: " + room);
        Room entity = roomRepo.findById(room.getId()).get();
        entity.setName(room.getName());
        entity.setOutGoingTime(room.getOutGoingTime());
        Room save = roomRepo.save(entity);
        System.out.println("after update: " + room);
        return save;
    }

}
