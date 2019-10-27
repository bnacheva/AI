package fmi.ai.puzzle.core;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the size of the initial board: ");
        int dim = scanner.nextInt();
        if (dim < 8 || ((dim + 1) % (int)Math.sqrt(dim + 1)) != 0) {
            System.out.println("Wrong input size!");
            return;
        }
        System.out.println("Type the initial position of 0: ");
        int position = scanner.nextInt();
        if (position < -1 || position > 7) {
            System.out.println("Wrong input position!");
            return;
        }
        int size = (int)Math.sqrt(dim + 1);
        Board initialBoard = new Board(size);
        initialBoard.printBoard();
        Board finalBoard = new Board();
        finalBoard.finalBoard(position, size);
        finalBoard.printBoard();
        System.out.println();
        System.out.println("The manhattan distance: " + initialBoard.manhattan(initialBoard, finalBoard));
        System.out.println();
        System.out.println("Is the board solvable: " + initialBoard.isSolvable(initialBoard));
        System.out.println();
        System.out.println("The minimum steps to the final board: " + initialBoard.solution(initialBoard, finalBoard));
    }
}
