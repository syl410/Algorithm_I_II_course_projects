import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final double[] probArr; // probArr is to have probability of each trial
	private final int T;	// T times trials
	private final int N;	// N is for creating NxN grid
	private final double mean_x;
	private final double stddev_s;
	private final static double Std_var = 1.96;
	private final double conLo;
	private final double conHi;

	// perform trials independent experiments on an n-by-n grid
   	public PercolationStats(int n, int trials) {
		this.T = trials;
		this.N = n;
		int row = 0;
		int col = 0;
		int gridNum = (int) Math.pow(N, 2); // size of N*N grid
		if (n <= 0 || trials <= 0) {
			throw new IllegalArgumentException("Invalid input! n is " + n + "; trials is " + trials);
		}
		this.probArr = new double[trials];
		for (int i = 0; i < trials; i++) {
			Percolation P_grid = new Percolation(n); // create NxN grid
			while(!P_grid.percolates()) { 
				row = (int) (StdRandom.uniform() * n) + 1;
				col = (int) (StdRandom.uniform() * n) + 1;
				P_grid.open(row, col);		
			}
			// when the grid is percolated, calculate probability
			probArr[i] = (double) P_grid.numberOfOpenSites() / (double) gridNum;
		}
		mean_x = StdStats.mean(probArr);
		stddev_s = StdStats.stddev(probArr);
		conHi = mean_x + Std_var * stddev_s / Math.sqrt(T);
		conLo = mean_x - Std_var * stddev_s / Math.sqrt(T);
	}    

	// Monte Carlo simulation
	private void simulation() {
		System.out.println("mean                    = " + mean());
		System.out.println("stddev                  = " + stddev());
		System.out.println("95% confidence interval = [" + confidenceLo() + ", " + confidenceHi() + "]");

	}
	// sample mean of percolation threshold
   	public double mean() {
		return mean_x;
	}                     
   	// sample standard deviation of percolation threshold
   	public double stddev() {
		return stddev_s;
	}                     
   	// low  endpoint of 95% confidence interval
   	public double confidenceLo() {
		return conLo;
	}                  
   	// high endpoint of 95% confidence interval
   	public double confidenceHi() {
		return conHi;
	}                

	// main is for test
	// output sample:
	// mean                    = 0.5933216250000001
	// stddev                  = 0.009114855026005652
	// 95% confidence interval = [0.5920583705435096, 0.5945900355971293]
   	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[0]);
		PercolationStats P_test = new PercolationStats(n, trials);
		P_test.simulation();
	}
}
