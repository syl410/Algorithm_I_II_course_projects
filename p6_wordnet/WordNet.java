import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    private final Digraph netG; // word net graph
    private int count;
    private final HashMap<Integer, String> idMap; // each id has a String
    private final HashMap<String, HashSet<Integer>> nounMap; // each noun has id set
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        idMap = new HashMap<>();
        nounMap = new HashMap<>();
        this.count = 0;
        synsetsIn(synsets);
        netG = new Digraph(count);
        hypernymsIn(hypernyms);

        DirectedCycle directedC = new DirectedCycle(netG);
        if (directedC.hasCycle()) throw new IllegalArgumentException();

        isSingleRoot();

        sap = new SAP(netG);
    }

    // check if the graph has single root
    private void isSingleRoot() {
        int rootNum = 0;
        for (int i = 0; i < count; i++) {
            if (netG.outdegree(i) == 0) rootNum++;
        }
        if (rootNum != 1) throw new IllegalArgumentException();
    }

    // read synset file
    private void synsetsIn(String synsets) {
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String eachLine =  in.readLine();
            String[] wordArr = eachLine.split(",");
            int id = Integer.parseInt(wordArr[0]);
            String[] synArr = wordArr[1].split(" ");
            idMap.put(id, wordArr[1]);
            for (String syn : synArr) {
                if (nounMap.containsKey(syn)) {
                    HashSet<Integer> intSet = nounMap.get(syn);
                    intSet.add(id);
                } else {
                    HashSet<Integer> intSet = new HashSet<Integer>();
                    intSet.add(id);
                    nounMap.put(syn, intSet);
                }
            }
            count++;
        }
    }

    // read hypernyms
    private void hypernymsIn(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String eachLine =  in.readLine();
            String[] wordArr = eachLine.split(",");
            int id = Integer.parseInt(wordArr[0]);
            for (int i = 1; i < wordArr.length; i++) {
                int index = Integer.parseInt(wordArr[i]);
                netG.addEdge(id, index);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounMap.containsKey(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        HashSet<Integer> idSetA = nounMap.get(nounA);
        HashSet<Integer> idSetB = nounMap.get(nounB);
        return sap.length(idSetA, idSetB);
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        HashSet<Integer> idSetA = nounMap.get(nounA);
        HashSet<Integer> idSetB = nounMap.get(nounB);
        return idMap.get(sap.ancestor(idSetA, idSetB));
    }
    
}
