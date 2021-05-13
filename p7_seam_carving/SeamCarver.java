import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private static final int BORDER_ENERGY = 1000;
    private Picture picture;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }
    
    // current picture
    public Picture picture() {
        Picture newPicture = new Picture(width(), height());
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                newPicture.set(col, row, picture.get(col, row));
            }
        }
        return newPicture;
    }
    
    // width of current picture
    public int width() {
        return this.width;
    }
    
    // height of current picture
    public int height() {
        return this.height;
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // throw exception for illegal arguments
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException();
        }
        // energy of border pixels are 1000
        if (x == 0 || x == width -1 || y == 0 || y == height - 1) {
            return BORDER_ENERGY;
        }
        Color xLeft = picture.get(x - 1, y);
        Color xRight = picture.get(x + 1, y);
        Color yTop = picture.get(x, y - 1);
        Color yBot = picture.get(x, y + 1);
        double deltaXGradientSquare = rgbDiffSquare(xLeft, xRight);
        double deltaYGradientSquare = rgbDiffSquare(yTop, yBot);
        return Math.sqrt(deltaXGradientSquare + deltaYGradientSquare);
    }

    private double rgbDiffSquare(Color c1, Color c2) {
        double redDiffSqure = diffSquare(c1.getRed(), c2.getRed());
        double greenDiffSqure = diffSquare(c1.getGreen(), c2.getGreen());
        double blueDiffSqure = diffSquare(c1.getBlue(), c2.getBlue());
        return redDiffSqure + greenDiffSqure + blueDiffSqure;
    }

    private double diffSquare(int value1, int value2) {
        return Math.pow(value1 - value2, 2);
    }

    private void initialization(double[][] energy) {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = Double.POSITIVE_INFINITY;
            }
        }
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[][] lastEdge = new int[width()][height()];
        double[][] pathEnergy = new double[width()][height()];
        initialization(pathEnergy);

        for (int row = 0; row < height(); row++) {
            lastEdge[0][row] = -1;
            pathEnergy[0][row] = energy(0, row);
        }

        for (int col = 1; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                double curEng = energy(col, row); // current energy
                int minRow = Math.max(0, row - 1);
                int maxRow = Math.min(height() - 1, row + 1);
                for (int y = minRow; y <= maxRow; y++) {
                    double prevPathEng = pathEnergy[col - 1][y]; // previous pathEnergy
                    double curPathEng = pathEnergy[col][row]; // current path energy
                    double newPathEng = curEng + prevPathEng;
                    if (newPathEng < curPathEng) {
                        pathEnergy[col][row] = newPathEng;
                        lastEdge[col][row] = y;
                    }
                }
            }
        }

        return getHorizontalSeamArr(lastEdge, pathEnergy);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] lastEdge = new int[width()][height()];
        double[][] pathEnergy = new double[width()][height()];
        initialization(pathEnergy);

        for (int col = 0; col < width(); col++) {
            lastEdge[col][0] = -1;
            pathEnergy[col][0] = energy(col, 0);
        }

        for (int row = 1; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                double curEng = energy(col, row); // current energy
                int minCol = Math.max(0, col - 1);
                int maxCol = Math.min(width() - 1, col + 1);
                for (int x = minCol; x <= maxCol; x++) {
                    double prevPathEng = pathEnergy[x][row - 1]; // previous pathEnergy
                    double curPathEng = pathEnergy[col][row]; // current path energy
                    double newPathEng = curEng + prevPathEng;
                    if (newPathEng < curPathEng) {
                        pathEnergy[col][row] = newPathEng;
                        lastEdge[col][row] = x;
                    }
                }
            }
        }
        return getVerticalSeamArr(lastEdge, pathEnergy);
    }

    private int[] getVerticalSeamArr(int[][] lastEdge, double[][] pathEnergy) {
        double minEng = Double.POSITIVE_INFINITY;
        int lastRow = height() - 1;
        int minColNum = -1;
        // find col number of min path energy in last row
        for (int x = 0; x < width(); x++) {
            if (pathEnergy[x][lastRow] < minEng) {
                minEng = pathEnergy[x][lastRow];
                minColNum = x;
            }
        }

        int[] verticalSeamArr = new int[height()];
        verticalSeamArr[height() - 1] = minColNum;
        int prevCol = minColNum;
        // trace lastEdge one by one
        for (int row = height() - 1; row >= 1; row--) {
            prevCol = lastEdge[prevCol][row];
            verticalSeamArr[row - 1] = prevCol;
        }
        return verticalSeamArr;
    }

    private int[] getHorizontalSeamArr(int[][] lastEdge, double[][] pathEnergy) {
        double minEng = Double.POSITIVE_INFINITY;
        int lastCol = width() - 1;
        int minRowNum = -1;
        // find row number of min path energy in last column
        for (int y = 0; y < height(); y++) {
            if (pathEnergy[lastCol][y] < minEng) {
                minEng = pathEnergy[lastCol][y];
                minRowNum = y;
            }
        }

        int[] horizontalSeamArr = new int[width()];
        horizontalSeamArr[width() - 1] = minRowNum;
        int prevRow = minRowNum;
        // trace lastEdge one by one
        for (int col = width() - 1; col >= 1; col--) {
            prevRow = lastEdge[col][prevRow];
            horizontalSeamArr[col - 1] = prevRow;
        }
        return horizontalSeamArr;
    }
    
    private void inRowRange(int row) {
        if (row < 0 || row >= height()) throw new IllegalArgumentException();
    }

    private void inColRange(int col) {
        if (col < 0 || col >= width()) throw new IllegalArgumentException();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1 || seam == null || seam.length != width()) throw new IllegalArgumentException();
        int last = seam[0];
        inRowRange(last);
        for (int col = 1; col < width(); col++) {
            inRowRange(seam[col]);
            if (Math.abs(seam[col] - last) > 1) throw new IllegalArgumentException();
            last = seam[col];
        }
        
        Picture newPicture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                if (row < seam[col]) {
                    newPicture.set(col, row, picture.get(col, row));
                } else {
                    newPicture.set(col, row, picture.get(col, row + 1));
                }
            }
        }
        picture = newPicture;
        height--;
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1 || seam == null || seam.length != height()) throw new IllegalArgumentException();
        int last = seam[0];
        inColRange(last);
        for (int row = 1; row < height(); row++) {
            inColRange(seam[row]);
            if (Math.abs(seam[row] - last) > 1) throw new IllegalArgumentException();
            last = seam[row];
        }
        
        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width() - 1; col++) {
                if (col < seam[row]) {
                    newPicture.set(col, row, picture.get(col, row));
                } else {
                    newPicture.set(col, row, picture.get(col + 1, row));
                }
            }
        }
        picture = newPicture;
        width--;
    }
    
    //  unit testing (optional)
    public static void main(String[] args) {
    }
}
