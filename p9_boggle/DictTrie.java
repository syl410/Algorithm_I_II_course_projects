public class DictTrie {
	private static final int RADIX = 26;
    private static final char START_LETTER = 'A';	
    private Node root;

	public DictTrie() {
		root = new Node();
	}

	// inner class
    private static class Node {
        private boolean isEndOfWord = false;
        private Node[] next = new Node[RADIX];
    }

	// check if the trie contains str
    public boolean contains(String str) {
        Node n = findNode(root, str, 0);

        if (n == null) return false;

        return n.isEndOfWord;
    }

    public boolean isPrefix(String str) {
        Node n = findNode(root, str, 0);

        return n != null;
    }

    public void addWord(String str) {
        root = addWord(str, root, 0);
    }

	private Node addWord(String str, Node n, int d) {
        if (n == null) n = new Node();

        if (d == str.length()) {
            n.isEndOfWord = true;
            return n;
        }

        int c = str.charAt(d) - START_LETTER;
        n.next[c] = addWord(str, n.next[c], d + 1);

        return n;
    }

    private Node findNode(Node n, String str, int d) {
        if (n == null) return null;

        if (d == str.length()) {
            return n;
        }

        int c = str.charAt(d) - START_LETTER;
        return findNode(n.next[c], str, d + 1);
    }

}
