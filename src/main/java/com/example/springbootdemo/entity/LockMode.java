package com.example.springbootdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "lock_mode")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LockMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
//    @Version
    private LocalDateTime localDateTime;
    private ZonedDateTime zonedDateTime;
    private Date date;
    private java.sql.Date dateSql;
    private Timestamp timestamp;
    @Version
    private Integer version;
}
