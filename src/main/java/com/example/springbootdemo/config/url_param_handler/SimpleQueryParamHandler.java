package com.example.springbootdemo.config.url_param_handler;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleQueryParamHandler implements HandlerMethodArgumentResolver {

    private final Map<Class<?>, Map<String, String>> map = new ConcurrentHashMap<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ConvertibleParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Class<?> clazz = parameter.getParameterType();
        Object target = clazz.getConstructor().newInstance();
        WebDataBinder binder = binderFactory.createBinder(webRequest, target, target.getClass().getName());
        MutablePropertyValues values = buildPropertyParams(webRequest, binder);
        binder.bind(values);

        return binder.getTarget();
    }

    private MutablePropertyValues buildPropertyParams(NativeWebRequest request,
                                                      WebDataBinder binder) {
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        Class<?> clazz = binder.getTarget().getClass();
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
