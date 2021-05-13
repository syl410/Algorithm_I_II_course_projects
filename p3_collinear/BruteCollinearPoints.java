import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
 
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private final Point[] points;
    private final LineSegment[] lineSegments;
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // check exception
        // check if points is null
        if (points == null) throw new IllegalArgumentException();
        // check if points contain null
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
        }
        // check if there are duplicated points
        this.points = points.clone();
        Arrays.sort(this.points);
        for (int i = 0; i < this.points.length - 1; i++) {
            if (this.points[i].compareTo(this.points[i + 1]) == 0) throw new IllegalArgumentException();
        }
        this.lineSegments = this.createSegments();
    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // collect line segments by brute force
    public LineSegment[] segments() {
        return lineSegments.clone();
    }
    private LineSegment[] createSegments() {
        ArrayList<LineSegment> lineList = new ArrayList<LineSegment>();
        // Arrays.sort(points);
        int pSize = points.length;
        for (int i = 0; i < pSize - 3; i++) {
            Point pi = points[i];
            Comparator<Point> piCmp = pi.slopeOrder();
            for (int j = i + 1; j < pSize - 2; j++) {
                Point pj = points[j];
                for (int k = j + 1; k < pSize - 1; k++) {
                    Point pk = points[k];
                    if (piCmp.compare(pj, pk) != 0) continue;
                    for (int n = k + 1; n < pSize; n++) {
                        Point pn = points[n];
                        if (piCmp.compare(pn, pk) != 0) continue;
                        LineSegment line1 = new LineSegment(pi, pn);
                        lineList.add(line1);
                    }
                }
            }
        }
        int size = lineList.size();
        LineSegment[] lineSegs = new LineSegment[size];
        for (int i = 0; i < size; i++) {
            lineSegs[i] = lineList.get(i);
        }
        return lineSegs;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
