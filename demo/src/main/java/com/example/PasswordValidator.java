package com.example;

public class PasswordValidator {
    private static final int MIN_PASSWORD_LENGTH = 8;

    public static boolean isValidPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasNumber = password.chars().anyMatch(Character::isDigit);
        return hasUpperCase && hasLowerCase && hasNumber;
    }

    public static void main(String[] args) {
        String password = "v3ryStrongP@ss";
        boolean valid = isValidPassword(password);
        System.out.println("Password is valid: " + valid);
    }
}