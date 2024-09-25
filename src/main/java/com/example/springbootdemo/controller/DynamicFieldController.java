package com.example.springbootdemo.controller;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/dynamic-fields")
@Configuration
public class DynamicFieldController {

    public static final String DTO_FILTER_ID = "dtoFilterId";

    @GetMapping
    public ResponseEntity<MappingJacksonValue> get(@RequestParam(required = false, defaultValue = "") String[] dtoFields,
                                                   @RequestParam(value = "wrapper.dtoFieldMode", defaultValue = "DEFAULT") PropertyMode dtoFieldMode,
                                                   @RequestParam(required = false, defaultValue = "") String[] wrapperFields,
                                                   @RequestParam(value = "wrapperFieldMode", defaultValue = "DEFAULT") PropertyMode wrapperFieldMode) {
        Dto dto = new Dto("dto field 1", "dto field 2", "dto field 3", "common dto");
        DtoWrapper wrapper = new DtoWrapper(dto, dto, "wrapper 1", "wrapper 2", "common wrapper");

        // áp dụng cho tất cả object và object con bên trong
//        Map<Class<?>, Set<String>> map = new HashMap<>();
//        map.put(Dto.class, Set.of(dtoFields));
//        map.put(DtoWrapper.class, Set.of(wrapperFields));
//        MappingJacksonValue mappingJacksonValue = new DynamicFieldResponseWrapper(wrapper, DTO_FILTER_ID, map, PropertyMode.EXCLUDE);

        // mỗi object có cách áp dụng khác nhau
        Map<Class<?>, SerializeFieldMode> map2 = new HashMap<>();
        map2.put(Dto.class, new SerializeFieldMode(Set.of(dtoFields), dtoFieldMode));
        map2.put(DtoWrapper.class, new SerializeFieldMode(Set.of(wrapperFields), wrapperFieldMode));
        MappingJacksonValue mappingValue = new DynamicFieldResponsePerTypeWrapper(List.of(wrapper), DTO_FILTER_ID, map2);

        return ResponseEntity.ok(mappingValue);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonFilter(DTO_FILTER_ID)
    public static class Dto {
        private String field1;
        private String field2;
        private String field3;
        private String common;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonFilter(DTO_FILTER_ID)
    public static class DtoWrapper {
        private Dto dto;
        private Dto dto2;
        private String wrapper1;
        private String wrapper2;
        private String common;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class SerializeFieldMode {
        private Set<String> fields;
        private PropertyMode mode;
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
        INCLUDE, EXCLUDE, DEFAULT;
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

    @Setter
    @Getter
    public static class DynamicFieldResponsePerTypeWrapper extends MappingJacksonValue {
        private final Map<Class<?>, SerializeFieldMode> map;
        private final String propertyFilterId;

        public DynamicFieldResponsePerTypeWrapper(Object value,
                                                  String propertyFilterId,
                                                  Map<Class<?>, SerializeFieldMode> map) {
            super(value);
            this.map = map;
            this.propertyFilterId = propertyFilterId;
        }

        @Override
        public FilterProvider getFilters() {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter(propertyFilterId, new CustomPropertyFilter(map));
            return filterProvider;
        }
    }

    public static class CustomPropertyFilter extends SimpleBeanPropertyFilter {
        private final Map<Class<?>, SerializeFieldMode> map;

        public CustomPropertyFilter(Map<Class<?>, SerializeFieldMode> map) {
            this.map = map;
        }

        @Override
        protected boolean include(BeanPropertyWriter writer) {
            return this.include((PropertyWriter) writer);
        }

        @Override
        protected boolean include(PropertyWriter writer) {
            String fieldName = writer.getName();
            Class<?> checkedClass = writer.getMember().getDeclaringClass();

            SerializeFieldMode serializeFieldMode = map.get(checkedClass);
            if (serializeFieldMode == null)
                return true;

            PropertyMode mode = serializeFieldMode.getMode();
            boolean isIncludeMode = mode == PropertyMode.INCLUDE;
            boolean isExcludeMode = mode == PropertyMode.EXCLUDE;
            boolean isContainField = serializeFieldMode.getFields().contains(fieldName);

            if (isIncludeMode) {
                return isContainField;
            } else if (isExcludeMode) {
                return !isContainField;
            }
            return true;
        }
    }

}
