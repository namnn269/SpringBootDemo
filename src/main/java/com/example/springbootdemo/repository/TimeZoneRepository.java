package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.TimeZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZoneEntity, Integer>, BaseRepository {
}
