package fmi.ai.salesman.core;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the number of cities: ");
        int numberCities = scanner.nextInt();
        if (numberCities < 0 || numberCities > 100) {
            throw new IllegalArgumentException("The number of cities must be between 0 and 100!");
        } else {
            int[][] travelPrices = new int[numberCities][numberCities];
            Genetic.generateTravelPrices(travelPrices, numberCities);
            Genetic.printTravelPrices(travelPrices, numberCities);
            final int startingCity = 0;
            final int targetFitness = 0;
            Genetic solution = new Genetic(numberCities, travelPrices, startingCity, targetFitness);
            Genome result = solution.geneticAlgorithm();
            System.out.println(result);
        }
    }
}
