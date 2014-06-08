/**
 * 
 */
package pl.elka.game2048j.common;

/**
 * Matrix with modifiable gravity.
 */
public class GravMatrix extends Matrix {
    private static final long serialVersionUID = -2293950536676826768L;

    /**
     * @param size
     */
    public GravMatrix(Integer size) {
        super(size);
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Move all the matrix elements in the selected direction.
     */
    public void setGravity(Direction dir) {
        int start = 0;
        int delimiter = 0;
        int step = 0;
        boolean vertical = false;

        switch (dir) {
        case DOWN:
            vertical = true;
        case RIGHT:
            start = getSize()-1;
            delimiter = -1;
            step = -1;
            break;

        case UP:
            vertical = true;
        case LEFT:
            start = 0;
            delimiter = getSize();
            step = 1;
            break;
        }

        for (int dim1 = 0; dim1 < getSize(); ++dim1) {
            int empty = start;
            int occupied = empty;
            while (empty != delimiter
                    && occupied != delimiter) {
                // find the first empty tile
                while (empty != delimiter 
                        && get(empty, dim1, vertical) != null) {
                    empty += step;
                }

                // find the first occupied tile
                occupied = empty;
                while (occupied != delimiter 
                        && get(occupied, dim1, vertical) == null) {
                    occupied += step;
                }

                // swap them
                if (occupied != delimiter) {
                    set(empty   , dim1, get(occupied, dim1, vertical), vertical);
                    set(occupied, dim1, null                         , vertical);
                }
            }
        }
    }

}
