/**
 * 
 */
package pl.elka.game2048j.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * NxN matrix
 */
public class Matrix implements Cloneable, Serializable {
    private static final long serialVersionUID = -5810984163531040145L;

    private Integer[][] board;

    /**
     * Creates NxN matrix, where N = size
     * @param size
     */
    public Matrix(Integer size) {
        board = new Integer[size][size];
    }

    /**
     * A simple element getter.
     * 
     * @param x
     * @param y
     * @return
     */
    public Integer get(int x, int y) {
        return board[y][x];
    }

    /**
     * Element getter, possibly with matrix transposition.
     * 
     * @param x
     * @param y
     * @param transpose
     * @return
     */
    protected Integer get(int x, int y, boolean transpose) {
        if (transpose) {
            return board[x][y];
        } else {
            return board[y][x];
        }
    }

    /**
     * A simple element setter
     * 
     * @param x
     * @param y
     * @param value
     */
    public void set(int x, int y, Integer value) {
        board[y][x] = value;
    }

    /**
     * Element setter, possibly with matrix transposition.
     * 
     * @param x
     * @param y
     * @param value
     * @param transpose
     */
    protected void set(int x, int y, Integer value, boolean transpose) {
        if (transpose) {
            board[x][y] = value;
        } else {
            board[y][x] = value;
        }
    }

    public Integer getSize() {
        return board.length;
    }

    public int count() {
        int count = 0;
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                if (get(x,y) != null) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Returns a deep clone of the matrix.
     */
    public Object clone() throws CloneNotSupportedException {
        Matrix clone = (Matrix)super.clone();

        clone.board = (Integer[][])board.clone();
        for (int i = 0; i < clone.board.length; i++) {
            clone.board[i] = Arrays.copyOf(board[i], clone.board.length);
        }

        return clone;

    }

    /**
     * Compares two matrices deeply.
     */
    @Override
    public boolean equals(Object rhs) {
        Matrix rhs_matrix = (Matrix)rhs;
        return Arrays.deepEquals(board, rhs_matrix.board);
    }

}
