import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

/**
 *  A set of points in the unit square (all points have x- and y-coordinates between 0 and 1)
 *  using a 2d-tree to support efficient range search and nearest-neighbor search
 */
public class KdTree {
    private int size = 0;
    private Node root;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
    }

    public KdTree() {
        // create empty tree
    }

    // is the set empty
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validatePoint(p);

        root = insert(root, p, false, 0, null);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);

        if (isEmpty()) {
            return null;
        }

        return nearest(root, p, root.p, false);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validatePoint(p);

        return get(root, p, false) != null;
    }

    public void draw() {
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);

        List<Point2D> result = new ArrayList<>();

        addToRange(root, rect, result);

        return result;
    }

    private void validatePoint(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null point");
        }
    }

    private void validateRect(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("null rect");
        }
    }

    private void addToRange(Node r, RectHV rect, List<Point2D> result) {
        if (r == null || !r.rect.intersects(rect)) {
            return;
        }

        if (rect.contains(r.p)) {
            result.add(r.p);
        }

        addToRange(r.lb, rect, result);
        addToRange(r.rt, rect, result);
    }

    private Point2D nearest(Node r, Point2D p, Point2D pMin, boolean direction) {
        if (r == null || pMin.distanceSquaredTo(p) < r.rect.distanceSquaredTo(p)) {
            return pMin;
        }

        pMin = pMin.distanceSquaredTo(p) < r.p.distanceSquaredTo(p) ? pMin : r.p;
        int cmp = direction ? Double.compare(p.y(), r.p.y()) : Double.compare(p.x(), r.p.x());

        if (cmp < 0) {
            pMin = nearest(r.lb, p, pMin, !direction);
            pMin = nearest(r.rt, p, pMin, !direction);
        } else {
            pMin = nearest(r.rt, p, pMin, !direction);
            pMin = nearest(r.lb, p, pMin, !direction);
        }

        return pMin;
    }

    private Node insert(Node r, Point2D p, boolean direction, int side, Point2D parentPoint) {
        // false for x, true for y
        if (r == null) {
            Node node = new Node();
            node.rect = getRect(direction, side, parentPoint);
            node.p = p;

            size++;

            return node;
        }

        int cmp = direction ? Double.compare(p.y(), r.p.y()) : Double.compare(p.x(), r.p.x());

        if (cmp < 0) {
            r.lb = insert(r.lb, p, !direction, cmp, r.p);
        } else if (!r.p.equals(p)) {
            r.rt = insert(r.rt, p, !direction, cmp, r.p);
        }

        return r;
    }

    private Point2D get(Node r, Point2D p, boolean direction) {
        // false for x, true for y
        if (r == null) {
            return null;
        }

        if (r.p.equals(p)) {
            return p;
        }

        int cmp = direction ? Double.compare(p.y(), r.p.y()) : Double.compare(p.x(), r.p.x());
        if (cmp < 0) {
            return get(r.lb, p, !direction);
        }

        return get(r.rt, p, !direction);
    }

    private RectHV getRect(boolean direction, int side, Point2D p) {
        // rectangle that covers point's subtree
        // side: -1 - left, 1 - right
        double xMin = 0;
        double yMin = 0;
        double xMax = 1;
        double yMax = 1;

        if (p != null) {
            if (side < 0) {
                if (direction) {
                    xMax = p.x();
                } else {
                    yMax = p.y();
                }
            } else {
                if (direction) {
                    xMin = p.x();
                } else {
                    yMin = p.y();
                }
            }
        }

        return new RectHV(xMin, yMin, xMax, yMax);
    }

    public static void main(String[] args) {
        KdTree t = new KdTree();

        t.insert(new Point2D(0.7, 0.2));
        t.insert(new Point2D(0.5, 0.4));
        t.insert(new Point2D(0.2, 0.3));
        t.insert(new Point2D(0.4, 0.7));
        t.insert(new Point2D(0.9, 0.6));
        t.nearest(new Point2D(0.3, 0.98));
    }
}
