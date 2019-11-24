package fmi.ai.pruning.core;

import fmi.ai.pruning.core.board.Board;
import fmi.ai.pruning.core.board.Position;
import fmi.ai.pruning.core.board.TicTacToe;
import fmi.ai.pruning.core.enums.Marker;
import fmi.ai.pruning.core.enums.State;

import java.util.Scanner;

public class Game {

    private Board board = new Board();

    public Game() { }

    public void start(boolean humanFirst) {
        Scanner scanner = new Scanner(System.in);
        if(humanFirst) {
            while (this.board.getState() == State.UNFINISHED) {
                System.out.println("Enter position: ");
                String[] position = scanner.nextLine().split(",");
                // human input
                Position inputPosition = new Position(Integer.parseInt(position[0]) - 1,
                        Integer.parseInt(position[1]) - 1);
                if (this.board.getBoard()[inputPosition.getRow()][inputPosition.getCol()] == Marker.EMPTY) {
                    this.board.setMarker(Marker.X, inputPosition);
                } else {
                    throw new IllegalArgumentException("This space is already filled!");
                }

                System.out.println("Human moves:");
                this.board.print();

                if (this.board.getState() == State.UNFINISHED) {
                    // ai input
                    Position aiPosition = TicTacToe.getNextMoveFromAI(board, Marker.O);
                    if (this.board.getBoard()[aiPosition.getRow()][aiPosition.getCol()] == Marker.EMPTY) {
                        this.board.setMarker(Marker.O, aiPosition);
                    } else {
                        throw new IllegalArgumentException("This space is already filled!");
                    }

                    System.out.println("AI moves:");
                    this.board.print();
                } else {
                    break;
                }
            }
        } else {
            while (this.board.getState() == State.UNFINISHED) {
                // ai input
                Position aiPosition = TicTacToe.getNextMoveFromAI(board, Marker.X);
                if (this.board.getBoard()[aiPosition.getRow()][aiPosition.getCol()] == Marker.EMPTY) {
                    this.board.setMarker(Marker.X, aiPosition);
                } else {
                    throw new IllegalArgumentException("This space is already filled!");
                }

                System.out.println("AI moves:");
                this.board.print();

                if (this.board.getState() == State.UNFINISHED) {
                    System.out.println("Enter position: ");
                    String[] position = scanner.nextLine().split(",");
                    // human input
                    Position inputPosition = new Position(Integer.parseInt(position[0]) - 1,
                            Integer.parseInt(position[1]) - 1);
                    if (this.board.getBoard()[inputPosition.getRow()][inputPosition.getCol()] == Marker.EMPTY) {
                        this.board.setMarker(Marker.O, inputPosition);
                    } else {
                        throw new IllegalArgumentException("This space is already filled!");
                    }

                    System.out.println("Human moves:");
                    this.board.print();
                } else {
                    break;
                }
            }
        }

        printResults();
    }

    private void printResults() {
        System.out.println("Final state: ");
        this.board.print();

        switch (this.board.getState()) {
            case EVEN:
                System.out.println("Even!");
                break;
            case X_WIN:
                System.out.println("X won!");
                break;
            case O_WIN:
                System.out.println("O won!");
                break;
            default: throw new RuntimeException();
        }
    }
}
