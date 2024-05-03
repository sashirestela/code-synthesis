package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TSPSolver {

    // Method to solve the TSP using the Nearest Neighbor Algorithm
    public static List<Integer> solveTSP(int[][] distances) {
        List<Integer> tour = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        // Start from city 0
        int currentCity = 0;
        tour.add(currentCity);
        visited.add(currentCity);

        while (visited.size() < distances.length) {
            int nextCity = findNearestNeighbor(currentCity, visited,
                    distances);
            tour.add(nextCity);
            visited.add(nextCity);
            currentCity = nextCity;
        }

        // Return to the starting city
        tour.add(0);
        return tour;
    }

    // Method to find the nearest unvisited neighbor of a given city
    private static int findNearestNeighbor(int currentCity, Set<Integer> visited, int[][] distances) {
        int nearestNeighbor = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int city = 0; city < distances.length; city++) {
            if (!visited.contains(city) && distances[currentCity][city] < minDistance) {
                nearestNeighbor = city;
                minDistance = distances[currentCity][city];
            }
        }
        return nearestNeighbor;
    }

}
