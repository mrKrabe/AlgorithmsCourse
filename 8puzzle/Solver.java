import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Stack<Board> solution = new Stack<>();
    private boolean solvable = true;

    private class Node {
        final Board board;
        final int moves;
        final int priority;
        final Node parent;

        Node(Board board, int moves, int priority, Node parent) {
            this.board = board;
            this.moves = moves;
            this.priority = priority;
            this.parent = parent;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("null argument");
        }

        // twin is used to detect unsolvable board

        MinPQ<Node> queue = new MinPQ<>(Comparator.comparingInt(o -> o.moves + o.priority));
        queue.insert(new Node(initial, 0, initial.manhattan(), null));
        Board twin = initial.twin();
        MinPQ<Node> queueTwin = new MinPQ<>(Comparator.comparingInt(o -> o.moves + o.priority));
        queueTwin.insert(new Node(twin, 0, twin.manhattan(), null));

        while (!queue.isEmpty()) {
            Node item = queue.delMin();
            Node itemTwin = queueTwin.delMin();

            if (item.board.isGoal()) {
                createSolution(item);
                break;
            }

            if (itemTwin.board.isGoal()) {
                solvable = false;
                solution = null;
                break;
            }

            addAdjToQueue(queue, item);
            addAdjToQueue(queueTwin, itemTwin);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution == null) {
            return -1;
        }

        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void addAdjToQueue(MinPQ<Node> queue, Node item) {
        for (Board b : item.board.neighbors()) {
            if (item.parent == null || !b.equals(item.parent.board)) {
                queue.insert(new Node(b, item.moves + 1, b.manhattan(), item));
            }
        }
    }

    private void createSolution(Node item) {
        while (item != null) {
            solution.push(item.board);
            item = item.parent;
        }
    }

    public static void main(String[] args) {
        In in = new In("8puzzle/puzzle14.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
