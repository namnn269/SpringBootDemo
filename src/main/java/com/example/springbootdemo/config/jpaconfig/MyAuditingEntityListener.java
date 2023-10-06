package com.example.springbootdemo.config.jpaconfig;

import com.example.springbootdemo.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Configurable
@NoArgsConstructor
// đang ko tùy chỉnh được Date lưu vào trong DB khi kế thừa AuditingEntityListener
public class MyAuditingEntityListener extends AuditingEntityListener {

    @Autowired
    public MyAuditingEntityListener(ObjectFactory<AuditingHandler> auditingHandler) {
        super.setAuditingHandler(auditingHandler);
    }

    @Override
    @PrePersist
    public void touchForCreate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setCreatedAt(now());
            baseEntity.setUpdateAt(now());
            super.touchForCreate(entity);
        }
    }

    @Override
    @PreUpdate
    public void touchForUpdate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setUpdateAt(now());
            super.touchForUpdate(entity);
        }
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
