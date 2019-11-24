package fmi.ai.pruning.core.enums;

import org.jetbrains.annotations.Contract;

public enum Marker {

    EMPTY("_"),
    X("X"),
    O("O");

    private String markerString;

    @Contract(pure = true)
    Marker(String markerString) {
        this.markerString = markerString;
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return this.markerString;
    }
}
