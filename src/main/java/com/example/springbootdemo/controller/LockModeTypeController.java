package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.LockMode;
import com.example.springbootdemo.repository.LockModeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/lock-mode")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class LockModeTypeController {

    private final LockModeRepository lockModeRepo;

    @PostMapping
    public Object post(@RequestBody LockMode lockMode) {
        lockMode = LockMode.builder()
                .name(lockMode.getName())
                .localDateTime(LocalDateTime.now())
                .zonedDateTime(ZonedDateTime.now(ZoneId.of("+09:00")))
                .date(new Date())
                .dateSql(new java.sql.Date(new Date().getTime()))
                .timestamp(new Timestamp(new Date().getTime()))
                .build();
        return lockModeRepo.save(lockMode);
    }

    @GetMapping("/compare")
    public Object compare(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'P'HH:mm:ssXXX") ZonedDateTime zonedDateTimeStart,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'P'HH:mm:ss'['z']'") ZonedDateTime zonedDateTimeEnd) {
        System.out.println(zonedDateTimeStart);
        System.out.println(zonedDateTimeEnd);
        return lockModeRepo.findTopByZonedDateTime(zonedDateTimeStart, zonedDateTimeEnd);
    }

    @GetMapping
    public Object getAllNoLock() {
        System.out.println("\n==================\n");
        return lockModeRepo.findAll();
    }

    @GetMapping("/{id}")
    public Object getNoLock(@PathVariable Integer id) {
        System.out.println("\n==================\n");
        return lockModeRepo.findNoLockById(id);
    }

    @GetMapping("/optimistic/{id}")
    public Object getOptimistic(@PathVariable int id) {
        System.out.println("\n==================\n");
        LockMode o = lockModeRepo.findOptimisticById(id);
        System.out.println("in get optimistic");
        return o;
    }

    @GetMapping("/optimistic-force-increment/{id}")
    public Object getOptimisticForce(@PathVariable int id) {
        System.out.println("\n==================\n");
        LockMode optimisticForceById = lockModeRepo.findOptimisticForceById(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return optimisticForceById;
    }

    @GetMapping("/share-lock/{id}")
    public Object getShareLock(@PathVariable Integer id) {
        System.out.println("\n==================\n");
        LockMode o = lockModeRepo.findShareById(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    @GetMapping("/exclusive-lock/{id}")
    public Object getExclusiveLock(@PathVariable Integer id) {
        System.out.println("\n==================\n");
        LockMode o = lockModeRepo.findExclusiveById(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    @GetMapping("/pessimistic-force-lock/{id}")
    public Object getPesForceLock(@PathVariable Integer id) {
        System.out.println("\n==================\n");
        LockMode o = lockModeRepo.findPessimisticForceById(id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    @PatchMapping("/update-no-lock")
    public Object updateNoLock(@RequestBody LockMode lockModeReq) {
        System.out.println("\n==================\n");
        LockMode lockModeDb = lockModeRepo.findNoLockById(lockModeReq.getId());
        return setFields(lockModeReq, lockModeDb);
    }

    @PatchMapping("/update-optimistic")
    public Object updateOptimistic(@RequestBody LockMode lockModeReq) {
        try {
            System.out.println("\n==================\n");
            LockMode lockModeDb = lockModeRepo.findOptimisticById(lockModeReq.getId());
            Object o = setFields(lockModeReq, lockModeDb);
            System.out.println("in optimistic normal");
            return o;
        } catch (OptimisticLockingFailureException ex) {
            return "optimistic error 2";
        } catch (Exception exception) {
            return "error";
        }
    }

    @PatchMapping("/update-optimistic-force-increment")
    public Object updateOptimisticForceIncrement(@RequestBody LockMode lockModeReq) {
        System.out.println("\n==================\n");
        LockMode lockModeDb = lockModeRepo.findOptimisticForceById(lockModeReq.getId());
        Object o = setFields(lockModeReq, lockModeDb);
        System.out.println("in optimistic force");
        return o;
    }

    @PatchMapping("/update-no-change-no-lock")
    public Object updateNoChangeNoLock(@RequestBody LockMode lockModeReq) {
        System.out.println("\n==================\n");
        LockMode lockModeDb = lockModeRepo.findNoLockById(lockModeReq.getId());
        return lockModeRepo.save(lockModeDb);
    }

    @PatchMapping("/update-no-change-optimistic")
    public Object updateNoChangeOptimistic(@RequestBody LockMode lockModeReq) {
        System.out.println("\n==================\n");
        LockMode lockModeDb = lockModeRepo.findOptimisticById(lockModeReq.getId());
        return lockModeRepo.save(lockModeDb);
    }

    @PatchMapping("/update-no-change-optimistic-force-increment")
    public Object updateNoChangeOptimisticForceIncrement(@RequestBody LockMode lockModeReq) {
        System.out.println("\n==================\n");
        LockMode lockModeDb = lockModeRepo.findOptimisticForceById(lockModeReq.getId());
        return lockModeRepo.save(lockModeDb);
    }

    private Object setFields(@RequestBody LockMode lockModeReq, LockMode lockModeDb) {
        lockModeDb.setName(lockModeReq.getName());
        lockModeDb.setLocalDateTime(LocalDateTime.now());
        lockModeDb.setZonedDateTime(ZonedDateTime.now(ZoneId.of("+10:00")));
        lockModeDb.setDate(new Date());
        lockModeDb.setTimestamp(new Timestamp(new Date().getTime()));
        lockModeDb.setDateSql(new java.sql.Date(new Date().getTime()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return lockModeRepo.save(lockModeDb);
    }

}
