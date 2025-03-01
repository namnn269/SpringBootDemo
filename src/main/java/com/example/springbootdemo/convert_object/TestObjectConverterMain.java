package com.example.springbootdemo.convert_object;

import com.example.springbootdemo.annotation.convert_object.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class TestObjectConverterMain {
    static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Object source = """
                {
                    "name" : "my tar get",
                    "a" : {
                        "color" : "blue",
                        "year" : 2020
                    },
                    "b" : {
                        "diameter" : 10,
                        "shape" : {
                            "x" : 55,
                            "y" : 66
                        }
                    }
                }
                """;
        source = Map.of(
                "name", "aa",
                "a", Map.of("color", "red", "year", 2222),
                "b", Map.of("diameter", 99, "shape", Map.of("x", 77, "y", 88))
        );


        Method method = TestObjectConverterMain.class.getDeclaredMethod("handle", Object.class);
        ObjectConverter annotation = method.getAnnotation(ObjectConverter.class);

        if (annotation == null) {
            return;
        }

        JavaType targetClass = targetType(annotation);

        Object targetObject = null;

        if (source instanceof String s) {
            try {
                targetObject = objectMapper.readValue(s, targetClass);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println(targetObject);
        } else {
            targetObject = objectMapper.convertValue(source, targetClass);
            System.out.println(targetObject);
        }
        System.out.println("==========");
    }

    private static JavaType targetType(ObjectConverter annotation) {
        Class<?> mainClass = annotation.value();
        JavaType[] genericLv1 = Arrays.stream(annotation.genericLevel1())
                .map(TestObjectConverterMain::lv1)
                .toArray(JavaType[]::new);
        return objectMapper.getTypeFactory().constructParametricType(mainClass, genericLv1);
    }

    private static JavaType lv1(GenericConverterLevel1 lv1GenericAnnotation) {
        Class<?> lv1Class = lv1GenericAnnotation.value();
        JavaType[] lv2Classes = Arrays.stream(lv1GenericAnnotation.genericLevel2())
                .map(TestObjectConverterMain::lv2)
                .toArray(JavaType[]::new);
        return objectMapper.getTypeFactory().constructParametricType(lv1Class, lv2Classes);
    }

    private static JavaType lv2(GenericConverterLevel2 lv2GenericAnnotation) {
        Class<?> lv2Class = lv2GenericAnnotation.value();
        JavaType[] lv3Classes = Arrays.stream(lv2GenericAnnotation.genericLevel3())
                .map(TestObjectConverterMain::lv3)
                .toArray(JavaType[]::new);
        return objectMapper.getTypeFactory().constructParametricType(lv2Class, lv3Classes);
    }

    private static JavaType lv3(GenericConverterLevel3 lv3GenericAnnotation) {
        Class<?> lv3Class = lv3GenericAnnotation.value();
        Class<?>[] lv4Classes = Arrays.stream(lv3GenericAnnotation.genericLevel4())
                .map(GenericConverterLevel4::value)
                .toArray(Class[]::new);
        return objectMapper.getTypeFactory().constructParametricType(lv3Class, lv4Classes);
    }


    @ObjectConverter(
            value = TargetObject.class,
            genericLevel1 = {
                    @GenericConverterLevel1(
                            value = MyHouse.class
                    ),
                    @GenericConverterLevel1(
                            value = MyBall.class,
                            genericLevel2 = {
                                    @GenericConverterLevel2(value = MyCircle.class)
                            })
            }
    )
    public Object handle(Object source) {
        TargetObject<MyHouse, MyBall<MyCircle>> targetObject1 = new TargetObject<>();
        return source;
    }
}

