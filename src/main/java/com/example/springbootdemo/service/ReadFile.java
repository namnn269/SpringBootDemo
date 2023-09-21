package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ReadFile<E> {
    @Autowired
    private ObjectMapper objectMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<E> getList(MultipartFile file, Class<E> clazz) {
        List<E> list = new LinkedList<>();
        try {
            List<Map<String, Object>> listMap = objectMapper.readValue(file.getInputStream(), new TypeReference<>() {
            });

            for (Map<String, Object> map : listMap) {
//                E e = modelMapper.map(map, clazz);
                E e = objectMapper.convertValue(map, clazz);
                list.add(e);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
