package com.example.springbootdemo.config.requestparam;

import com.example.springbootdemo.annotation.param.ParamName;
import com.example.springbootdemo.annotation.param.SupportsParameterResolution;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.util.WebUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestMethodParamResolver extends ServletModelAttributeMethodProcessor {

    private final ConcurrentMap<Class<?>, Map<String, String>> definitionsCache = new ConcurrentHashMap<>();

    public RequestMethodParamResolver(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAnnotationPresent(SupportsParameterResolution.class);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        bind(servletRequest, servletBinder);
    }

    @SuppressWarnings("unchecked")
    public void bind(ServletRequest request, ServletRequestDataBinder binder) {
        Map<String, ?> propertyValues = parsePropertyValues(request, binder);
        MutablePropertyValues mpvs = new MutablePropertyValues(propertyValues);
        MultipartRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartRequest.class);
        if (multipartRequest != null) {
            bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
        }

        // two lines copied from ExtendedServletRequestDataBinder
        String attr = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
        mpvs.addPropertyValues((Map<String, String>) request.getAttribute(attr));
        binder.bind(mpvs);
    }

    private Map<String, ?> parsePropertyValues(ServletRequest request, ServletRequestDataBinder binder) {

        // similar to WebUtils.getParametersStartingWith(..) (prefixes not supported)
        Map<String, Object> params = new TreeMap<>();
        Assert.notNull(request, "Request must not be null");
        Enumeration<?> paramNames = request.getParameterNames();
        Object target = binder.getTarget();
        Class<?> targetClass = target.getClass();
        Map<String, String> parameterMappings = getParameterMappings(binder);
        try {
            while (paramNames != null && paramNames.hasMoreElements()) {

                String paramName = (String) paramNames.nextElement();
                String[] values = request.getParameterValues(paramName);

                String fieldName = parameterMappings.get(paramName);
                // no annotation exists, use the default - the param name=field name
                if (fieldName == null) {
                    fieldName = paramName;
                }
                Field field = null;

                field = targetClass.getDeclaredField(fieldName);

                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(fieldName, values);
                } else {
                    DateTimeFormat dateTimeFormat = field.getAnnotation(DateTimeFormat.class);
                    if (dateTimeFormat != null && !dateTimeFormat.style().isEmpty()) {
                        if (LocalDate.class.getName().equals(field.getType().getName())) {
                            LocalDate date = toJavaLocalDate(values[0], dateTimeFormat.style());
                            params.put(fieldName, date);
                        }
                    } else {
                        params.put(fieldName, values[0]);
                    }
                }
            }
        } catch (NoSuchFieldException ex) {
            logger.error(ex.getMessage());
//            throw new RuntimeException(ex.getMessage(), ex);
        }


        return params;
    }

    /**
     * Gets a mapping between request parameter names and field names.
     * If no annotation is specified, no entry is added
     *
     * @return
     */
    private Map<String, String> getParameterMappings(ServletRequestDataBinder binder) {
        Object target = binder.getTarget();
        if (target == null) {
            return Map.of();
        }
        Class<?> targetClass = target.getClass();
        Map<String, String> map = definitionsCache.get(targetClass);
        if (map == null) {
            Field[] fields = targetClass.getDeclaredFields();
            map = new HashMap<>(fields.length);
            for (Field field : fields) {
                ParamName annotation = field.getAnnotation(ParamName.class);
                if (annotation != null && !annotation.value().isEmpty()) {
                    map.put(annotation.value(), field.getName());
                }
            }
            definitionsCache.putIfAbsent(targetClass, map);
            return map;
        } else {
            return map;
        }
    }

    /**
     * Copied from WebDataBinder.
     *
     * @param multipartFiles
     * @param mpvs
     */
    protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs) {
        for (Map.Entry<String, List<MultipartFile>> entry : multipartFiles.entrySet()) {
            String key = entry.getKey();
            List<MultipartFile> values = entry.getValue();
            if (values.size() == 1) {
                MultipartFile value = values.get(0);
                if (!value.isEmpty()) {
                    mpvs.add(key, value);
                }
            } else {
                mpvs.add(key, values);
            }
        }
    }

    public static LocalDate toJavaLocalDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ofPattern(pattern));
    }
}
