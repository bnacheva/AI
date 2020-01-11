package fmi.ai.salesman.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genome implements Comparable {
    List<Integer> genome;
    int[][] travelPrices;
    int startingCity;
    int numberOfCities;
    int fitness;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path: ");
        sb.append(this.startingCity);
        for (int gene: this.genome) {
            sb.append(" ");
            sb.append(gene);
        }
        sb.append(" ");
        sb.append(this.startingCity);
        sb.append("\nPath length: ");
        sb.append(this.fitness);
        return sb.toString();
    }


    @Override
    // compares two genomes to their total fitness values
    public int compareTo(Object o) {
        Genome genome = (Genome) o;
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }

    public List<Integer> getGenome() {
        return this.genome;
    }

    public int[][] getTravelPrices() {
        return this.travelPrices;
    }

    public int getStartingCity() {
        return this.startingCity;
    }

    public int getNumberOfCities() {
        return this.numberOfCities;
    }

    public int getFitness() {
        return this.fitness;
    }

    public void setGenome(List<Integer> genome) {
        this.genome = genome;
    }

    public void setTravelPrices(int[][] travelPrices) {
        this.travelPrices = travelPrices;
    }

    public void setStartingCity(int startingCity) {
        this.startingCity = startingCity;
    }

    public void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Contract(pure = true)
    public Genome() {
        this.genome = new ArrayList<>();
        this.travelPrices = new int[this.numberOfCities][this.numberOfCities];
        this.startingCity = 0;
        this.numberOfCities = 0;
        this.fitness = 0;
    }

    @NotNull
    private List<Integer> randomGenome() {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < this.numberOfCities; i++) {
            if(i != this.startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    public Genome(int numberOfCities, int[][] travelPrices, int startingCity) {
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.genome = randomGenome();
        this.fitness = this.calculateFitness();
    }

    public Genome(List<Integer> permutationOfCities, int numberOfCities, int[][] travelPrices, int startingCity) {
        this.genome = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.fitness = this.calculateFitness();
    }

    public int calculateFitness() {
        int fitness = 0;
        int currentCity = this.startingCity;
        // calculates the path cost
        for (int gene : this.genome) {
            // calculates the distance between each two cities
            fitness += this.travelPrices[currentCity][gene];
            currentCity = gene;
        }
        // adds the distance between the final city and the starting city
        fitness += this.travelPrices[this.genome.get(this.numberOfCities-2)][this.startingCity];
        return fitness;
    }
}
