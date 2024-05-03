package com.example;

public class Main {

    public static void main(String[] args) {
        // Define an array of numbers
        int[] numbers = { 1, 2, 3, 4, 5 };

        // Iterate over the array and print each number
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
        System.out.println();

        // Summing up all the numbers in the array
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        System.out.println("Sum of all numbers: " + sum);

        // Finding the maximum or minimum value in the array
        int max = numbers[0];
        int min = numbers[0];

        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        System.out.println("Max value in the array: " + max);
        System.out.println("Min value in the array: " + min);

        // Sorting the array in descending order
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] < numbers[j]) {
                    int temp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = temp;
                }
            }
        }
        System.out.print("Array sorted in descending order: ");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
        System.out.println();

        // Generating a new array with squared values of the original numbers
        int[] squaredNumbers = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            squaredNumbers[i] = numbers[i] * numbers[i];
        }
        System.out.print("Squared values of the numbers: ");
        for (int i = 0; i < squaredNumbers.length; i++) {
            System.out.print(squaredNumbers[i] + " ");
        }
        System.out.println();

        // Filtering the array to only include even or odd numbers
        int[] evenNumbers = new int[numbers.length];
        int[] oddNumbers = new int[numbers.length];
        int evenCount = 0;
        int oddCount = 0;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] % 2 == 0) {
                evenNumbers[evenCount++] = numbers[i];
            } else {
                oddNumbers[oddCount++] = numbers[i];
            }
        }

        System.out.print("Even numbers in the array: ");
        for (int i = 0; i < evenCount; i++) {
            System.out.print(evenNumbers[i] + " ");
        }
        System.out.println();

        System.out.print("Odd numbers in the array: ");
        for (int i = 0; i < oddCount; i++) {
            System.out.print(oddNumbers[i] + " ");
        }
        System.out.println();
        
    }
}
