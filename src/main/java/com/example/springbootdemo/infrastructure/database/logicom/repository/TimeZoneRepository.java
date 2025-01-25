package com.example.springbootdemo.infrastructure.database.logicom.repository;

import com.example.springbootdemo.infrastructure.database.logicom.entity.TimeZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends JpaRepository<TimeZoneEntity, Integer>, BaseRepository {
}
