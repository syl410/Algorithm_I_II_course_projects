import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
	private DictTrie dictTrie;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
		dictTrie = new DictTrie();
		for (String eachWord : dictionary) {
			dictTrie.addWord(eachWord);
		}
	}

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		HashSet<String> wordSet = new HashSet<>();
		int rows = board.rows();
		int cols = board.cols();
		// traverse all positions in the board
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				boolean[][] marked = new boolean[rows][cols];
				wordSearch(board, row, col, "", marked, wordSet);
			}
		}
		return wordSet;
	}

	// search all legal words and add them to wordSet
	private void wordSearch(BoggleBoard board, int row, int col, String prefix, boolean[][] marked, HashSet<String> wordSet) {
		// System.out.println(prefix);
		marked[row][col] = true;

		char letter = board.getLetter(row, col);
		prefix += letter;
		// check it is 'QU'
		if (letter == 'Q') prefix += 'U';

		// find legal word, put it into wordSet
		if (prefix.length() >= 3 && dictTrie.contains(prefix)) {
			wordSet.add(prefix);
		}

		if (!dictTrie.isPrefix(prefix)) {
			marked[row][col] = false;
			return;
		}

		ArrayList<int[]> adjList = getAdjList(board, row, col, marked);
		for (int[] pos : adjList) {
			wordSearch(board, pos[0], pos[1], prefix, marked, wordSet);
		}
		marked[row][col] = false;
	}

	// get adjacent positions which are not marked
	private ArrayList<int[]> getAdjList(BoggleBoard board, int row, int col, boolean[][] marked) {
		int rows = board.rows();
		int cols = board.cols();
		ArrayList<int[]> adjList = new ArrayList<>();

		int minRow = row;
		int maxRow = row;
		int minCol = col;
		int maxCol = col;
		if (row - 1 >= 0) minRow = row - 1;
		if (row + 1 < rows) maxRow = row + 1;
		if (col - 1 >= 0) minCol = col - 1;
		if (col + 1 < cols) maxCol = col + 1;

		// System.out.println("minRow: " + minRow + " maxRow: " + maxRow);
		// System.out.println("minCol: " + minCol + " maxCol: " + maxCol);

		for (int i = minRow; i <= maxRow; i++) {
			for (int j = minCol; j <= maxCol; j++) {
				checkAndAdd(adjList, marked, i, j);
			}
		}
		
		// print
		/*
		for (int[] eachPos : adjList) {
			System.out.print(eachPos[0] + "," + eachPos[1] + " ");
		}
		System.out.println();
		*/
		
		return adjList;
	}

	// getAdjList helper
	private void checkAndAdd(ArrayList<int[]> adjList, boolean[][] marked, int newRow, int newCol) {
		if (!marked[newRow][newCol]) {
			int[] pos = {newRow, newCol};
			adjList.add(pos);
		}
	}
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
		if (!dictTrie.contains(word)) return 0;
		int len = word.length();
		if (len < 3) return 0;
		else if (len < 5) return 1;
		else if (len == 5) return 2;
		else if (len == 6) return 3;
		else if (len == 7) return 5;
		else return 11;
	}


	public static void main(String[] args) {
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
	}
}
