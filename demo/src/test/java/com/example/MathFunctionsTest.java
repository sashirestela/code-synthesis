package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathFunctionsTest {

    @Test
    public void testAdd() {
        assertEquals(5, MathFunctions.add(2, 3));
        assertEquals(-1, MathFunctions.add(2, -3));
        assertEquals(0, MathFunctions.add(2, -2));
        assertEquals(Integer.MAX_VALUE, MathFunctions.add(Integer.MAX_VALUE, 0));
        assertEquals(-Integer.MAX_VALUE, MathFunctions.add(-Integer.MAX_VALUE, 0));
    }

    @Test
    public void testSubtract() {
        assertEquals(-1, MathFunctions.subtract(2, 3));
        assertEquals(5, MathFunctions.subtract(2, -3));
        assertEquals(0, MathFunctions.subtract(2, 2));
        assertEquals(Integer.MAX_VALUE, MathFunctions.subtract(Integer.MAX_VALUE, 0));
        assertEquals(-Integer.MAX_VALUE, MathFunctions.subtract(-Integer.MAX_VALUE, 0));
    }

    @Test
    public void testMultiply() {
        assertEquals(6, MathFunctions.multiply(2, 3));
        assertEquals(-6, MathFunctions.multiply(2, -3));
        assertEquals(0, MathFunctions.multiply(2, 0));
        assertEquals(0, MathFunctions.multiply(0, 2));
        assertEquals(Integer.MAX_VALUE, MathFunctions.multiply(Integer.MAX_VALUE, 1));
        assertEquals(-Integer.MAX_VALUE, MathFunctions.multiply(-Integer.MAX_VALUE, 1));
    }

    @Test
    public void testDivide() {
        assertEquals(2, MathFunctions.divide(6, 3));
        assertEquals(-2, MathFunctions.divide(6, -3));
        assertEquals(0, MathFunctions.divide(0, 3));
        assertThrows(IllegalArgumentException.class, () -> MathFunctions.divide(2, 0));
        assertEquals(Integer.MAX_VALUE, MathFunctions.divide(Integer.MAX_VALUE, 1));
        assertEquals(-Integer.MAX_VALUE, MathFunctions.divide(-Integer.MAX_VALUE, 1));
    }
}