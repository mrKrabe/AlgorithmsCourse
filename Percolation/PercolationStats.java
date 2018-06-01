import edu.princeton.cs.algs4.StdStats;

import static edu.princeton.cs.algs4.StdRandom.uniform;

/**
 * Simulation class
 */
public class PercolationStats {
    private static final double STUD_COEF = 1.96;
    private final double std;
    private final double mean;
    private final int trials;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (trials < 1){
            throw new IllegalArgumentException("Trials must be positive: " + trials);
        }

        double[] result = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                p.open(uniform(n) + 1, uniform(n) + 1);
            }

            result[i] = (double) p.numberOfOpenSites() / (n * n);
        }

        this.mean = StdStats.mean(result);
        this.std = StdStats.stddev(result);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - STUD_COEF * std / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + STUD_COEF * std / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(1, 100);

        System.out.println("mean: " + stats.mean());
        System.out.println("stddev: " + stats.stddev());
        System.out.println("confidenceLo: " + stats.confidenceLo());
        System.out.println("confidenceHi: " + stats.confidenceHi());
    }
}
