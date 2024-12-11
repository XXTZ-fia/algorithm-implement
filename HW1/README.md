Question1：
For the Percolation method:
The time complexity of the constructor Percolation(int N) is O(N^2).
The complexity of open(int i, int j) is O(1).
isOpen(int i, int j) has a time complexity O(1).
The time complexity of union(int p, int q) is O(1).
while (! percolation.percolates()) has a time complexity of O(N^2).

For the PercolationStats method:
The constructor PercolationStats(int N, int T) calls the constructor of Percolation T times, each with a time complexity of O(N^2).
The time complexity of mean(), stddev(), confidenceLow(), confidenceHigh() is O(T).
The total time complexity is O(T*N^2).

When N becomes 2N, the total run time increases by 4 times.
When T becomes 2T, the total running time is doubled.

Question2:
matrix: N^2                 bytes
parents[]:(N*N+2)*4         bytes
size[]:(N*N+2)*4            bytes
matrixLength：4             bytes
virtualTop:4                bytes
virtualbottom:4             bytes
x:8                         bytes
all:8N^2