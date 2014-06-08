/**
 * 
 */
package pl.elka.game2048j.common;

/**
 * GravMatrix that merges the fields moved into one another provided a given 
 * condition is true.
 */
public abstract class MergingGravMatrix extends GravMatrix {
    private static final long serialVersionUID = -356254497997543309L;

    /**
     * @param size
     */
    public MergingGravMatrix(Integer size) {
        super(size);
    }

    @Override
    public void setGravity(Direction dir) {
        super.setGravity(dir);

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

        for (int i = start; i != delimiter; i += step) {
            for (int j = start+step; j != delimiter; j += step) {
                Integer a = get(j  , i, vertical);
                Integer b = get(j-step, i, vertical);
                if (predicate(a,b)) {
                    set(j     , i, null      , vertical);
                    set(j-step, i, merge(a,b), vertical);
                }
            }
        }

        super.setGravity(dir);
    }

    /**
     * Check whether there are any two adjacent mergable elements.
     * @return
     */
    public boolean isMergePossible() {
        boolean vertical = false;
        for (int i0 = 0; i0 < 2; i0++) {
            for (int i = 0; i < getSize(); i++) {
                for (int j = 1; j < getSize(); j++) {
                    Integer a = get(j  , i, vertical);
                    Integer b = get(j-1, i, vertical);
                    if (predicate(a,b)) {
                        return true;
                    }
                }
            }
            vertical = true;
        }
        return false;
    }

    /**
     * Binary predicate for merging
     * 
     * @param a
     * @param b
     * @return
     */
    abstract protected boolean predicate(Integer a, Integer b);

    /**
     * Merge result
     * 
     * @param a
     * @param b
     * @return
     */
    abstract protected Integer merge(Integer a, Integer b);
}
