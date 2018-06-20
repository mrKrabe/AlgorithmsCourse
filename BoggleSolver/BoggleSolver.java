import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// Solver for boggle game
public class BoggleSolver {
    private final Trie dict = new Trie();

    // Initializes the data structure using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("null dictionary");
        }

        for (String value : dictionary) {
            dict.add(value);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("null board");
        }

        Set<String> result = new HashSet<>();

        int cols = board.cols();
        int rows = board.rows();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.addAll(findWordsFromPoint(i, j, board));
            }
        }

        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    public int scoreOf(String word) {
        int len = word.length();

        if (len <= 2 || !dict.contains(word)) {
            return 0;
        }

        if (len <= 4) {
            return 1;
        }

        if (len == 5) {
            return 2;
        }

        if (len == 6) {
            return 3;
        }

        if (len == 7) {
            return 5;
        }

        return 11;
    }

    private List<String> findWordsFromPoint(int i, int j, BoggleBoard board) {
        List<String> result = new LinkedList<>();
        String c = String.valueOf(board.getLetter(i, j));
        String s = c.equals("Q") ? "QU" : c;
        int cols = board.cols();
        int rows = board.rows();

        boolean[][] visited = new boolean[rows][cols];

        search(i, j, s, visited, board, result);

        return result;
    }

    private void search(int x, int y, String pref, boolean[][] visited, BoggleBoard board, List<String> result) {
        int minR = x > 0 ? x - 1 : 0;
        int minC = y > 0 ? y - 1 : 0;
        int rows = board.rows();
        int maxR = x < rows - 1 ? x + 1 : x;
        int maxC = y < board.cols() - 1 ? y + 1 : y;

        visited[x][y] = true;

        for (int i = minR; i <= maxR; i++) {
            for (int j = minC; j <= maxC; j++) {
                if (!visited[i][j]) {
                    String c = String.valueOf(board.getLetter(i, j));
                    String s = c.equals("Q") ? pref + "QU" : pref + c;

                    if (s.length() > 2 && dict.contains(s)) {
                        result.add(s);
                    }

                    if (dict.hasPrefix(s)) {
                        // mark as visited only path to this node
                        search(i, j, s, visited, board, result);
                    }
                }
            }
        }

        visited[x][y] = false;
    }

    public static void main(String[] args) {
        In in = new In("boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("boggle/board4x4.txt");
        int score = 0;

        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }

        StdOut.println("Score = " + score);
    }
}
