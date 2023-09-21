package com.example.springbootdemo.param_in_object;

import com.example.springbootdemo.annotation.param.ParamName;
import com.example.springbootdemo.annotation.param.SupportsParameterResolution;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@SupportsParameterResolution
@Data
public class DemoParam {

    @ParamName("nameInAnnotation")
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @ParamName("addressInAnnotation")

    @NotBlank
    @NotEmpty
    private String address;

}
