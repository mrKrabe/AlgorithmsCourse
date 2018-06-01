import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Program examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments
 */
public class BruteCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || (points.length == 1 && points[0] == null)) {
            throw new IllegalArgumentException("null argument");
        }

        Point[] pnts = Arrays.copyOf(points, points.length);

        checkDuplicates(pnts);

        if (pnts.length < 4) {
            return;
        }

        for (int i = 0; i < pnts.length; i++) {
            Point point = pnts[i];

            if (point == null) {
                throw new IllegalArgumentException("null point");
            }

            for (int j = i + 1; j < pnts.length; j++) {
                double slopeJ = point.slopeTo(pnts[j]);

                for (int k = j + 1; k < pnts.length; k++) {
                    if (slopeJ == point.slopeTo(pnts[k])) {
                        for (int m = k + 1; m < pnts.length; m++) {
                            if (slopeJ == point.slopeTo(pnts[m])) {
                                segments.add(new LineSegment(point, pnts[m]));
                                break;
                            }
                        }
                    }
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

}
