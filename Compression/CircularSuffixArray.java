import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private static final int R = 256;
    private static final int CUTOFF = 15;
    private final int[] indArr;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("null argument");
        }

        int length = s.length();
        indArr = new int[length];
        Arrays.setAll(indArr, i -> i);

        int[] aux = new int[length];

        sort(indArr, 0, indArr.length - 1, 0, s, aux);
    }

    // length of s
    public int length() {
        return indArr.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("index out of bounds " + i);
        }

        return indArr[i];
    }

    private static void sort(int[] a, int lo, int hi, int d, String s, int[] aux) {
        // Sort from a[lo] to a[hi], starting at the dth character.

        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d, s);
            return;
        }

        // Compute frequency counts.
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            count[charAt(s, a[i], d) + 2]++;
        }

        // Transform counts to indices.
        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        // Distribute.
        for (int i = lo; i <= hi; i++) {
            aux[count[charAt(s, a[i], d) + 1]++] = a[i];
        }

        // Copy back.
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }

        // Recursively sort for each character value.
        for (int r = 0; r < R; r++) {
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, s, aux);
        }
    }

    private static void insertion(int[] a, int lo, int hi, int d, String s) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d, s); j--) {
                int temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
            }
        }
    }

    // is v less than w, starting at character d
    private static boolean less(int v, int w, int d, String s) {
        for (int i = d; i < s.length(); i++) {
            int vc = charAt(s, v, i);
            int wc = charAt(s, w, i);

            if (vc < wc) {
                return true;
            }

            if (vc > wc) {
                return false;
            }
        }

        return false;
    }

    // return dth character of s starting from pos, -1 - if greater or equal than length
    private static int charAt(String s, int pos, int d) {
        if (d >= s.length()) {
            return -1;
        }

        return s.charAt((pos + d) % s.length());
    }

    // unit testing
    public static void main(String[] args) {
        CircularSuffixArray suff = new CircularSuffixArray(new In("burrows/aesop.txt").readAll());

        StdOut.println("Length: " + suff.length());
        StdOut.println("Ind 11: " + suff.index(11));
    }
}
