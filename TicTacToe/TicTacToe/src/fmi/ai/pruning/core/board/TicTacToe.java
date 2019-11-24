package fmi.ai.pruning.core.board;

import fmi.ai.pruning.core.enums.Marker;
import fmi.ai.pruning.core.enums.State;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;

public class TicTacToe {

    // the move from AI with the last current board from human input
    public static Position getNextMoveFromAI(@NotNull Board board, Marker aiMarker) {
        // retrieve the score for each position
        TreeMap<Integer, Position> possibleMoves = new TreeMap<>();
        // get all free spaces and for each space calculate the score and get the best one
        board.getFreeSpaces().forEach(freeSpace -> {
            // mark the space
            board.setMarker(aiMarker, freeSpace);
            int score = minmax(board, getOppositeMarker(aiMarker), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            // unmark the space
            board.setMarker(Marker.EMPTY, freeSpace);
            // save the score
            possibleMoves.put(score, freeSpace);
        });

        // get the best score
        if (aiMarker == Marker.X) {
            return possibleMoves.lastEntry().getValue();
        } else if (aiMarker == Marker.O) {
            return possibleMoves.firstEntry().getValue();
        } else {
            throw new IllegalArgumentException("Invalid data!");
        }
    }

    // the solution
    private static int minmax(@NotNull Board board, Marker marker, int depth, int alpha, int beta) {
        // if the current state is a leaf node
        if (board.getState() != State.UNFINISHED) {
            State gameState = board.getState();
            if (gameState == State.X_WIN) {
                return (10 - depth);
            } else if (gameState == State.O_WIN) {
                return -(10 - depth);
            } else {
                return 0;
            }
        } else {
            if(marker == Marker.X) {
                // Maximizing
                int bestScore = Integer.MIN_VALUE;
                List<Position> freeSpaces = board.getFreeSpaces();
                for (Position freeSpace : freeSpaces) {
                    // mark the space
                    board.setMarker(Marker.X, freeSpace);
                    int score = minmax(board, getOppositeMarker(Marker.X), depth + 1, alpha, beta);
                    // unmark the space
                    board.setMarker(Marker.EMPTY, freeSpace);

                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, bestScore);

                    // pruning
                    if(beta <= alpha) {
                        break;
                    }
                }

                return bestScore;
            } else if(marker == Marker.O) {
                // Minimizing
                int bestScore = Integer.MAX_VALUE;

                List<Position> freeSpaces = board.getFreeSpaces();
                for (Position freeSpace : freeSpaces) {
                    // mark the space
                    board.setMarker(Marker.O, freeSpace);
                    int score = minmax(board, getOppositeMarker(Marker.O), depth + 1, alpha, beta);
                    // unmark the space
                    board.setMarker(Marker.EMPTY, freeSpace);

                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, bestScore);

                    // pruning
                    if(beta <= alpha) {
                        break;
                    }
                }

                return bestScore;
            } else {
                throw new RuntimeException();
            }
        }
    }

    // get the opposite marker for the current one
    public static Marker getOppositeMarker(Marker marker) {
        if (marker == Marker.X) {
            return Marker.O;
        } else if (marker == Marker.O) {
            return Marker.X;
        } else {
            throw new IllegalArgumentException("No such marker!");
        }
    }
}
