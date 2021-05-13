import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private class Node implements Comparable<Node> {
        private Board b;
        private Node prev;
        private int mvNum;

        public Node(Board b, Node prev) {
            this.b = b;
            this.prev = prev;
            if (prev == null) {
                this.mvNum = 0;
            } else {
                this.mvNum = prev.mvNum + 1;
            }
        }

        public int totalNum() {
            return this.b.manhattan() + this.mvNum;
        }
        
        public int compareTo(Node n) {
            return this.totalNum() - n.totalNum();
        }
    }

    private Node finalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<Node> nodePQ = new MinPQ<Node>();
        MinPQ<Node> twinPQ = new MinPQ<Node>();
        
        Node initNode = new Node(initial, null);
        Node initTwin = new Node(initial.twin(), null);

        nodePQ.insert(initNode);
        twinPQ.insert(initTwin);

        while (true) {
            finalNode = traverseMinNodeNeighors(nodePQ);
            if (finalNode != null) break;
            Node twinNode =  traverseMinNodeNeighors(twinPQ);
            if (twinNode != null) break;
        }
    }

    private Node traverseMinNodeNeighors(MinPQ<Node> nodePQ) {
        if (nodePQ.isEmpty()) return null;
        Node minNode = nodePQ.delMin();
        if (minNode.b.isGoal()) return minNode;
        for (Board neighborB : minNode.b.neighbors()) {
            Node newN = new Node(neighborB, minNode);
            if (minNode.prev == null) { // initial node
                nodePQ.insert(newN);
            } else {
                if (!neighborB.equals(minNode.prev.b)) {
                    nodePQ.insert(newN);
                }
            }
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (finalNode == null) return false;
        else return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) return finalNode.mvNum;
        else return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> nodeStack = new Stack<Board>();
        Node itrNode = finalNode;
        while (itrNode != null) {
            nodeStack.push(itrNode.b);
            itrNode = itrNode.prev;
        }
        return nodeStack;
    }

    // test client (see below) 
    public static void main(String[] args) {
    
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}
