import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        if (g == null) {
            throwException();
        }

        graph = new Digraph(g);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getMinPath(new BreadthFirstDirectedPaths(graph, v), new BreadthFirstDirectedPaths(graph, w))[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return getMinPath(new BreadthFirstDirectedPaths(graph, v), new BreadthFirstDirectedPaths(graph, w))[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArguments(v, w);

        return getMinPath(new BreadthFirstDirectedPaths(graph, v), new BreadthFirstDirectedPaths(graph, w))[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArguments(v, w);

        return getMinPath(new BreadthFirstDirectedPaths(graph, v), new BreadthFirstDirectedPaths(graph, w))[1];
    }

    // returns 2 element array, 0 - path length, 1 - parent index
    private int[] getMinPath(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        List<Integer> parents = new ArrayList<>();
        int minPath = Integer.MAX_VALUE;
        int parent = -1;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                parents.add(i);
            }
        }

        for (int p : parents) {
            int path = bfsV.distTo(p) + bfsW.distTo(p);

            if (path < minPath) {
                minPath = path;
                parent = p;
            }
        }

        if (minPath == Integer.MAX_VALUE) {
            // -1 if no path
            minPath = -1;
        }

        return new int[]{minPath, parent};
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("wordnet/digraph1.txt");
        Digraph g = new Digraph(in);
        SAP sap = new SAP(g);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void checkArguments(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throwException();
        }
    }

    private void throwException() {
        throw new IllegalArgumentException("Null argument");
    }
}
