import java.util.ArrayList;

public class Board {
    private int[][] tiles;

    private int[] zeroLoc;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.zeroLoc = findZeroLoc(tiles);
        this.tiles = tilesCopy(tiles);
    }

    private int[][] tilesCopy(int[][] tiles) {
        if (tiles == null) return null;
        int len = tiles.length;
        int[][] newTiles = new int[len][len];
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                newTiles[row][col] = tiles[row][col];
            }
        }
        return newTiles;
    }

    private int[] findZeroLoc(int[][] tiles) {
        if (tiles == null) return null;
        int len = tiles.length;
        int[] zeroLoc = new int[2];
        zeroLoc[0] = 0;
        zeroLoc[1] = 0;
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                if (tiles[row][col] == 0) {
                    zeroLoc[0] = row;
                    zeroLoc[1] = col;
                }
            }
        }
        return zeroLoc;
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder bStr = new StringBuilder();
        int len = dimension();
        bStr.append(len + "\n");
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                bStr.append(" " + tiles[row][col]);
            }
            bStr.append("\n");
        }

        return bStr.toString();
    }

    // board dimension n
    public int dimension() {
        if (this.tiles == null) return 0;
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int len = dimension();
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                int correctNum = getCorrectNum(row, col, len);
                if (this.tiles[row][col] != correctNum && this.tiles[row][col] != 0) {
                    count++;
                }
            }
        }
        // if (len >= 1 && this.tiles[len - 1][len - 1] == 0) count--;
        return count;
    }

    private int getCorrectNum(int row, int col, int len) {
        return row * len + col + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        int len = dimension();
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                dist += manhattanDist(row, col);
            }
        }
        return dist;
    }

    private int manhattanDist(int row, int col) {
        int num = this.tiles[row][col];
        if (num == 0) return 0;
        int newRow = getRow(num);
        int newCol = getCol(num);
        return Math.abs(newRow - row) + Math.abs(newCol - col);
    }

    // get row from a number
    private int getRow(int num) {
        return (num - 1) / dimension();
    }

    // get col from a number
    private int getCol(int num) {
        return (num - 1) % dimension();
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board by = (Board) y;
        if (by.dimension() != this.dimension()) return false;

        int len = dimension();
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                if (by.tiles[row][col] != this.tiles[row][col]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<Board>();
        int row = this.zeroLoc[0];
        int col = this.zeroLoc[1];
        int len = dimension();
        // top
        if (row > 0) {
            int[][] newTiles = tilesCopy(this.tiles);
            int tmp = newTiles[row - 1][col];
            newTiles[row - 1][col] = 0;
            newTiles[row][col] = tmp;
            Board newB = new Board(newTiles);
            neighborBoards.add(newB);
        }
        // bot
        if (row < len - 1) {
            int[][] newTiles = tilesCopy(this.tiles);
            int tmp = newTiles[row + 1][col];
            newTiles[row + 1][col] = 0;
            newTiles[row][col] = tmp;
            Board newB = new Board(newTiles);
            neighborBoards.add(newB);
        }
        // left
        if (col > 0) {
            int[][] newTiles = tilesCopy(this.tiles);
            int tmp = newTiles[row][col - 1];
            newTiles[row][col - 1] = 0;
            newTiles[row][col] = tmp;
            Board newB = new Board(newTiles);
            neighborBoards.add(newB);
        }
        // right
        if (col < len - 1) {
            int[][] newTiles = tilesCopy(this.tiles);
            int tmp = newTiles[row][col + 1];
            newTiles[row][col + 1] = 0;
            newTiles[row][col] = tmp;
            Board newB = new Board(newTiles);
            neighborBoards.add(newB);
        }
        return neighborBoards;
    }



    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int len = dimension();
        if (len < 2) return null;
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len - 1; col++) {
                if (this.tiles[row][col] != 0 && this.tiles[row][col + 1] != 0) {
                    int[][] newTiles = tilesCopy(this.tiles);
                    int tmp = newTiles[row][col];
                    newTiles[row][col] = newTiles[row][col + 1];
                    newTiles[row][col + 1] = tmp;
                    return new Board(newTiles);
                }
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        
    }

}
