package com.example.springbootdemo.controller;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/dynamic-fields")
public class DynamicFieldController {

    public static final String dtoFilterId = "dtoFilter";

    @GetMapping
    public ResponseEntity<MappingJacksonValue> get(@RequestParam(required = false, defaultValue = "") String[] dtoFields,
                                                   @RequestParam(required = false, defaultValue = "") String[] wrapperFields) {
        Dto dto = new Dto("dto field 1", "dto field 2", "dto field 3", "common dto");
        DtoWrapper wrapper = new DtoWrapper(dto, "wrapper 1", "wrapper 2", "common wrapper");


        Map<Class<?>, Set<String>> map = new HashMap<>();
        map.put(Dto.class, Set.of(dtoFields));
        map.put(DtoWrapper.class, Set.of(wrapperFields));


//        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(wrapper);
//        PropertyFilter propertyFilter = new CustomPropertyFilter(map, true);
//        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("dtoFilter", propertyFilter);
//
//        mappingJacksonValue.setFilters(filterProvider);

        MappingJacksonValue mappingJacksonValue = new DynamicFieldResponseWrapper(
                wrapper, dtoFilterId, map, PropertyMode.EXCLUDE);

        return ResponseEntity.ok(mappingJacksonValue);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonFilter(dtoFilterId)
    public static class Dto {
        private String field1;
        private String field2;
        private String field3;
        private String common;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonFilter(dtoFilterId)
    public static class DtoWrapper {
        private Dto dto;
        private String wrapper1;
        private String wrapper2;
        private String common;
    }

    @Setter
    @Getter
    public static class DynamicFieldResponseWrapper extends MappingJacksonValue {
        private final Map<Class<?>, Set<String>> map;
        private final PropertyMode propertyMode;
        private final String propertyFilterId;

        public DynamicFieldResponseWrapper(Object value,
                                           String propertyFilterId,
                                           Map<Class<?>, Set<String>> map,
                                           PropertyMode propertyMode) {
            super(value);
            this.map = map;
            this.propertyMode = propertyMode;
            this.propertyFilterId = propertyFilterId;
        }

        @Override
        public FilterProvider getFilters() {
            PropertyFilter propertyFilter;
            switch (propertyMode) {
                case EXCLUDE -> propertyFilter = new CustomExcludePropertyFilter(map);
                case INCLUDE -> propertyFilter = new CustomIncludePropertyFilter(map);
                default -> throw new RuntimeException();
            }
            return new SimpleFilterProvider().addFilter(propertyFilterId, propertyFilter);
        }
    }

    public enum PropertyMode {
        INCLUDE, EXCLUDE;
    }

    public static class CustomIncludePropertyFilter extends SimpleBeanPropertyFilter {
        private final Map<Class<?>, Set<String>> map;

        public CustomIncludePropertyFilter(Map<Class<?>, Set<String>> map) {
            this.map = map;
        }

        @Override
        protected boolean include(BeanPropertyWriter writer) {
            String fieldName = writer.getName();
            Class<?> clazz = writer.getMember().getDeclaringClass();
            return map.containsKey(clazz) && map.get(clazz).contains(fieldName);
        }

        @Override
        protected boolean include(PropertyWriter writer) {
            String fieldName = writer.getName();
            Class<?> clazz = writer.getMember().getDeclaringClass();
            return map.containsKey(clazz) && map.get(clazz).contains(fieldName);
        }
    }

    public static class CustomExcludePropertyFilter extends SimpleBeanPropertyFilter {
        private final Map<Class<?>, Set<String>> map;

        public CustomExcludePropertyFilter(Map<Class<?>, Set<String>> map) {
            this.map = map;
        }

        @Override
        protected boolean include(BeanPropertyWriter writer) {
            String fieldName = writer.getName();
            Class<?> clazz = writer.getMember().getDeclaringClass();
            return map.containsKey(clazz) && !map.get(clazz).contains(fieldName);
        }

        @Override
        protected boolean include(PropertyWriter writer) {
            String fieldName = writer.getName();
            Class<?> clazz = writer.getMember().getDeclaringClass();
            return map.containsKey(clazz) && !map.get(clazz).contains(fieldName);
        }
    }

}
