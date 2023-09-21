package com.example.springbootdemo.enums;

import lombok.Data;


public enum Color {

    RED(1, 2, 3),
    GREEN(4, 5, 6),
    BLUE(7, 8, 9);

    private final int r;
    private final int g;
    private final int b;
    private int d;

    Color(int r, int g, int b) {
        System.out.println("in constructor" + r);
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getD() {
        return d;
    }

    @Override
    public String toString() {
        return this.name() + "(" + r + ", " + b + ", " + g + ")";
    }
}
