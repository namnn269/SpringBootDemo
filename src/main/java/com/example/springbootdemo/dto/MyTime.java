package com.example.springbootdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyTime {
    private Instant instantDate; // default: yyyy-MM-dd'T'HH:mm:ssXXX
    private Instant instantNumber;
    private LocalDateTime localDateTime;
    private ZonedDateTime zonedDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd'Q'HH:mm:ssXXX")
    private ZonedDateTime zonedDateTime2;
    @JsonFormat(pattern = "yyyy-MM-dd'Q'HH:mm:ss'['z']'")
    private ZonedDateTime zonedDateTime3;
    private OffsetDateTime offsetDateTime;
    private OffsetDateTime offsetDateTime2;
}
