package pl.elka.game2048j.common;

import pl.elka.game2048j.common.GravMatrix.Direction;

/**
 * UI event of 2048j
 */
public enum Event {
    LEFT, RIGHT, UP, DOWN,
    CLOSE, RESTART;

    /**
     * Cast event to a corresponding GravMatrix.Direction (or null)
     * @return appropriate direction
     */
    public GravMatrix.Direction toDirection() {
        switch (this) {
        case UP:
            return Direction.UP;
        case DOWN:
            return Direction.DOWN;
        case LEFT:
            return Direction.LEFT;
        case RIGHT:
            return Direction.RIGHT;
        default:
            return null;
        }
    }
}
