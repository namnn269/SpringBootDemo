package com.example.springbootdemo.controller;

import com.example.springbootdemo.entity.TimeZoneEntity;
import com.example.springbootdemo.repository.TimeZoneRepository;
import jakarta.persistence.EntityManager;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/timezone")
public class TimeZoneController {

    private final TimeZoneRepository timeZoneRepo;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public TimeZoneController(TimeZoneRepository timeZoneRepo,
                              JdbcTemplate jdbcTemplate,
                              EntityManager entityManager) {
        this.timeZoneRepo = timeZoneRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @GetMapping("/param")
    public Object get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") ZonedDateTime zonedDateTimeXXX,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['VV']'") ZonedDateTime zonedDateTimeVV,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['z']'") ZonedDateTime zonedDateTimeZ,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zz']'") ZonedDateTime zonedDateTimeZz,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zzz']'") ZonedDateTime zonedDateTimeZzz,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['zzzz']'") ZonedDateTime zonedDateTimeZzzz,
                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'['XXX']'") OffsetDateTime offsetDateTimeXXX
    ) {

        // khi nhận zonedDateTime hoặc offsetDateTime ở param thì sẽ giữ được zoneId hoặc offsetTime
        // còn khi nhận ở body tự động chuyển về giờ UTC

        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2023-10-10T10:10:10"), ZoneId.of("Asia/Tokyo"));
        TimeZoneEntity timeZoneEntity = TimeZoneEntity.builder()
                .zonedDateTimeVV(zonedDateTimeVV)
                .zonedDateTimeXXX(zonedDateTimeXXX)
                .zonedDateTimeZ(zonedDateTimeZ)
                .zonedDateTimeZz(zonedDateTimeZz)
                .zonedDateTimeZzz(zonedDateTimeZzz)
                .zonedDateTimeZzzz(zonedDateTimeZzzz)
                .offsetDateTimeXXX(offsetDateTimeXXX)
                .build();
        TimeZoneEntity save = timeZoneRepo.save(timeZoneEntity);
        return List.of(timeZoneEntity, timeZoneRepo.findById(49));
    }

    @PostMapping
    public Object post(@RequestBody TimeZoneEntity timeZone) {
        // khi nhận zonedDateTime hoặc offsetDateTime ở param thì sẽ giữ được zoneId hoặc offsetTime
        // còn khi nhận ở body tự động chuyển về giờ UTC
        //  timeZoneRepo.save(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse("2023-10-10T10:10:10"), ZoneId.of("Asia/Tokyo"));
        TimeZoneEntity timeZoneEntity = TimeZoneEntity.builder()
                .zonedDateTimeVV(zonedDateTime)
                .zonedDateTimeXXX(zonedDateTime)
                .zonedDateTimeZ(zonedDateTime)
                .zonedDateTimeZz(zonedDateTime)
                .zonedDateTimeZzz(zonedDateTime)
                .zonedDateTimeZzzz(zonedDateTime)
                .offsetDateTimeXXX(zonedDateTime.toOffsetDateTime())
                .build();
        return List.of(timeZone, timeZoneEntity);
    }

    @GetMapping
    public Object get(@RequestBody List<Integer> idList) {
        return timeZoneRepo.findAllById(idList);
    }

    @PostMapping("/with-jdbc")
    public Object postJdbc(@RequestBody TimeZoneEntity timeZone) {
        String sql = " INSERT INTO time_zone " +
                " (id, name, date_sql, date, local_date_time, zoned_date_time, timestamp) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?) ";
        return jdbcTemplate.update(sql, null, timeZone.getName(), timeZone.getDateSql(), timeZone.getDate(),
                timeZone.getLocalDateTime(), timeZone.getZonedDateTimeVV(), timeZone.getTimestamp());
    }

    @GetMapping("/with-jdbc")
    public Object getJdbc(@RequestBody List<Integer> idList) {
        String sql = " SELECT * FROM time_zone WHERE id IN (?) ";
//        Map<String, Object> params = new HashMap<>();
//        params.put("idList", idList);
        return jdbcTemplate.queryForList(sql, idList.toArray());
    }
}
