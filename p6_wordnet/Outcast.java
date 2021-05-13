import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int sum = 0;
        String oc = null; // outcast
        for (int i = 0; i < nouns.length; i++) {
            int newSum = 0;
            for (int j = 0; j < nouns.length; j++) {
                newSum += wordnet.distance(nouns[i], nouns[j]);
            }
            if (newSum > sum) {
                sum = newSum;
                oc = nouns[i];
            } 
        }
        return oc;
    }
}
