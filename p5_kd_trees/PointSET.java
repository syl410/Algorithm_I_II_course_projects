import java.util.LinkedList;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        this.pointSet = new TreeSet<Point2D>();
    }

    // is the set empty?         
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set     
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)    
    public void insert(Point2D p) {
        checkNull(p);
        pointSet.add(p);    
    }   
    // does the set contain point p?    
    public boolean contains(Point2D p) {
        checkNull(p);
        return pointSet.contains(p);
    }  
    // draw all points to standard draw    
    public void draw() {
    
    }
    // all points that are inside the rectangle (or on the boundary)    
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        LinkedList<Point2D> pointList = new LinkedList<Point2D>();
        for (Point2D p : pointSet) {
            if (pInRect(p, rect)) pointList.add(p);
        }
        return pointList;
    }

    // is p inside a rectangle?
    private boolean pInRect(Point2D p, RectHV rect) {
        if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) return true;
        else return false;
    }

    private void checkNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    // a nearest neighbor in the set to point p; null if the set is empty    
    public Point2D nearest(Point2D p) {
        checkNull(p);
        Point2D closestP = null;
        double dist = 0;
        boolean started = false;
        for (Point2D eachP : pointSet) {
            double newDist = p.distanceTo(eachP);
            if (!started) {
                closestP = eachP;
                dist = newDist;
                started = true;
                continue;
            }
            if (newDist < dist) {
                closestP = eachP;
                dist = newDist;
            }
        }
        return closestP;
    }             

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    
    }                
}
