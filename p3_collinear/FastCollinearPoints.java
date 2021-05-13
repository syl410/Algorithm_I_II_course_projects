import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
 
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private final Point[] points;
    private final LineSegment[] lineSegments;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        this.lineSegments = createSegments();
    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }
    private boolean allSameOrder(ArrayList<Point> sortedPoints, int idx, Point p, int count) {
        for (int i = idx - count; i < idx; i++) {
            if (p.compareTo(sortedPoints.get(i)) > 0) return false;
            p = sortedPoints.get(i);
        }
        return true;
    }

    private void addLineToList(ArrayList<LineSegment> lineList, ArrayList<Point> sortedPoints, int idx, Point p) {
        LineSegment line1 = new LineSegment(p, sortedPoints.get(idx - 1));
        lineList.add(line1);
    }
    
    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // create line segments
    private LineSegment[] createSegments() {
        ArrayList<LineSegment> lineList = new ArrayList<LineSegment>();
        for (Point point : points) {
            Comparator<Point> slopeOrderCmp = point.slopeOrder();
            ArrayList<Point> sortedPoints = new ArrayList<>(Arrays.asList(points)); 

            Collections.sort(sortedPoints, slopeOrderCmp);

            int count = 1;
            int i = 1;
            for (; i < sortedPoints.size(); i++) {
                if (slopeOrderCmp.compare(sortedPoints.get(i), sortedPoints.get(i - 1)) == 0) {
                    count++;
                } else {
                    if (count >= 3) {
                        if (allSameOrder(sortedPoints, i, point, count)) {
                            addLineToList(lineList, sortedPoints, i, point);
                        }        
                    } 
                    count = 1;
                }
            }
            if (count >= 3) {
                if (allSameOrder(sortedPoints, i, point, count)) {
                    addLineToList(lineList, sortedPoints, i, point);
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
