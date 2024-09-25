package com.example.springbootdemo.dto;

import com.example.springbootdemo.annotation.valid_annotation.CountCompareDB;
import com.example.springbootdemo.annotation.valid_annotation.MaxListSize;
import com.example.springbootdemo.annotation.valid_annotation.StartString;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidForm {
    private Integer recordId;

    @NotNull(message = "Name must be not null :vvv")
    @NotEmpty(message = "__{not.empty}", groups = GroupClass1.class)
    @StartString
    private String name;
    @NotNull(groups = GroupClass1.class, payload = Level1.class)
    @Min(0)
    @Max(10)
    private Integer age;

    @CountCompareDB(groups = {GroupClass1.class, GroupClass2.class}, payload = Level2.class)
    private Integer count;

    @MaxListSize(size = 666, groups = GroupClass2.class, payload = Level1.class)
    List<Integer> list;

    @NotNull(payload = Level2.class)
    @Valid
    Detail detail;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private String address;
        private LocalDate localDate;
    }

    public interface GroupClass1 {
    }

    public interface GroupClass2 {
    }

    public static class Level1 implements Payload {
    }

    public static class Level2 implements Payload {
    }
}
