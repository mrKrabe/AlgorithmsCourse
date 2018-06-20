import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WordNet {
    private final Map<String, List<Integer>> synSet = new HashMap<>();
    private final Map<Integer, List<String>> invSynSet = new HashMap<>();
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        parseSynSets(synsets);
        parseHyperNyms(hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synSet.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("null argument");
        }

        return synSet.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        List<Integer> v = synSet.get(nounA);
        List<Integer> w = synSet.get(nounB);

        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        List<Integer> v = synSet.get(nounA);
        List<Integer> w = synSet.get(nounB);

        int parent = sap.ancestor(v, w);
        return String.join(" ", invSynSet.get(parent));
    }

    private void parseHyperNyms(String hypernyms) {
        In in = new In(hypernyms);
        String[] items = in.readAllStrings();

        Digraph digraph = new Digraph(items.length + 1);

        for (String s : items) {
            String[] ids = s.split(",");
            int root = Integer.parseInt(ids[0]);

            for (int i = 1; i < ids.length; i++) {
                digraph.addEdge(root, Integer.parseInt(ids[i]));
            }
        }

        DirectedCycle cycle = new DirectedCycle(digraph);

        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("DAG with cycle");
        }

        sap = new SAP(digraph);
    }

    private void parseSynSets(String synsets) {
        In in = new In(synsets);

        while (!in.isEmpty()) {
            String[] item = in.readLine().split(",");
            String[] nouns = item[1].split(" ");
            int id = Integer.parseInt(item[0]);

            for (String n : nouns) {
                if (synSet.containsKey(n)) {
                    List<Integer> ids = synSet.get(n);
                    ids.add(id);
                } else {
                    synSet.put(n, new ArrayList<>(Arrays.asList(id)));
                }
            }

            invSynSet.put(id, new ArrayList<>(Arrays.asList(nouns)));
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("wordnet/synsets100-subgraph.txt", "wordnet/hypernyms100-subgraph.txt");
        StdOut.println(net.distance("albumen", "ricin_toxin"));
        StdOut.println(net.sap("albumen", "ricin_toxin"));
    }
}