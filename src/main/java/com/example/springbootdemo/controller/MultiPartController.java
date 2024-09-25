package com.example.springbootdemo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/multipart-file")
public class MultiPartController {

    @PostMapping
    public Object importFile(@RequestPart(name = "file") List<MultipartFile> files,
                             Metadata metadata,
                             @RequestHeader HttpHeaders headers) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        for (MultipartFile file : files) {
            builder.part("file", file.getResource(), MediaType.MULTIPART_FORM_DATA);
        }
        builder.part("file", new FileSystemResource("src/main/resources/image/image_1.png"), MediaType.MULTIPART_FORM_DATA);
        builder.part("name", metadata.getName());
        builder.part("address", metadata.getAddress(), MediaType.TEXT_PLAIN);

        MultiValueMap<String, HttpEntity<?>> body = builder.build();
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(body);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Metadata> response = restTemplate.exchange(
                "http://localhost:8080/multipart-file/send-from-server",
                HttpMethod.POST,
                httpEntity,
                Metadata.class);
        return response.getBody();
    }

    @PostMapping("/send-from-server")
    public Object importFileFromServer(@RequestPart(name = "file") List<MultipartFile> files,
                                       Metadata metadata,
                                       @RequestHeader HttpHeaders headers) {
        metadata.setName(metadata.getName() + " from service B");
        metadata.setAddress(metadata.getAddress() + " from service B");
        return metadata;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String name;
        private String address;
    }
}
