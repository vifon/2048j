/**
 * 
 */
package pl.elka.game2048j.server;

import java.util.Random;

import pl.elka.game2048j.common.Matrix;
import pl.elka.game2048j.common.Matrix2048;

/**
 * 2048j model implementing the game logic
 */
public class Model {
    private Matrix2048 board;
    private Random rand;

    /**
     * Creates a size x size model.
     * @param size
     */
    public Model(Integer size) {
        board = new Matrix2048(size);
        rand = new Random();
    }

    /**
     * Returns the model size.
     * @return
     */
    public Integer getSize() {
        return board.getSize();
    }

    /**
     * Performs the move in a given direction.
     * @param dir
     */
    public void setGravity(Matrix2048.Direction dir) {
        board.setGravity(dir);
    }

    /**
     * Adds a quasi-random tile in a random place.
     * @throws NoFreeSpaceException
     */
    public void addTile() throws NoFreeSpaceException {
        addTile(genTile());
    }

    /**
     * Check whether any move is possible.
     * @return
     */
    public boolean isMovePossible() {
        if (board.count() < getSize()*getSize()) {
            return true;
        } else {
            return board.isMergePossible();
        }
    }

    /**
     * Adds a given tile in a random place.
     * @param value
     * @throws NoFreeSpaceException
     */
    public void addTile(Integer value) throws NoFreeSpaceException {
        if (board.count() == getSize()*getSize()) {
            throw new NoFreeSpaceException();
        }


        int x,y;
        do {
            x = rand.nextInt(getSize());
            y = rand.nextInt(getSize());
        } while (board.get(x,y) != null);

        board.set(x,y, value);
    }

    /**
     * 90% chance for 2.
     * 10% chance for 4,
     * @return
     */
    private Integer genTile() {
        if (rand.nextInt(10) > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Get a copy of the game board
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public Matrix getSnapshot() {
        Matrix clone = null;
        try {
            clone = (Matrix)board.clone();
        } catch (CloneNotSupportedException e) {
            // will never happen
            System.exit(1);
        }
        return clone;
    }

}
