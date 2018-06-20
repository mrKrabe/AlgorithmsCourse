import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// which noun is the least related to the others?
public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = 0;
        int maxInd = 0;

        for (int i = 0; i < nouns.length; i++) {
            String base = nouns[i];
            int dist = 0;

            for (String noun : nouns) {
                if (!base.equals(noun)) {
                    int value = wordnet.distance(base, noun);

                    if (value < 0) {
                        return nouns[i];
                    } else {
                        dist += value;
                    }
                }
            }

            if (dist > maxDist) {
                maxDist = dist;
                maxInd = i;
            }
        }

        return nouns[maxInd];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        In in = new In("wordnet/outcast11.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println("outcast5.txt: " + outcast.outcast(nouns));
    }
}