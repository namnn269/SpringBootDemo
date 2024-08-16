package com.example.springbootdemo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMarker {
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "a111");
        map.put("list", List.of(1, 2, 3, 4, 5));
        map.put("map", Map.of("name", "nam", "age", 88));

        Writer writer = new StringWriter();

        FileReader reader = new FileReader("src/main/resources/text/text1.txt");
        Template template = new Template("a1", reader, configuration);
        template.process(map, writer);
        System.out.println(writer);
    }
}

