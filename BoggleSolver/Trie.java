// modified Trie from algs4.jar
public class Trie {
    private static final int R = 26;        // we have 26 letters A..Z

    private Node root;      // root of trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key);
        if (x == null) return false;
        return x.isString;
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node get(Node x, String key) {
        Node n = x;
        int pos = 0;

        while (true) {
            if (n == null) return null;
            if (pos == key.length()) return n;

            int c = getChar(key, pos);
            n = n.next[c];

            pos++;
        }
    }

    private int getChar(String key, int d) {
        return key.charAt(d) - 65; // start indexing from 0
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isString = true;
        } else {
            int c = getChar(key, d);
            x.next[c] = add(x.next[c], key, d + 1);
        }
        return x;
    }

    public boolean hasPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("argument to hasPrefix() is null");
        Node x = get(root, prefix);
        return x != null;
    }
}
