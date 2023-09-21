package com.example.springbootdemo.repository;

import com.example.springbootdemo.entity.LockMode;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
//@Transactional(propagation = Propagation.REQUIRED)
public interface LockModeRepository extends JpaRepository<LockMode, Integer> {

    LockMode findNoLockById(Integer id);

    @Query(" SELECT lm FROM LockMode lm " +
            " WHERE lm.id = :id ")
    @Lock(LockModeType.OPTIMISTIC)
    LockMode findOptimisticById(int id);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    LockMode findOptimisticForceById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints(value = {@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    LockMode findShareById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    LockMode findExclusiveById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    LockMode findPessimisticForceById(Integer id);

    @Query(" SELECT lm FROM LockMode lm " +
            " WHERE lm.zonedDateTime >= :zonedDateTimeStart " +
            " AND lm.zonedDateTime <= :zonedDateTimeEnd ")
    List<LockMode> findTopByZonedDateTime(ZonedDateTime zonedDateTimeStart, ZonedDateTime zonedDateTimeEnd);

}
