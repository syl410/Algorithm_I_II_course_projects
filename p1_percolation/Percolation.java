import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
	private final int N; // define size of grid
	private final int size; // size of grid
	private boolean[][] grid;
	private int numOfOpen; // number of open sites;
	private final WeightedQuickUnionUF WQUF;
	
	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		valid_size(n); // check if n > 0
		this.N = n;
		this.size = n * n;
		this.grid = new boolean[n][n];
		this.numOfOpen = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.grid[i][j] = false;
			}
		}

		WQUF = new WeightedQuickUnionUF(size + 2);
	} 
	
	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		valid_row_col(row, col);
		// input is 1-N, row/col is 0 - N-1
		row--;
		col--;
		// if the cite has been open, it will be skipped.
		if (grid[row][col] == false) {
			grid[row][col] = true;
			numOfOpen++;
			int index = row * N + col;

			if (row == 0) {
				WQUF.union(index, size); // [size] is virtual top; 
			}

			if (row == N - 1) {
				WQUF.union(index, size + 1); // [size - 1] is virtual bot;
			}

			// Corner Check
			// if the cite is not at the edge (top/bot/left/right),
			// it unions near opening cites.
			if (col + 1 < N) {
				if (grid[row][col + 1]) {
					WQUF.union(index, index + 1);
				}
			}
			if (col - 1 >=  0) {
				if (grid[row][col - 1]) {
					WQUF.union(index, index - 1);
				}
			}
			if (row + 1 < N) {
				if (grid[row + 1][col]) {
					WQUF.union(index, index + N);
				}
			}
			if (row - 1 >= 0) {
				if (grid[row - 1][col]) {
					WQUF.union(index, index - N);	
				}
			}

			/*
			if (row == N - 1) {
				if (isFull(row + 1, col + 1)) {
					WQUF.union(index, size + 1); // [size - 1] is virtual bot;
				}	
			}
			*/
		}
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		valid_row_col(row, col);
		row--;
		col--;
		return this.grid[row][col];
	}  
	// is site (row, col) full?
   	public boolean isFull(int row, int col) {
		valid_row_col(row, col);
		row--;
		col--;
		return isOpen(row + 1, col + 1) && WQUF.connected(row * N + col, size);
	}
	// number of open sites
   	public	int numberOfOpenSites() {
		return numOfOpen;
	}   
	// does the system percolate?
   	public boolean percolates() {
		if (N == 1) {
			return isOpen(1, 1);
		}else{
			return WQUF.connected(size, size + 1);
		}
	}     

	// is input valid?
	private void valid_row_col(int row, int col) {
		if (row < 1 || row > N || col < 1 || col > N) {
			throw new IllegalArgumentException("Invalid input ! Index is out of bound. row is " + row + "; col is " + col);
		}
	}

	private void valid_size(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException("Invalid input ! Index is out of bound. N is " + N);
		}
	}

	// test client (optional)
   	public static void main(String[] args)   
   	{
		int n = Integer.parseInt(args[0]); // read grid size
		test_case4(n);		

   	}

	private static void test_case4(int n) {
		double randSeed;
		final double prob = 0.594; // probability of a site to open
		final int iterNum = 100;
		int iterCount = 0;
		for (int iteration = 0; iteration < iterNum; iteration++) {
			Percolation P_test = new Percolation(n);
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					randSeed = StdRandom.uniform();
					if (randSeed < prob) {
						P_test.open(i,j);
					}
				}
			}
			if (P_test.percolates()) {
				iterCount++;
			}
		}
		double rate = (double) iterCount / iterNum;
		System.out.println("percolate rate is " + rate);
	}	
}
