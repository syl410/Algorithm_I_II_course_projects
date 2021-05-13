import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SAP {
    private final Digraph netG;
    private final HashMap<HashSet<Integer>, ArrayList<Integer>> pairDistAncestorMap;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
    	if (G == null) throw new IllegalArgumentException();
        netG = new Digraph(G);
        pairDistAncestorMap = new HashMap<>();
    }

    private void checkRange(int v) {
        if (v < 0 || v >= netG.V()) throw new IllegalArgumentException();
    }

    private void checkRange(Iterable<Integer> vIter) {
        for (int v : vIter) checkRange(v);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkRange(v);
        checkRange(w);
        if (v == w) return 0;
        HashSet<Integer> pairKey = createPairKey(v, w);
        if (!pairDistAncestorMap.containsKey(pairKey)) {
            findDistAncestor(v, w);
        }
        return pairDistAncestorMap.get(pairKey).get(0);
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkRange(v);
        checkRange(w);
        if (v == w) return v;
        HashSet<Integer> pairKey = createPairKey(v, w);
        if (!pairDistAncestorMap.containsKey(pairKey)) {
            findDistAncestor(v, w);
        }
        return pairDistAncestorMap.get(pairKey).get(1);
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkRange(v);
        checkRange(w);
        ArrayList<Integer> distAncestor = findDistAncestor(v, w);
        return distAncestor.get(0);
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkRange(v);
        checkRange(w);
        ArrayList<Integer> distAncestor = findDistAncestor(v, w);
        return distAncestor.get(1);
    }

    private HashSet<Integer> createPairKey(int v, int w) {
        HashSet<Integer> pairKey = new HashSet<>();
        pairKey.add(v);
        pairKey.add(w);
        return pairKey;
    }

    // find distance and ancestor
    private void findDistAncestor(int v, int w) {
        checkRange(v);
        checkRange(w);
        HashSet<Integer> pairKey = createPairKey(v, w);

        if (pairDistAncestorMap.containsKey(pairKey)) return;

        BreadthFirstDirectedPaths vAncPath = new BreadthFirstDirectedPaths(netG, v);
        BreadthFirstDirectedPaths wAncPath = new BreadthFirstDirectedPaths(netG, w);
        int dist = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < netG.V(); i++) {
            if (vAncPath.hasPathTo(i) && wAncPath.hasPathTo(i)) {
                int newDist = vAncPath.distTo(i) + wAncPath.distTo(i);
                if (newDist < dist) {
                    dist = newDist;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) dist = -1;
        ArrayList<Integer> distAncestor = new ArrayList<>();
        distAncestor.add(dist);
        distAncestor.add(ancestor);
        pairDistAncestorMap.put(pairKey, distAncestor);
    }

    // find distance and ancestor
    private ArrayList<Integer> findDistAncestor(Iterable<Integer> v, Iterable<Integer> w) {

        BreadthFirstDirectedPaths vAncPath = new BreadthFirstDirectedPaths(netG, v);
        BreadthFirstDirectedPaths wAncPath = new BreadthFirstDirectedPaths(netG, w);
        int dist = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < netG.V(); i++) {
            if (vAncPath.hasPathTo(i) && wAncPath.hasPathTo(i)) {
                int newDist = vAncPath.distTo(i) + wAncPath.distTo(i);
                if (newDist < dist) {
                    dist = newDist;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) dist = -1;
        ArrayList<Integer> distAncestor = new ArrayList<>();
        distAncestor.add(dist);
        distAncestor.add(ancestor);
        return distAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
