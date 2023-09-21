package com.example.springbootdemo.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseRepository {
    @Query(value = " SELECT o FROM #{#entityName} o ")
    <T> List<T> findByDynamicEntity();
}
