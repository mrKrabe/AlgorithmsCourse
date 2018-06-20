import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Burrows–Wheeler Data Compression
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suff = new CircularSuffixArray(s);
        int first = -1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ind = suff.index(i);

            // start index
            if (ind == 0) {
                first = i;
            }

            b.append(s.charAt(getIndex(s, ind)));
        }

        BinaryStdOut.write(first);
        BinaryStdOut.write(b.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int ind = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int[] next = createNextArr(s);

        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            b.append(s.charAt(next[ind]));
            ind = next[ind];
        }

        BinaryStdOut.write(b.toString());
        BinaryStdOut.close();
    }

    private static int[] createNextArr(String s) {
        // reconstruct array with char positions
        int[] count = charCounts(s);
        int length = s.length();
        int[] next = new int[length];

        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            int ind = count[c];
            next[ind] = i;
            count[c]++;
        }

        return next;
    }

    private static int getIndex(String s, int ind) {
        return (ind + s.length() - 1) % s.length();
    }

    private static int[] charCounts(String s) {
        int length = s.length();
        char[] a = s.toCharArray();
        int R = 256;
        int[] count = new int[R + 1];

        for (int i = 0; i < length; i++) {
            count[a[i] + 1]++;
        }

        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        return count;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else if ("+".equals(args[0])) {
            inverseTransform();
        }
    }
}
