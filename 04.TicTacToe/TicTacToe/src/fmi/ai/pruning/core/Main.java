package fmi.ai.pruning.core;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to start first? (y/n)");
        boolean humanFirst = scanner.nextLine().contains("y");
        game.start(humanFirst);
    }
}
