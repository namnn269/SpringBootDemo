package com.example.springbootdemo.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Room extends BaseEntity{
    private String name;
    private ZonedDateTime outGoingTime;
}
