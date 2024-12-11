import java.util.Scanner;

public class PercolationStats{
    private int matrixLength;
    private double[] threshold;
    private double mean;                //平均值
    private double stddev;              //标准偏差
    private double confidenceLow;   // 最低置信度
    private double confidenceHigh;    // 高置信度

    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        if (T <= 0) {
            throw new IllegalArgumentException("trials must be positive");
        }
        matrixLength = N;

        if(matrixLength == 1) {
            mean = 1;
            stddev = Double.NaN;
            confidenceLow = Double.NaN;
            confidenceHigh = Double.NaN;
        } else{
            threshold = new double[T];
            for(int i = 0; i < T; ++i) {
                Percolation percolation = new Percolation(N);
                int count = 0;
                do {
                    int row = (int)(Math.random() * (matrixLength)) + 1;
                    int col = (int)(Math.random() * (matrixLength)) + 1;
                    if(percolation.isOpen(row, col)) {
                        continue;
                    } else {
                        percolation.open(row, col);
                        ++count;
                    }
                } while (!percolation.percolates());
                threshold[i] = (double)count / (matrixLength * matrixLength);
            }
            mean = Computemean(threshold, T);
            stddev = Computestddev(threshold, T);
            double diff = (1.96 * stddev) / Math.sqrt(T);
            confidenceLow = mean - diff;
            confidenceHigh = mean + diff;
        }
    }

    private double Computemean(double[] threshold,int T) {
        double sum = 0;
        for(int i =0 ;i < threshold.length;++i){
            sum += threshold[i];
        }
        return sum / T;
    }

    private double Computestddev(double[] threshold,int T) {
        double sum = 0;
        for(int i = 0;i < threshold.length;++i){
            sum += (threshold[i] - mean) * (threshold[i] - mean);
        }
        return Math.sqrt(sum / (T - 1));
    }

    public double mean() {
        return mean;
    }
    // 
    public double stddev() {
        return stddev;
    }
    // 
    public double confidenceLow() {
        return confidenceLow;
    }
    // 
    public double confidenceHigh() {
        return confidenceHigh;
    }

        public static void main(String[] args) {
        System.out.println("Please enter N and T : ");  //Length  and Times
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int T = in.nextInt();
        PercolationStats percolationStats = new PercolationStats(N,T);
        System.out.println("The mean of percolationStats is " + percolationStats.mean());
        System.out.println("The stddev of percolationStats is " + percolationStats.stddev());
        System.out.println("The condidence  intervals of percolationStats is " + "[" + percolationStats.confidenceLow() + 
                                                                        " , " +  percolationStats.confidenceHigh() + "]");
        in.close();
    }
}