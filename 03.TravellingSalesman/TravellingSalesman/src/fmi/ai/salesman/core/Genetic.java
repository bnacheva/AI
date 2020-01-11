package fmi.ai.salesman.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Genetic {
    private int populationSize; // the number of genomes in each generation
    private int genomeSize; // the length of the genome = numberOfCities - 1
    private int numberOfCities;
    private int reproductionSize; // the number of genomes who'll be selected to reproduce to make the next generation
    private int maxIterations; // the maximum number of generations the program will evolve before terminating
    private float mutationRate; // the frequency of mutations when creating a new generation
    private int[][] travelPrices;
    private int startingCity;
    private int targetFitness;

    @Contract(pure = true)
    public Genetic() {
        this.numberOfCities = 0;
        this.genomeSize = 0;
        this.travelPrices = new int[this.numberOfCities][this.numberOfCities];
        this.startingCity = 0;
        this.targetFitness = 0;
        this.populationSize = 5000;
        this.reproductionSize = 200;
        this.maxIterations = 1000;
        this.mutationRate = 0.1f;
    }

    @Contract(pure = true)
    public Genetic(int numberOfCities, int[][] travelPrices, int startingCity, int targetFitness) {
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities - 1;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;
        this.populationSize = 5000;
        this.reproductionSize = 200;
        this.maxIterations = 1000;
        this.mutationRate = 0.1f;
    }

    // generates a random cost path between each two cities
    public static void generateTravelPrices(int[][] travelPrices, int numberOfCities) {
        for(int i = 0; i < numberOfCities; i++){
            for(int j = 0; j <= i; j++) {
                Random rand = new Random();
                if(i == j) {
                    travelPrices[i][j] = 0;
                } else {
                    travelPrices[i][j] = rand.nextInt(100);
                    travelPrices[j][i] = travelPrices[i][j];
                }
            }
        }
    }

    // prints the cost path between each two cities
    public static void printTravelPrices(int[][] travelPrices, int numberOfCities) {
        for(int i = 0; i < numberOfCities; i++){
            for(int j = 0; j < numberOfCities; j++) {
                System.out.print(travelPrices[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // generates the initial population
    public List<Genome> initialPopulation() {
        List<Genome> population = new ArrayList<>();
        for(int i = 0; i < this.populationSize; i++) {
            Genome genome = new Genome(this.numberOfCities, this.travelPrices, this.startingCity);
            population.add(genome);
        }
        return population;
    }

    // gets the total fitness value of the current population
    public static int getFitnessPopulation(@NotNull List<Genome> population) {
        int totalFitness = 0;
        for (Genome genome : population) {
            totalFitness += genome.getFitness();
        }
        return totalFitness;
    }

    // returns the "best" genome of the current population
    public Genome rouletteSelection(List<Genome> population) {
        // get the total fitness value of the current population
        int totalFitness = getFitnessPopulation(population);
        Random random = new Random();
        // selects a random fitness value from all of the genomes in the population
        int selectedValue = random.nextInt(totalFitness);
        float recValue = (float) 1/selectedValue;
        float currentSum = 0;
        for(Genome genome : population) {
            currentSum += (float) 1/genome.getFitness();
            if(currentSum >= recValue){
                return genome;
            }
        }
        int selectRandom = random.nextInt(this.populationSize);
        return population.get(selectRandom);
    }

    // selects the next population which will reproduce
    public List<Genome> selection(List<Genome> population) {
        List<Genome> selected = new ArrayList<>();
        for(int i = 0; i < this.reproductionSize; i++) {
            selected.add(rouletteSelection(population));
        }
        return selected;
    }

    // Because the algorithm is based on randomness,
    // it's possible for it to accidentally converge on a wrong solution.
    // To avoid that, we randomly perform mutation on a small percentage of our genomes
    // to increase the likelihood that we'll find the right solution.
    public Genome mutate(Genome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if(mutate < mutationRate) {
            List<Integer> genome = salesman.getGenome();
            // swaps two cities of the current genome
            Collections.swap(genome, random.nextInt(this.genomeSize), random.nextInt(this.genomeSize));
            return new Genome(genome, this.numberOfCities, this.travelPrices, this.startingCity);
        }
        return salesman;
    }

    // picks random elements from a list
    @Nullable
    public static List<Genome> pickRandomElements(@NotNull List<Genome> list, int n) {
        Random r = new Random();
        int length = list.size();
        if (length < n) return null;
        for (int i = length - 1; i >= length - n; --i)
        {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    // creates the next generation
    public List<Genome> createGeneration(List<Genome> population) {
        List<Genome> generation = new ArrayList<>();
        int currentPopulationSize = 0;
        while(currentPopulationSize < this.populationSize) {
            // picks two random genomes from the population
            List<Genome> parents = pickRandomElements(population,2);
            // crossovers the parents to get the children
            List<Genome> children = crossover(parents);
            // mutates the children
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            // add the children to the new generation
            generation.addAll(children);
            currentPopulationSize += 2;
        }
        return generation;
    }

    public List<Genome> crossover(@NotNull List<Genome> parents) {
        Random random = new Random();
        int breakpoint = random.nextInt(this.genomeSize);
        List<Genome> children = new ArrayList<>();

        // copy parental genomes - we copy so we wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());

        // creating child 1
        for(int i = 0; i < breakpoint; i++) {
            int newVal = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newVal), i);
        }
        children.add(new Genome(parent1Genome, this.numberOfCities, this.travelPrices, this.startingCity));
        parent1Genome = parents.get(0).getGenome(); // resetting the edited parent

        // creating child 2
        for(int i = breakpoint; i < this.genomeSize; i++) {
            int newVal = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newVal), i);
        }
        children.add(new Genome(parent2Genome, this.numberOfCities, this.travelPrices, this.startingCity));

        return children;
    }

    public Genome geneticAlgorithm() {
        List<Genome> population = initialPopulation();
        Genome globalBestGenome = population.get(0);
        for(int i = 0; i < this.maxIterations; i++) {
            if (i == 10 || i == 200 || i == 300 || i == 400) {
                System.out.println("The " + i + " generation: ");
                System.out.println(globalBestGenome);
                System.out.println();
            }
            List<Genome> selected = selection(population);
            population = createGeneration(selected);
            // picks the best genome after the manipulations
            globalBestGenome = Collections.min(population);
            if(globalBestGenome.getFitness() < this.targetFitness)
                break;
        }
        System.out.println("The last generation: ");
        return globalBestGenome;
    }

    public void printGeneration(@NotNull List<Genome> generation) {
        for(Genome genome : generation){
            System.out.println(genome);
        }
        System.out.println();
    }
}
