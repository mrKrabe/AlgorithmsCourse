import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

// Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.
public class SeamCarver {
    private static final int BORDER = 1000;
    private int[][] pixels;
    private boolean transposed = false;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null");
        }

        this.width = picture.width();
        this.height = picture.height();

        this.pixels = getPixels(picture);
    }

    // current picture
    public Picture picture() {
        return createPicture();
    }

    // width of current picture
    public int width() {
        return transposed ? height : width;
    }

    // height of current picture
    public int height() {
        return transposed ? width : height;
    }

    // energy of pixel at column x and row y
    public double energy(int ax, int ay) {
        int x = transposed ? ay : ax;
        int y = transposed ? ax : ay;

        validatePixel(x, y);

        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return BORDER;
        }

        return Math.sqrt((double) deltaX(x, y) + deltaY(x, y));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposed = true;

        int[] seam = findSeam();

        transposed = false;

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transposed = true;

        removeSeam(seam);

        transposed = false;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        removeSeam(seam);
    }

    private int getPixel(int x, int y) {
        return transposed ? pixels[y][x] : pixels[x][y];
    }

    private void setPixel(int x, int y, int value) {
        if (transposed) {
            pixels[y][x] = value;
        } else {
            pixels[x][y] = value;
        }
    }

    private void validatePixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("index out of bounds: " + x + " " + y);
        }
    }

    private int[] findSeam() {
        double[][] energy = calcEnergy();
        int h = height();
        int w = width();
        double[][] distTo = new double[w][h];
        int[][] edgeTo = new int[w][h];

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                setMinDist(distTo, edgeTo, energy, i, j);
            }
        }

        return findMinPath(distTo, edgeTo);
    }

    private void removeSeam(int[] seam) {
        if (seam == null || seam.length != height()) {
            throw new IllegalArgumentException("invalid argument");
        }

        int lastInd = seam[0];
        for (int i = 0; i < seam.length; i++) {
            int ind = seam[i];

            validateIndex(ind, lastInd);

            for (int j = ind + 1; j < width(); j++) {
                setPixel(j - 1, i, getPixel(j, i));
            }

            lastInd = ind;
        }

        if (transposed) {
            --height;

            for (int i = 0; i < width; i++) {
                pixels[i] = Arrays.copyOf(pixels[i], height);
            }
        } else {
            pixels[--width] = null;
        }
    }

    private void validateIndex(int ind, int lastInd) {
        if (ind < 0 || ind >= width() || Math.abs(lastInd - ind) > 1) {
            throw new IllegalArgumentException("Invalid index: " + ind);
        }
    }

    private int[][] getPixels(Picture picture) {
        int[][] result = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = picture.getRGB(i, j);
            }
        }

        return result;
    }

    private double[][] calcEnergy() {
        double[][] result = new double[width()][height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                result[i][j] = energy(i, j);
            }
        }

        return result;
    }

    private int[] findMinPath(double[][] distTo, int[][] edgeTo) {
        double min = Double.POSITIVE_INFINITY;
        int minInd = -1;
        int lastRow = distTo[0].length - 1;

        for (int i = 0; i < distTo.length; i++) {
            double value = distTo[i][lastRow];

            if (value < min) {
                min = value;
                minInd = i;
            }
        }

        return buildMinPath(edgeTo, minInd);
    }

    private int[] buildMinPath(int[][] edgeTo, int minInd) {
        int[] result = new int[edgeTo[0].length];

        result[result.length - 1] = minInd;

        for (int i = result.length - 1; i > 0; i--) {
            result[i - 1] = edgeTo[result[i]][i];
        }

        return result;
    }

    private void setMinDist(double[][] distTo, int[][] edgeTo, double[][] energy, int i, int j) {
        if (j == 0) {
            distTo[i][j] = energy[i][j];
            edgeTo[i][j] = -1;
            return;
        }

        double l = i > 0 ? distTo[i - 1][j - 1] : Double.POSITIVE_INFINITY;
        double c = distTo[i][j - 1];
        double r = i < distTo.length - 1 ? distTo[i + 1][j - 1] : Double.POSITIVE_INFINITY;
        double minEnergy;
        int minInd;

        if (r < c && r < l) {
            minEnergy = r;
            minInd = i + 1;
        } else if (c <= l && c <= r) {
            minEnergy = c;
            minInd = i;
        } else {
            minEnergy = l;
            minInd = i - 1;
        }

        distTo[i][j] = minEnergy + energy[i][j];
        edgeTo[i][j] = minInd;
    }

    private Picture createPicture() {
        Picture result = new Picture(width, height);

        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); j++) {
                result.setRGB(i, j, pixels[i][j]);
            }
        }

        return result;
    }

    private int deltaX(int x, int y) {
        return delta(pixels[x - 1][y], pixels[x + 1][y]);
    }

    private int deltaY(int x, int y) {
        return delta(pixels[x][y - 1], pixels[x][y + 1]);
    }

    private int delta(int rgb1, int rgb2) {
        int[] rgbR = toRGBArray(rgb1);
        int[] rgbL = toRGBArray(rgb2);

        int dr = rgbR[0] - rgbL[0];
        int dg = rgbR[1] - rgbL[1];
        int db = rgbR[2] - rgbL[2];

        return dr * dr + dg * dg + db * db;
    }

    private int[] toRGBArray(int rgb) {
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;

        return new int[]{red, green, blue};
    }
}
