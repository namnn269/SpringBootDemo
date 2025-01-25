package com.example.springbootdemo.infrastructure.database.logicom.repository;

import com.example.springbootdemo.infrastructure.database.logicom.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
