package fmi.ai.queens.core;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the number of queens: ");
        int queens = scanner.nextInt();
        QueenBoard queenBoard = new QueenBoard(queens);
        long start = System.currentTimeMillis();
        queenBoard.minConflicts();
        long stop = System.currentTimeMillis();
        System.out.println("Found in " + ((double)(stop-start))/1000 + "s.");
        queenBoard.printBoard();
    }
}
