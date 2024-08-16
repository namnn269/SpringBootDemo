package com.example.springbootdemo.junittest;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator cal = new Calculator();

    @Test
    void tenPlusTenShouldBeTwenty() {
        assertThat(cal.add(20, 20)).isEqualTo(40);
    }

    @Test
    void twoPlusThreeShouldBeFive() {
        assertEquals(5, cal.add(2, 3));
    }

    @Test
    void tenPlusFiveShouldBeFifteen() {
        assertEquals(15, cal.add(10, 5));
    }

    @Test
    void sevenPlusSevenShouldBeDifferentTen() {
        assertNotEquals(10, cal.add(7, 7));
    }

}