package com.example.springbootdemo.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String input = "__@gmail.poab.loab.olabc.vn";

        Pattern pattern = Pattern.compile("(lo|ol)ab(^\\.n)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println("OK");
            System.out.println("==> :" + matcher.group());
            System.out.println(matcher.start() + "-->" + matcher.end());
        }
    }
}

