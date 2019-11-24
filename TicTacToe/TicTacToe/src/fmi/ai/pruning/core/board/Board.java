package fmi.ai.pruning.core.board;

import fmi.ai.pruning.core.enums.Marker;
import fmi.ai.pruning.core.enums.State;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private static final int BOARD_SIZE = 3;
    private Marker[][] board;

    public Marker[][] getBoard() {
        return this.board;
    }

    // fill the board with symbol '_', it is empty
    public Board() {
        this.board = new Marker[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            this.board[i] = new Marker[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; ++j) {
                this.board[i][j] = Marker.EMPTY;
            }
        }
    }

    // copy the current board
    public Board(Board board) {
        this.board = new Marker[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            this.board[i] = new Marker[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; ++j) {
                this.board[i][j] = board.board[i][j];
            }
        }
    }

    // set value to given position
    public void setMarker(Marker marker, @NotNull Position position) {
        this.board[position.getRow()][position.getCol()] = marker;
    }

    // print the current board
    public void print() {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // get all free spaces from the current board
    public List<Position> getFreeSpaces() {
        List<Position> freeSpaces = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (this.board[i][j] == Marker.EMPTY) {
                    freeSpaces.add(new Position(i, j));
                }
            }
        }
        return freeSpaces;
    }

    // get the current state of the board
    public State getState() {
        // Check for a win in the rows for each col
        for (int i = 0; i < BOARD_SIZE; ++i) {
            boolean isWinningState = true;
            Marker winningMarker = this.board[i][0];
            for (int j = 0; j < BOARD_SIZE - 1; ++j) {
                if (this.board[i][j] == Marker.EMPTY || this.board[i][j] != this.board[i][j + 1]) {
                    isWinningState = false;
                    break;
                }
            }

            if (isWinningState) {
                // check if X wins
                if (winningMarker == Marker.X) {
                    return State.X_WIN;
                }
                // check if O wins
                else if (winningMarker == Marker.O) {
                    return State.O_WIN;
                }
            }
        }

        // Check for a win in the cols for each row
        for (int j = 0; j < BOARD_SIZE; ++j) {
            boolean isWinningState = true;
            Marker winningMarker = this.board[0][j];
            for (int i = 0; i < BOARD_SIZE - 1; ++i) {
                if(this.board[i][j] == Marker.EMPTY || this.board[i][j] != this.board[i + 1][j]) {
                    isWinningState = false;
                    break;
                }
            }

            if (isWinningState) {
                // check if X wins
                if (winningMarker == Marker.X) {
                    return State.X_WIN;
                }
                // check if O wins
                else if (winningMarker == Marker.O) {
                    return State.O_WIN;
                }
            }
        }

        // Check for a win in the main diagonal
        boolean isWinningStateMainDiag = true;
        Marker winningMarkerMainDiag = this.board[0][0];
        for (int i = 0; i < BOARD_SIZE - 1; ++i) {
            if (this.board[i][i] == Marker.EMPTY || this.board[i][i] != this.board[i + 1][i + 1]) {
                isWinningStateMainDiag = false;
                break;
            }
        }
        if (isWinningStateMainDiag) {
            // check if X wins
            if (winningMarkerMainDiag == Marker.X) {
                return State.X_WIN;
            }
            // check if O wins
            else if (winningMarkerMainDiag == Marker.O) {
                return State.O_WIN;
            }
        }

        // Check for a win in the secondary diagonal
        boolean isWinningStateSecondaryDiag = true;
        Marker winningMarkerSecondaryDiag = this.board[0][BOARD_SIZE - 1];
        for (int i = 0; i < BOARD_SIZE - 1; ++i) {
            if (this.board[i][i] == Marker.EMPTY || this.board[i][(BOARD_SIZE - 1) - i] != this.board[i + 1][(BOARD_SIZE - 1) - (i + 1)]) {
                isWinningStateSecondaryDiag = false;
                break;
            }
        }
        if (isWinningStateSecondaryDiag) {
            // check if X wins
            if (winningMarkerSecondaryDiag == Marker.X) {
                return State.X_WIN;
            }
            // check if O wins
            else if (winningMarkerSecondaryDiag == Marker.O) {
                return State.O_WIN;
            }
        }

        // Check for an even
        boolean isEven = true;
        for (int i = 0; i < BOARD_SIZE; ++i) {
            if (!isEven) {
                break;
            }

            for (int j = 0; j < BOARD_SIZE; ++j) {
                // check for empty spaces
                if (this.board[i][j] == Marker.EMPTY) {
                    isEven = false;
                    break;
                }
            }
        }
        if (isEven) {
            return State.EVEN;
        }

        return State.UNFINISHED;
    }

}
