package com.example.springbootdemo.config.url_param_handler;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyCustomQueryParamHandler extends ServletModelAttributeMethodProcessor {

    private final Map<Class<?>, Map<String, String>> map = new ConcurrentHashMap<>();

    public MyCustomQueryParamHandler(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ConvertibleParam.class);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        MutablePropertyValues values = buildPropertyParams(request, binder);
        binder.bind(values);
    }

    private MutablePropertyValues buildPropertyParams(NativeWebRequest request,
                                                      WebDataBinder binder) {
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        ResolvableType targetType = binder.getTargetType();
        if (targetType == null)
            return propertyValues;

        Class<?> clazz = targetType.toClass();
        Map<String, String> paramToField;
        if (map.containsKey(clazz)) {
            paramToField = map.get(clazz);
        } else {
            paramToField = buildParamToFieldMap(clazz);
            map.put(clazz, paramToField);
        }

        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            propertyValues.add(paramToField.getOrDefault(key, key), value);
        }

        for (Map.Entry<String, List<MultipartFile>> entry : getMultiPartFiles(request).entrySet()) {
            String key = entry.getKey();
            List<MultipartFile> file = entry.getValue();

            propertyValues.add(paramToField.getOrDefault(key, key), file);
        }

        return propertyValues;
    }

    private Map<String, String> buildParamToFieldMap(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Map<String, String> paramToFieldMap = new HashMap<>(declaredFields.length);

        for (Field field : declaredFields) {
            QueryParam annotation = field.getAnnotation(QueryParam.class);
            String fieldName = field.getName();
            if (annotation != null) {
                paramToFieldMap.put(annotation.value(), fieldName);
            } else {
                paramToFieldMap.put(fieldName, fieldName);
            }
        }
        return paramToFieldMap;
    }

    private MultiValueMap<String, MultipartFile> getMultiPartFiles(NativeWebRequest request) {

        MultipartRequest multipartRequest = request.getNativeRequest(MultipartRequest.class);
        if (multipartRequest == null)
            return new LinkedMultiValueMap<>();
        return multipartRequest.getMultiFileMap();
    }

}
