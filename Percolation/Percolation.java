import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Program estimates the value of the percolation threshold via Monte Carlo simulation for a n*n grid
 */
public class Percolation {
    private final int size;
    private final WeightedQuickUnionUF union;
    private final WeightedQuickUnionUF union2;
    private final boolean[] field;
    private final int top;
    private final int bottom;

    private int opened = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size must be positive: " + n);
        }

        int elements = n * n;

        union = new WeightedQuickUnionUF(elements + 2);
        union2 = new WeightedQuickUnionUF(elements + 1);
        size = n;
        field = new boolean[elements];

        top = elements;
        bottom = elements + 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validateRowCol(row, col);

        if (isOpen(row, col)) {
            return;
        }

        int index = xyTo1D(row, col);
        field[index] = true;

        connectCell(row, col, row, col - 1);
        connectCell(row, col, row, col + 1);
        connectCell(row, col, row - 1, col);
        connectCell(row, col, row + 1, col);

        opened++;

        if (row == 1) {
            union.union(index, top);
            union2.union(index, top);
        }

        if (row == size) {
            union.union(index, bottom);
        }

        union.connected(top, xyTo1D(row, col));

    }

    // is site (row, col) open
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);

        return field[xyTo1D(row, col)];
    }

    // is site (row, col) full
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);

        return union2.connected(top, xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate
    public boolean percolates() {
        return union.connected(top, bottom);
    }

    public static void main(String[] args) {
        //no code
    }

    private boolean indexOutOfRange(int index) {
        return index < 1 || index > size;
    }

    private void validateRowCol(int row, int col) {
        if (indexOutOfRange(row) || indexOutOfRange(col)) {
            throw new IllegalArgumentException("Index out of bounds: " + row + " " + col);
        }
    }

    private int xyTo1D(int row, int col) {
        return (col - 1) + (row - 1) * size;
    }

    private void connectCell(int row1, int col1, int row2, int col2) {
        if (indexOutOfRange(row2) || indexOutOfRange(col2) || !isOpen(row2, col2)) {
            return;
        }

        union.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
        union2.union(xyTo1D(row1, col1), xyTo1D(row2, col2));
    }
}
