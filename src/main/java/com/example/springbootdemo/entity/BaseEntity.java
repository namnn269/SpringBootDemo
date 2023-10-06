package com.example.springbootdemo.entity;

import com.example.springbootdemo.config.jpaconfig.MyAuditingEntityListener;
import com.example.springbootdemo.config.jpaconfig.MyCustomAuditingEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@MappedSuperclass
@EntityListeners(MyCustomAuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "memo")
    private String memo;

    @CreatedDate
    @Column(name = "create_date_time")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "update_date_time")
    private LocalDateTime updateAt;

    @CreatedBy
    @Column(name = "create_user_id")
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "update_user_id")
    private Integer updateBy;
}
