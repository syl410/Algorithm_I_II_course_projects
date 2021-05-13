import java.util.LinkedList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private Node root;
    private int size;
    private static final boolean V = false; // VERTICAL
    private static final boolean H = true; // HORIZONTAL
    private class Node {
        private Node leftOrBot;
        private Node rightOrTop;
        private final Point2D p;
        private final boolean dir;

        Node(Point2D p, boolean dir) {
            this.p = p;
            this.dir = dir;
        }

        public boolean rectOverlapLine(RectHV rect) {
            if (this.dir == V) {
                if (rect.xmin() <= p.x() && rect.xmax() >= p.x()) return true;
                else return false;
            } else {
                if (rect.ymin() <= p.y() && rect.ymax() >= p.y()) return true;
                else return false;          
            }
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?         
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set     
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)    
    public void insert(Point2D p) {
        checkNull(p);
        if (size == 0) {
            root = new Node(p, V);
        } else {
            Node n = root;
            while (true) {
                double cmp = comparePoints(p, n);
                if (cmp < 0) {
                    if (n.leftOrBot == null) {
                        n.leftOrBot = new Node(p, !n.dir);
                        break;
                    }
                    n = n.leftOrBot;
                } else if (cmp ==0) {
                    return;
                } else {
                    if (n.rightOrTop == null) {
                        n.rightOrTop = new Node(p, !n.dir);
                        break;
                    }
                    n = n.rightOrTop;
                }
            }
        }
        size++;
    }

    private double comparePoints(Point2D p, Node n) {
        boolean nDir = n.dir;
        if (p.x() == n.p.x() && p.y() == n.p.y()) return 0;
        if (nDir == V) {
            if (p.x() == n.p.x()) return -1.0;
            return p.x() - n.p.x();
        } else {
            if (p.y() == n.p.y()) return -1.0;
            return p.y() - n.p.y();
        }
    }
    // does the set contain point p?    
    public boolean contains(Point2D p) {
        checkNull(p);
        if (size == 0) {
            return false;
        } else {
            Node n = root;
            while (n != null) {
                double cmp = comparePoints(p, n);
                if (cmp < 0) {
                    n = n.leftOrBot;
                } else if (cmp == 0) {
                    return true;
                } else {
                    n = n.rightOrTop;
                }
            }
        }
        return false;
    }  
    // draw all points to standard draw    
    public void draw() {
    
    }
    // all points that are inside the rectangle (or on the boundary)    
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        LinkedList<Point2D> pointList = new LinkedList<Point2D>();

        range(rect, root, pointList);

        return pointList;
    }

    private void range(RectHV rect, Node n, LinkedList<Point2D> pointList) {
        if (n == null) return;
        boolean overlap = n.rectOverlapLine(rect);
        if (overlap) {
            if (pInRect(n.p, rect)) pointList.add(n.p);
            range(rect, n.leftOrBot, pointList);
            range(rect, n.rightOrTop, pointList);
        } else {
            if (n.dir == H) {
                if (rect.ymax() < n.p.y()) range(rect, n.leftOrBot, pointList);
                else range(rect, n.rightOrTop, pointList);
            } else {
                if (rect.xmax() < n.p.x()) range(rect, n.leftOrBot, pointList);
                else range(rect, n.rightOrTop, pointList);
            }
        }
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
        Node n = nearest(p, root);
        if (n == null) return null;
        return n.p;
    }

    private Node nearest(Point2D p, Node n) {
        if (n == null) return null;
        Node newN1 = null;
        Node newN2 = null;
        if ((n.dir == H && p.y() < n.p.y()) || (n.dir == V && p.x() < n.p.x())) {
            newN1 = nearest(p, n.leftOrBot);
            if (checkOtherSide(p, newN1, n)) {
                newN2 = nearest(p, n.rightOrTop);
            }
        } else {
            newN1 = nearest(p, n.rightOrTop);
            if (checkOtherSide(p, newN1, n)) {
                newN2 = nearest(p, n.leftOrBot);
            }
        }
        return nearestNode(p, n, newN1, newN2);
    }

    private Node nearestNode(Point2D p, Node n1, Node n2, Node n3) {
        Node newN = null;
        if (n1 != null) {
            newN = n1;
        }
        if (n2 != null) {
            if (newN == null) newN = n2;
            else {
                if (p.distanceTo(n2.p) < p.distanceTo(newN.p)) newN = n2;
            }
        }

        if (n3 != null) {
            if (newN == null) newN = n3;
            else {
                if (p.distanceTo(n3.p) < p.distanceTo(newN.p)) newN = n3;
            }            
        }
        return newN;
    }


    // need to check the other side?
    private boolean checkOtherSide(Point2D p, Node n, Node originN) {
        if (n == null) return true;
        double pointDist = p.distanceTo(n.p);
        double lineDist2 = plDist(p, originN);
        if (lineDist2 < pointDist) {
            return true;
        } else return false;
    }

    // point to line distance
    private double plDist(Point2D p, Node n) {
        if (n.dir == V) {
            return Math.abs(p.x() - n.p.x());
        } else {
            return Math.abs(p.y() - n.p.y());
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    
    }                
}
