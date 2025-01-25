package com.example.springbootdemo.config.jpaconfig;

import com.example.springbootdemo.infrastructure.database.logicom.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//@Component
public class MyCustomAuditingEntityListener {

    private final AuditorAware<Integer> auditorAware;
    private AuditingEntityListener auditingEntityListener;

    public MyCustomAuditingEntityListener(AuditorAware<Integer> auditorAware) {
        this.auditorAware = auditorAware;
    }

    @PrePersist
    public void touchForCreate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setCreatedAt(now());
            baseEntity.setUpdateAt(now());
            baseEntity.setCreatedBy(auditorAware.getCurrentAuditor().orElse(0));
            baseEntity.setUpdateBy(auditorAware.getCurrentAuditor().orElse(0));
        }
    }

    @PreUpdate
    public void touchForUpdate(Object entity) {
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setUpdateAt(now());
            baseEntity.setUpdateBy(auditorAware.getCurrentAuditor().orElse(0));
        }
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
