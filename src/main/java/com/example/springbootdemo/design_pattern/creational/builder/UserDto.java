package com.example.springbootdemo.design_pattern.creational.builder;

public class UserDto {
    private String name;
    private int age;

    public UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private String name;
        private int age;

        public UserDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserDtoBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserDto build() {
            return new UserDto(this.name, this.age);
        }
    }
}
