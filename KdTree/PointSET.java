import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


public class PointSET {
    private final TreeSet<Point2D> set = new TreeSet<>();

    public PointSET() {
        // create empty set
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new ArrayList<>();

        for (Point2D p : set) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (set.contains(p)) {
            return p;
        }

        double minDist = Double.POSITIVE_INFINITY;
        Point2D min = null;

        for (Point2D item : set) {
            double dist = item.distanceSquaredTo(p);

            if (dist < minDist) {
                minDist = dist;
                min = item;
            }
        }

        return min;
    }

    public static void main(String[] args) {
    }
}
