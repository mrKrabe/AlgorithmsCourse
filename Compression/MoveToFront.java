import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = new char[R];
        fillAlphabet(alphabet);

        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();

            char ind = findKey(c, alphabet);
            moveToFront(ind, alphabet);

            BinaryStdOut.write(ind, 8);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[R];
        fillAlphabet(alphabet);


        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();

            moveToFront(c, alphabet);

            BinaryStdOut.write(alphabet[0], 8);
        }

        BinaryStdOut.close();
    }

    private static void moveToFront(char ind, char[] a) {
        char c = a[ind];

        System.arraycopy(a, 0, a, 1, ind);
        a[0] = c;
    }

    private static char findKey(char c, char[] a) {
        for (char i = 0; i < a.length; i++) {
            if (c == a[i]) {
                return i;
            }
        }

        throw new IllegalArgumentException("Invalid character");
    }

    private static void fillAlphabet(char[] alphabet) {
        //fill arr with indexes
        for (char i = 0; i < alphabet.length; i++) {
            alphabet[i] = i;
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            encode();
        } else if ("+".equals(args[0])) {
            decode();
        }
    }
}
