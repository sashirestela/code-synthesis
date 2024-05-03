package com.example;

/**
 * This class provides basic mathematical operations such as addition, subtraction, multiplication, and division.
 */
public class Calculator {

    /**
     * Adds two integers.
     *
     * @param a the first integer
     * @param b the second integer
     * @return the sum of a and b
     */
    public static int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts one integer from another.
     *
     * @param a the first integer
     * @param b the integer to subtract from a
     * @return the result of a minus b
     */
    public static int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two integers.
     *
     * @param a the first integer
     * @param b the second integer
     * @return the product of a and b
     */
    public static int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides one integer by another.
     *
     * @param a the dividend
     * @param b the divisor
     * @return the quotient of a and b
     * @throws IllegalArgumentException if b is zero
     */
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}