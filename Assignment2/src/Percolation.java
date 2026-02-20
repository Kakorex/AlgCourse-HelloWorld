import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final boolean[][] grid;
    private final int size;
    private int openSites;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        this.size = n;
        this.openSites = 0;
        this.grid = new boolean[n][n];

        int totalSites = n * n;
        this.virtualTop = 0;
        this.virtualBottom = totalSites + 1;

        this.uf = new WeightedQuickUnionUF(totalSites + 2);
        this.ufFull = new WeightedQuickUnionUF(totalSites + 1);
    }
    public void open(int row, int col) {
        validation(row, col);
        if (isOpen(row, col)) return;

        openSites++;
        grid[row - 1][col - 1] = true;

        if (row == 1) {
            uf.union(getIndex(row, col), virtualTop);
            ufFull.union(getIndex(row, col), virtualTop);
        }
        if (row == size) uf.union(getIndex(row, col), virtualBottom);

        int[][] neighbors = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
        for (int[] neighbor : neighbors) {
            int r = neighbor[0], c = neighbor[1];
            if (r >= 1 && r <= size && c >= 1 && c <= size && isOpen(r, c)) {
                uf.union(getIndex(row, col), getIndex(r, c));
                ufFull.union(getIndex(row, col), getIndex(r, c));
            }
        }
    }
    public boolean isOpen(int row, int col) {
        validation(row, col);
        return grid[row - 1][col - 1];
    }
    public boolean isFull(int row, int col) {
        validation(row, col);
        return ufFull.find(getIndex(row, col)) == ufFull.find(virtualTop);
    }
    public int numberOfOpenSites() {
        return openSites;
    }
    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }
    private void validation(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
    }
    private int getIndex(int row, int col) {
        return (row - 1) * size + col;
    }
    public static void drawGrid(Percolation p, int size) {
        System.out.println("———————————————");
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (p.isOpen(i, j)) {
                    if (p.isFull(i, j)) System.out.print("[·]");
                    else System.out.print("[ ]");
                }
                else System.out.print("[#]");
            }
            System.out.print("\n");
        }
    }
    public static void main(String[] args) {
        int size = 10;
        Percolation p = new Percolation(size);
        while (!p.percolates()) {
            int row = StdRandom.uniformInt(1, size + 1);
            int col = StdRandom.uniformInt(1, size + 1);
            drawGrid(p, size);
            p.open(row, col);
        }
        drawGrid(p, size);
    }
}
