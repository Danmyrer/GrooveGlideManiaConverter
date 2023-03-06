package dev.henrihenr.game2d.maniaconverter.game;

public record GameHitObject
(
    double beat,
    int row,
    Type type
) {
    public static enum Type {
        DEFAULT, MOVE
    }
}