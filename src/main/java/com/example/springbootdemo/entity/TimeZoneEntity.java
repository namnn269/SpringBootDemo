package com.example.springbootdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "time_zone")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TimeZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDateTime localDateTime;
    //  '['VV']'    '['z']'    '['zz']'    '['zzz']'    '['zzzz']'
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['VV']'") // req: +09:00 or Asia/Tokyo ; res: Asia/Tokyo
    private ZonedDateTime zonedDateTimeVV;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['XXX']'") // req: only +09:00 ; res: +09:00
    private ZonedDateTime zonedDateTimeXXX;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['z']'") // req: +09:00 or Asia/Tokyo ; res: JST
    private ZonedDateTime zonedDateTimeZ;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zz']'") // req: +09:00 or Asia/Tokyo ; res: JST
    private ZonedDateTime zonedDateTimeZz;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zzz']'") // req: +09:00 or Asia/Tokyo ; res: JST
    private ZonedDateTime zonedDateTimeZzz;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zzzz']'") // req: +09:00 or Asia/Tokyo ; res: Japan Standard Time
    private ZonedDateTime zonedDateTimeZzzz;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['XXX']'")
    private OffsetDateTime offsetDateTimeXXX; // offsetDateTime only use +09:00

    private Date date;
    private java.sql.Date dateSql;
    private Timestamp timestamp;

}
