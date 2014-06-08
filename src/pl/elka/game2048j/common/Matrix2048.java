/**
 * 
 */
package pl.elka.game2048j.common;

/**
 * GravMatrix that merges identical elements.
 */
public final class Matrix2048 extends MergingGravMatrix {
    private static final long serialVersionUID = -572102740165389119L;

    /**
     * @param size
     */
    public Matrix2048(Integer size) {
        super(size);
    }

    @Override
    protected boolean predicate(Integer a, Integer b) {
        return (a != null && a.equals(b));
    }

    @Override
    protected Integer merge(Integer a, Integer b) {
        return a+1;
    }

}
