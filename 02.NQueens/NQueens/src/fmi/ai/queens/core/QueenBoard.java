package fmi.ai.queens.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QueenBoard {
    private int[] rows;
    private int[] queenRows; // contains the column on each row for each queen (x, y)
    private int[] queenMainDiagonals; // contains the column on each main diagonal for each queen (x, y)
    private int[] queenSecondaryDiagonals; // contains the column on each secondary diagonal for each queen (x, y)

    // creates a new board
    public QueenBoard(int queens) {
        this.rows = new int[queens];
        this.queenRows = new int[queens];
        this.queenMainDiagonals = new int[queens * 2 - 1]; // all of them
        this.queenSecondaryDiagonals = new int[queens * 2 - 1]; // all of them
        this.shuffle();
    }

    // randomly fills the board with one queen in each column
    public void shuffle() {
        for (int i = 0 ; i < this.rows.length * 2 - 1; i++) {
            this.queenMainDiagonals[i] = 0;
            this.queenSecondaryDiagonals[i] = 0;
        }
        for (int i = 0; i < this.rows.length; i++) {
            this.queenRows[i] = 0;
            rows[i] = i;
        }
        List<Integer> list = new ArrayList<>();
        for (int i : this.rows) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            this.rows[i] = list.get(i);
            this.queenRows[this.rows[i]] += 1;
            this.queenMainDiagonals[rows[i] + i] += 1;
            if (rows[i] <= i) {
                this.queenSecondaryDiagonals[(this.rows.length - 1) - Math.abs(rows[i] - i)] += 1;
            } else {
                this.queenSecondaryDiagonals[(this.rows.length - 1) + Math.abs(rows[i] - i)] += 1;
            }
        }
    }

    // prints the board
    public void printBoard() {
        for (int i = 0; i < this.rows.length; i++) {
            for (int j = 0; j < this.rows.length; j++) {
                if (this.rows[j] == i) {
                    System.out.print('*' + " ");
                } else {
                    System.out.print('_' + " ");
                }
            }
            System.out.println();
        }
    }

    // finds the number of conflicts for each queen
    public int numConflicts(int row, int col) {
        int secondaryDiagonalIndex = 0;
        if (row > col){
            secondaryDiagonalIndex = this.rows.length - 1 + Math.abs(row - col);
        } else {
            secondaryDiagonalIndex = this.rows.length - 1 - Math.abs(row - col);
        }

        if (this.rows[col] == row) {
            return this.queenRows[row] + this.queenMainDiagonals[row + col] + this.queenSecondaryDiagonals[secondaryDiagonalIndex] - 3;
        }

        return this.queenRows[row] + this.queenMainDiagonals[row + col] + this.queenSecondaryDiagonals[secondaryDiagonalIndex];
    }

    // finds the solution
    public void minConflicts() {
        int steps = 0;
        ArrayList<Integer> queens = new ArrayList<>();
        Random random = new Random();
        while (true) {
            // finds the queen with maximum conflicts
            int maxConflicts = 0;
            queens.clear();
            for (int i = 0; i < this.rows.length; i++) {
                // current queen conflicts
                int numConflictsCurrent = numConflicts(this.rows[i], i);
                if (numConflictsCurrent > maxConflicts) {
                    maxConflicts = numConflictsCurrent;
                    queens.clear();
                    queens.add(i);
                } else if (numConflictsCurrent == maxConflicts) {
                    queens.add(i);
                }
            }

            // returns if there are no conflicts
            if (maxConflicts == 0) {
                return;
            }

            // gets the position of one of the worst queens
            int randomWorstQueen = queens.get(random.nextInt(queens.size()));

            // finds the queen with maximum conflicts
            int minConflicts = this.rows.length;
            queens.clear();
            for (int i = 0; i < this.rows.length; i++) {
                // current queen conflicts
                int numConflictsCurrent = numConflicts(i, randomWorstQueen);
                if (numConflictsCurrent < minConflicts) {
                    minConflicts = numConflictsCurrent;
                    queens.clear();
                    queens.add(i);
                } else if (numConflictsCurrent == minConflicts) {
                    queens.add(i);
                }
            }

            // moves the queen with the most conflicts to the place of the queen with the least conflicts
            if (!queens.isEmpty()) {
                this.queenRows[this.rows[randomWorstQueen]] -= 1;
                this.queenMainDiagonals[this.rows[randomWorstQueen] + randomWorstQueen] -= 1;
                if (this.rows[randomWorstQueen] <= randomWorstQueen) {
                    this.queenSecondaryDiagonals[(this.rows.length - 1) - Math.abs(this.rows[randomWorstQueen] - randomWorstQueen)] -= 1;
                } else {
                    this.queenSecondaryDiagonals[(this.rows.length - 1) + Math.abs(this.rows[randomWorstQueen] - randomWorstQueen)] -= 1;
                }
                this.rows[randomWorstQueen] =
                        queens.get(random.nextInt(queens.size()));
                this.queenRows[this.rows[randomWorstQueen]] += 1;
                this.queenMainDiagonals[this.rows[randomWorstQueen] + randomWorstQueen] += 1;
                if (this.rows[randomWorstQueen] <= randomWorstQueen) {
                    this.queenSecondaryDiagonals[(this.rows.length - 1) - Math.abs(this.rows[randomWorstQueen] - randomWorstQueen)] += 1;
                } else {
                    this.queenSecondaryDiagonals[(this.rows.length - 1) + Math.abs(this.rows[randomWorstQueen] - randomWorstQueen)] += 1;
                }
            }

            steps++;
            if (steps == this.rows.length * 2) {
                this.shuffle();
                steps = 0;
            }
        }
    }
}
