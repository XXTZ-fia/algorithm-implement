public class Percolation {
    private int matrixLength;
    private boolean[] matrix;
    private WeightedQuickUnionUF x;
    private int virtualTop;
    private int virtualbottom;

    public Percolation(int N) {
        if(N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        matrixLength = N;
        virtualTop = matrixLength * matrixLength;
        virtualbottom = matrixLength * matrixLength + 1;
        matrix = new boolean[N * N];
        x = new WeightedQuickUnionUF(N * N + 2);
    }

    private void check(int row, int col) {
        if(row <= 0 || row > matrixLength) {
            throw new IndexOutOfBoundsException("row index out of bounds");
        }
        if(col <= 0 || col > matrixLength) {
            throw new IndexOutOfBoundsException("col index out of bounds");
        }
    }

    private int transfer(int row,int col){
        return (row - 1) * matrixLength + col - 1;
    }

    public void open(int row, int col) {
        check(row, col);
        int real = transfer(row, col);
        if(matrix[real]) {
            return;
        }
        matrix[real] = true;
        
        if(row == 1) {
            x.union(real, virtualTop);
        }

        if(row == matrixLength) {
            x.union(real, virtualbottom);
        }

        int y;

        if(row > 1) {
            y = transfer(row - 1, col);
            if(matrix[y]) {
                x.union(real, y);
            }
        }

        if(row < matrixLength) {
            y = transfer(row + 1, col);
            if(matrix[y]) {
                x.union(real, y);
            }
        }

        if(col > 1) {
            y = transfer(row, col - 1);
            if(matrix[y]) {
                x.union(real, y);
            }
        }

        if(col < matrixLength) {
            y = transfer(row, col + 1);
            if(matrix[y]) {
                x.union(real, y);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        check(row, col);
        return matrix[transfer(row, col)];
    }

    public boolean isFull(int row, int col) {
        check(row, col);
        return x.connected(transfer(row, col), virtualTop);
    }

    public boolean percolates() {
        return x.connected(virtualTop, virtualbottom);
    }


}

