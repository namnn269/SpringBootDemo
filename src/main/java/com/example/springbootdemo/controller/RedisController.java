package com.example.springbootdemo.controller;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
@AllArgsConstructor
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;

    @GetMapping("/geo")
    public Object get(@RequestParam(defaultValue = "bike:stations") String key,
                      @RequestParam(defaultValue = "1") Double distance) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> search2 = redisTemplate
                .opsForGeo()
                .search(key, new Circle(new Point(-122.27652d, 37.805186d), new Distance(distance, Metrics.METERS)));
        return Map.of("jedis", geoJedis(key, distance), "spring", search2);
    }

    private void lockRedisson() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379");
//        config.setCodec(new JsonJacksonCodec());
//        RedissonClient redissonClient = Redisson.create(config);
//        List<Object> search = redissonClient
//                .getGeo(key)
//                .search(GeoSearchArgs.from(-122.27652d, 37.805186d).radius(distance, GeoUnit.METERS).count(5));

        RLock lock = redissonClient.getLock("lock_key");
        RLock lock2 = redissonClient.getLock("lock_key");
        try {
            boolean locked = lock.tryLock(1, 60, TimeUnit.SECONDS);
            boolean locked2 = lock2.tryLock(1, 60, TimeUnit.SECONDS);
            System.out.println(locked);
            System.out.println(locked2);
            lock.unlock();
            lock2.unlock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Object geoJedis(String key, double distance) {
        try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            GeoSearchParam geoSearchParam = new GeoSearchParam()
                    .fromLonLat(-122.27652d, 37.805186d)
                    .byRadius(distance, GeoUnit.M)
                    .withCoord()
                    .withDist()
                    .withHash();
            List<GeoRadiusResponse> stations2 = jedis.geosearch(key, geoSearchParam);
            System.out.println();
            return stations2;
        }
    }
}
