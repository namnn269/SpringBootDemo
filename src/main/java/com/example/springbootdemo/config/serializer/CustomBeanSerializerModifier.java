package com.example.springbootdemo.config.serializer;

import com.example.springbootdemo.annotation.serializer.ChangeValue;
import com.example.springbootdemo.annotation.serializer.Hidden;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.List;

public class CustomBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        BeanPropertyWriter hidden = beanProperties.stream()
                .filter(bean -> bean.getAnnotation(Hidden.class) != null)
                .findFirst()
                .orElse(null);
        if (hidden == null) {
            return beanProperties;
        }

        for (BeanPropertyWriter writer : beanProperties) {
            ChangeValue annotation = writer.getAnnotation(ChangeValue.class);
            if (annotation == null) {
                continue;
            }
            AnnotatedMember member = hidden.getMember();
            writer.assignSerializer(new MyConditionalSerializer(member));
        }

        return beanProperties;
    }

    public static class MyConditionalSerializer extends JsonSerializer<Object> {

        private final AnnotatedMember annotatedMember;

        public MyConditionalSerializer(AnnotatedMember annotatedMember) {
            super();
            this.annotatedMember = annotatedMember;
        }

        @Override
        public void serialize(Object value,
                              JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            Object obj = gen.getOutputContext().getCurrentValue();
            Object value1 = annotatedMember.getValue(obj);
            if ((boolean) value1)
                gen.writeString(value + "** string");
            else
                gen.writeString(String.valueOf(value));
        }
    }
}
