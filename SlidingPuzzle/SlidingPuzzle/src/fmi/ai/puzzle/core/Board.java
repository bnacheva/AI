package fmi.ai.puzzle.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Board {
    private int[][] tiles;
    private int size;

    @Contract(pure = true)
    Board() {
        this.tiles = null;
        this.size = 0;
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    Board(int size) {
        this.size = size;
        this.tiles = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Type board[" + i + "][" + j + "] = ");
                int num = scanner.nextInt();
                this.tiles[i][j] = num;
            }
        }
    }

    // copy the current board
    private Board(int[][] tiles) {
        this.size = tiles.length;
        this.tiles = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // create the final board
    void finalBoard(int posZero, int size) {
        this.size = size;
        this.tiles = new int[this.size][this.size];
        int num = 1;
        if (posZero == -1) {
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    this.tiles[i][j] = num;
                    num ++;
                }
            }
            this.tiles[this.size - 1][this.size - 1] = 0;
        } else {
            int[] tempArray = new int[this.size*this.size];
            int tempIndex = 0;
            for (int i = 0; i < this.size * this.size; i++) {
                if (i == posZero) {
                    tempArray[tempIndex] = 0;
                    tempIndex ++;
                }
                else {
                    tempArray[tempIndex] = num;
                    num ++;
                    tempIndex ++;
                }
            }
            tempIndex = 0;
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    this.tiles[i][j] = tempArray[tempIndex];
                    tempIndex++;
                }
            }
        }
    }

    // tile at (row, col) or 0 if blank
    int tileAt(int row, int col) {
        try {
            return this.tiles[row][col];
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Row and col must be between 0 and n − 1.");
        }
    }

    // board size n
    int size() {
        return this.size;
    }

    // returns the X or Y coordinate of a tile
    private int posXY(@NotNull Board a, int num, boolean isRow) {
        this.size = a.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == num) {
                    if (isRow)
                        return i;
                    else
                        return j;
                }
            }
        }
        return -1;
    }

    // sum of Manhattan distances between tiles and goal
    int manhattan(@NotNull Board a, Board b) {
        boolean isRow = true;
        this.size = a.size;
        int distance = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] != b.tiles[i][j] && a.tiles[i][j] != 0) {
                    int distanceTile = Math.abs(a.posXY(a, a.tiles[i][j], false) - b.posXY(b, a.tiles[i][j], false)) +
                            Math.abs(a.posXY(a, a.tiles[i][j], true) - b.posXY(b, a.tiles[i][j], true));
                    distance += distanceTile;
                }
            }
        }
        return distance;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof Board)) return false;
        return equals(this, (Board) obj);
    }

    // does this board equal y?
    boolean equals(@NotNull Board x, @NotNull Board y) {
        if (x.size() != y.size()) {
            return false;
        } else {
            this.size = x.size;
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (x.tileAt(i, j) != y.tileAt(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    // check if a current tile can move up
    @Contract(pure = true)
    private boolean canMoveUp(int i) {
        return i - 1 >= 0;
    }

    // check if a current tile can move down
    @Contract(pure = true)
    private boolean canMoveDown(int i) {
        return i + 1 <= this.size - 1;
    }

    // check if a current tile can move right
    @Contract(pure = true)
    private boolean canMoveRight(int j) {
        return j + 1 <= this.size - 1;
    }

    // check if a current tile can move left
    @Contract(pure = true)
    private boolean canMoveLeft(int j) {
        return j - 1 >= 0;
    }

    // swap tiles when moving up
    private void swapTilesUp(@NotNull Board a) {
        this.size = a.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == 0) {
                    int current = a.tiles[i][j];
                    a.tiles[i][j] = a.tiles[i - 1][j];
                    a.tiles[i - 1][j] = current;
                    return;
                }
            }
        }
    }

    // swap tiles when moving down
    private void swapTilesDown(@NotNull Board a) {
        this.size = a.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == 0) {
                    int current = a.tiles[i][j];
                    a.tiles[i][j] = a.tiles[i + 1][j];
                    a.tiles[i + 1][j] = current;
                    return;
                }
            }
        }
    }

    // swap tiles when moving right
    private void swapTilesRight(@NotNull Board a) {
        this.size = a.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == 0) {
                    int current = a.tiles[i][j];
                    a.tiles[i][j] = a.tiles[i][j + 1];
                    a.tiles[i][j + 1] = current;
                    return;
                }
            }
        }
    }

    // swap tiles when moving left
    private void swapTilesLeft(@NotNull Board a) {
        this.size = a.size;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == 0) {
                    int current = a.tiles[i][j];
                    a.tiles[i][j] = a.tiles[i][j - 1];
                    a.tiles[i][j - 1] = current;
                    return;
                }
            }
        }
    }

    // all neighboring boards
    ArrayList<Board> neighbors(@NotNull Board a) {
        this.size = a.size;
        ArrayList<Board> neighbors = new ArrayList<>();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] == 0) {
                    if (a.canMoveUp(i)) {
                        Board upBoard = new Board(a.tiles);
                        upBoard.swapTilesUp(upBoard);
                        neighbors.add(upBoard);
                    }
                    if (a.canMoveDown(i)) {
                        Board downBoard = new Board(a.tiles);
                        downBoard.swapTilesDown(downBoard);
                        neighbors.add(downBoard);
                    }
                    if (a.canMoveRight(j)) {
                        Board rightBoard = new Board(a.tiles);
                        rightBoard.swapTilesRight(rightBoard);
                        neighbors.add(rightBoard);
                    }
                    if (a.canMoveLeft(j)) {
                        Board leftBoard = new Board(a.tiles);
                        leftBoard.swapTilesLeft(leftBoard);
                        neighbors.add(leftBoard);
                    }
                }
            }
        }
        return neighbors;
    }

    // returns the number of inversions of the current board
    int inversions(@NotNull Board a) {
        this.size = a.size;
        int cntInversions = 0;
        int[] tempArray = new int[this.size*this.size - 1];
        int tempIndex = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (a.tiles[i][j] != 0) {
                    tempArray[tempIndex] = a.tiles[i][j];
                    tempIndex++;
                    if (tempIndex > this.size*this.size - 1) {
                        return 0;
                    }
                }
            }
        }
        for (int i = 0; i < this.size * this.size - 2; i++) {
            for (int j = i + 1; j < this.size * this.size - 1; j++) {
                if (tempArray[i] > tempArray[j]) {
                    cntInversions++;
                }
            }
        }
        return cntInversions;
    }

    // is this board solvable?
    boolean isSolvable(@NotNull Board a) {
        this.size = a.size;
        if ((this.size * this.size) % 2 == 0) {
            int zero = 0;
            return (inversions(a) + posXY(a, zero, true)) % 2 != 0;
        }
        else {
            return inversions(a) % 2 == 0;
        }
    }

    // print the current board
    void printBoard() {
        System.out.println();
        System.out.println("The board: ");
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(this.tiles[i][j] + " ");
            }
            System.out.println();
        }
    }

    // the shortest path solution
    int solution(@NotNull Board initialBoard, Board finalBoard) {
        // check if the board is solvable
        if (!initialBoard.isSolvable(initialBoard)) {
            throw new IllegalArgumentException("The puzzle is not solvable!");
        } else {
            // current manhattan distance
            int limit = initialBoard.manhattan(initialBoard, finalBoard);
            // current visited boards
            List<Board> visited = new ArrayList<>();
            while(true) {
                int temp = depthLimitedSearch(initialBoard, finalBoard, 0, limit, visited);
                if (temp < 0) {
                    // if the return value is negative
                    limit = Math.abs(temp);
                    visited.clear();
                } else {
                    return temp;
                }
            }
        }
    }

    // return the shortest path in actions
    ArrayList<String> actionsPath(@NotNull List<Board> pathBoards) {
        ArrayList<String> actions = new ArrayList<>();
        int zero = 0;
        for (int i = 0; i < pathBoards.size() - 1; i++) {
            Board currentBoard = pathBoards.get(i);
            Board boardAction = pathBoards.get(i + 1);
            int posZeroRowCurrent = currentBoard.posXY(currentBoard, zero, true);
            int posZeroColCurrent = currentBoard.posXY(currentBoard, zero, false);
            int posZeroRowAction = boardAction.posXY(boardAction, zero, true);
            int posZeroColAction = boardAction.posXY(boardAction, zero, false);
            if (posZeroRowCurrent + 1 == posZeroRowAction) {
                actions.add("down");
            } else if (posZeroRowCurrent - 1 == posZeroRowAction) {
                actions.add("up");
            } else if (posZeroColCurrent + 1 == posZeroColAction) {
                actions.add("left");
            } else if (posZeroColCurrent - 1 == posZeroColAction) {
                actions.add("right");
            }
        }
        return actions;
    }

    // depth limited search for the current board
    private int depthLimitedSearch(@NotNull Board currentBoard, Board finalBoard, int moves, int limit, @NotNull List<Board> visited) {
        //current costPath
        int costPath = moves + currentBoard.manhattan(currentBoard, finalBoard);
        // check if the current board is already visited
        if(visited.contains(currentBoard)) {
            return Integer.MAX_VALUE;
        }
        // check if the current board is equal to the final board
        if (currentBoard.equals(finalBoard)) {
            visited.add(currentBoard);
            System.out.println("The actions to reach to the final board: ");
            for (String action : currentBoard.actionsPath(visited)) {
                System.out.println(action);
            }
            System.out.println();
            return moves;
        } else {
            // return negative value to distinguish it from the moves
            if (costPath > limit) {
                return -costPath;
            }
            // add the current board
            visited.add(currentBoard);
            // the children of the current board
            ArrayList<Board> expanded = currentBoard.neighbors(currentBoard);
            List<Integer> children = new ArrayList<>();
            for (Board b : expanded) {
                // check if the child of the current board is visited
                if(!visited.contains(b)) {
                    // calculate the current function, moves + 1
                    int currentLimit = depthLimitedSearch(b, finalBoard, moves + 1, limit, visited);
                    // add the result of each child
                    children.add(currentLimit);
                }
            }
            // remove the current board
            visited.remove(currentBoard);
            int newThreshold = Integer.MIN_VALUE;
            for (int currentChild : children) {
                // if the current threshold is positive value, return it
                if (currentChild > 0) {
                    return currentChild;
                } else {
                    // if the current threshold is negative, return min negative threshold (closest to 0)
                    newThreshold = Math.max(currentChild, newThreshold);
                }
            }
            return newThreshold;
        }
    }
}
