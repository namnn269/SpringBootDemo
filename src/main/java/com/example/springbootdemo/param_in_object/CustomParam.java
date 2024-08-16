package com.example.springbootdemo.param_in_object;

import com.example.springbootdemo.config.url_param_handler.QueryParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CustomParam {

    @QueryParam("full_name")
    private String name;

    @QueryParam("detail_address")
    private List<String> address;

    @DateTimeFormat(pattern = "yyyy-dd-MM")
    @QueryParam("my_local_date")
    private LocalDate localDate;

    @JsonIgnore
    private List<MultipartFile> myFile;

    @JsonIgnore
    private MultipartFile myFile2;

}
