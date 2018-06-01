import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  8puzzle board
 */
public class Board {
    private final int[][] tiles;
    private final int size;
    private int manhattan = -1;
    private int hamming = -1;

    // construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.size = blocks.length;
        this.tiles = new int[size][size];

        copyArray(blocks, tiles);
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming == -1) {
            hamming = calcHamming();
        }

        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan == -1) {
            manhattan = calcManhattan();
        }

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] field = new int[size][size];

        copyArray(tiles, field);

        int sx = -1;
        int sy = -1;

        // exchange one pair (non-zero)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int item = field[i][j];

                if (item != 0) {
                    if (sx < 0) {
                        sx = i;
                        sy = j;
                    } else {
                        field[i][j] = field[sx][sy];
                        field[sx][sy] = item;

                        return new Board(field);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return size == board.size &&
                Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> result = new ArrayList<>();

        int[] zero = findZero(tiles);
        int x = zero[0];
        int y = zero[1];

        if (x > 0) {
            result.add(adjBoard(tiles, x, y, x - 1, y));
        }

        if (x < size - 1) {
            result.add(adjBoard(tiles, x, y, x + 1, y));
        }

        if (y > 0) {
            result.add(adjBoard(tiles, x, y, x, y - 1));
        }

        if (y < size - 1) {
            result.add(adjBoard(tiles, x, y, x, y + 1));
        }

        return result;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size);
        s.append("\n");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }

        return s.toString();
    }

    private Board adjBoard(int[][] field, int sx, int sy, int x, int y) {
        field[sx][sy] = field[x][y];
        field[x][y] = 0;

        Board result = new Board(field);

        field[x][y] = field[sx][sy];
        field[sx][sy] = 0;

        return result;
    }

    private void copyArray(int[][] from, int[][] to) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(from[i], 0, to[i], 0, size);
        }
    }

    private int to1d(int x, int y) {
        return x * size + y;
    }

    private int[] findZero(int[][] field) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[0];
    }

    private int calcHamming() {
        int result = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = tiles[i][j];

                if (tile > 0 && tile != to1d(i, j) + 1) {
                    result++;
                }
            }
        }

        return result;
    }

    private int calcManhattan() {
        int result = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = tiles[i][j] - 1;
                if (tile >= 0) {
                    int x = tile / size;
                    int y = tile % size;

                    result += Math.abs(i - x) + Math.abs(j - y);
                }
            }
        }

        return result;
    }
}
