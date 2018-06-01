import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *  Program examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.
 *  It uses sorting by slope with base point to find collinear segments
 */
public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validateArray(points);

        Point[] pnts = Arrays.copyOf(points, points.length);

        checkDuplicates(pnts);

        if (pnts.length < 4) {
            return;
        }

        for (Point basePoint : points) {
            // find slopes with base point
            Arrays.sort(pnts, 0, pnts.length, basePoint.slopeOrder());

            LinkedList<Point> result = new LinkedList<>();
            double lastSlope = Double.NEGATIVE_INFINITY;

            // create line segments
            for (int j = 1; j < pnts.length; j++) {
                Point point = pnts[j];
                double slope = basePoint.slopeTo(point);

                if (slope != lastSlope) {
                    addSegment(basePoint, result);

                    result.clear();
                    result.add(point);
                    lastSlope = slope;
                } else {
                    result.add(point);
                }

                if (j == pnts.length - 1) {
                    addSegment(basePoint, result);
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    private void addSegment(Point basePoint, LinkedList<Point> result) {
        if (result.size() >= 3) {
            result.sort(Comparator.naturalOrder());

            // filter duplicates by checking that the base point is the lowest in a segment
            if (basePoint.compareTo(result.get(0)) < 0) {
                segments.add(new LineSegment(basePoint, result.getLast()));
            }
        }
    }

    private void checkDuplicates(Point[] pnts) {
        Arrays.sort(pnts, (o1, o2) -> {
            if (o1 == null || o2 == null) {
                throw new IllegalArgumentException("null point");
            }

            int result = o1.compareTo(o2);
            if (result == 0) {
                throw new IllegalArgumentException("duplicate point");
            }

            return result;
        });
    }

    private void validateArray(Point[] points) {
        if (points == null || (points.length == 1 && points[0] == null)) {
            throw new IllegalArgumentException("null argument");
        }
    }
}
