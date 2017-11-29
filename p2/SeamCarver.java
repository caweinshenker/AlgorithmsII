import java.util.*;
import java.lang.*;
import java.awt.Color;
import edu.princeton.cs.algs4.*;



public class SeamCarver {

	private Picture picture;
	private final int HORIZONTAL_SEAM = 0;
	private final int VERTICAL_SEAM = 1;
	private double[][] energies;


	public SeamCarver(Picture picture){
		if (picture == null)
			throw new java.lang.IllegalArgumentException("Null picture");

		this.picture = picture;

	}

	public Picture picture() {
		return new Picture(picture);
	}

	public int width() {
		return picture.width();
	}


	public int height()  {
		return picture.height();
	}

	public double energy(int col,  int row)  {
		if (col < 0 || col >= width() || row < 0 || row >= height()){
			throw new java.lang.IllegalArgumentException("Coordinates out of bounds");
		}

		if (col == 0 || col == width() - 1 || row == 0 || row == height() - 1)
			return 1000;

		return gradient(col, row);
	}

	private double gradient(int col, int row) {
		Color left = picture.get(col - 1, row);
		Color right = picture.get(col + 1, row);
		Color top = picture.get(col, row - 1);
		Color bottom = picture.get(col, row + 1);

		return Math.pow((centralDifferences(left, right) +
		 		centralDifferences(top, bottom)), 0.5);
	}

	private double centralDifferences(Color c1, Color c2) {
		return Math.pow(c1.getRed() - c2.getRed(), 2) +
			   Math.pow(c1.getBlue() - c2.getBlue(), 2) +
			   Math.pow(c1.getGreen() - c2.getGreen(), 2);
	}

	public int[] findHorizontalSeam()   {
		Picture tmpPicture = picture;
		picture = transpose(picture);
		int[] seam = findVerticalSeam();
		picture = transpose(picture);
		return seam;
	}

	public int[] findVerticalSeam()  {
		Point2D edgeTo[][] = new Point2D[height()][width()];
		double distTo[][] = new double[height()][width()];

		//StdOut.printf("Height: %d, Width: %d\n", height(), width());
		fillEnergies();
		initDistToAndEdgeTo(edgeTo, distTo);
		for (int row=0; row < height(); row++){
			for (int col=0; col < width(); col++) {
				relaxVertex(row, col, edgeTo, distTo);
			}
		}

		return trace(edgeTo, distTo);
	}

	private int[] trace(Point2D edgeTo[][], double[][] distTo) {
		int curCol = -1;
		double minDist = Double.MAX_VALUE;
		int[] seam = new int[height()];

		for (int col=0; col < width(); col++) {
			if (distTo[height() - 1][col] < minDist) {
				curCol = col;
				minDist = distTo[height() - 1][curCol];
			}
		}

		for (int row= height() - 1; row > 0; row--){
			seam[row] = curCol;
			curCol = (int) edgeTo[row][curCol].y();
		}
		seam[0] = curCol;


		return seam;
	}

	private void initDistToAndEdgeTo(Point2D[][] edgeTo, double[][] distTo) {
		for (int row=0; row < height(); row++) {
			for (int col=0; col < width(); col++) {
				distTo[row][col] = Double.MAX_VALUE;
				edgeTo[row][col] = null;
			}
		}
		for (int col=0; col < width(); col++)
			distTo[0][col] = 0;
	}

	private ArrayList<Point2D> reachableVertices(int row, int col) {
		ArrayList<Point2D> reachable = new ArrayList<Point2D>();

		if (row == height() - 1) return reachable;

		if (col > 0) reachable.add(new Point2D (row + 1, col - 1));
		if (col < width() - 1) reachable.add(new Point2D(row + 1, col + 1));
		reachable.add(new Point2D(row + 1, col));

		return reachable;
	}

	private void relaxVertex(int row, int col, Point2D[][] edgeTo, double[][] distTo) {

		ArrayList<Point2D> reachable = reachableVertices(row, col);
		for (Point2D sink : reachable)
			relaxEdge((int) sink.x(), (int) sink.y(), row, col, edgeTo, distTo);
	}

	private void relaxEdge(int row, int col, int sourceRow, int sourceCol, Point2D[][]edgeTo, double[][] distTo){
		double sourceDist = distTo[sourceRow][sourceCol];
		if (energies[row][col] + sourceDist < distTo[row][col]) {
			edgeTo[row][col] = new Point2D(sourceRow, sourceCol);
			distTo[row][col] = energies[row][col] + sourceDist;
		}
	}

	public void removeHorizontalSeam(int[] seam) {
		validateSeam(seam, HORIZONTAL_SEAM);
		if (seam == null || width() <= 1 )
			throw new java.lang.IllegalArgumentException("Cannot remove seam");
	}

	public void removeVerticalSeam(int[] seam) {
		validateSeam(seam, HORIZONTAL_SEAM);
		if (seam == null || height() <= 1)
			throw new java.lang.IllegalArgumentException("Cannot remove seam");
	}

	private void validateSeam(int[] seam, int orientation){
		int expectedSeamLength = orientation == HORIZONTAL_SEAM ? width() : height();

		if (seam == null || seam.length != expectedSeamLength)
			throw new java.lang.IllegalArgumentException("Invalid seam: wrong length");

		for (int i = 0; i < expectedSeamLength - 1; i++){
			if (i < 0 || i >= seam.length || Math.abs(seam[i+1] - seam[i]) <= 1)
				throw new java.lang.IllegalArgumentException("Invalid seam: unadjacent vertex");
		}
	}

	private void fillEnergies() {
		energies = new double[height()][width()];

		for (int row=0; row < height(); row++){
			for (int col=0; col < width(); col++) {
				energies[row][col] = energy(col, row);
			}
		}
	}

	private Picture transpose(Picture picture) {
		Picture newPicture = new Picture(height(), width());
		for (int col=0; col < width(); col++) {
			for (int row=0; row < height(); row++){
				newPicture.set(row, col, picture.get(col, row));
			}
		}
		return newPicture;
	}

}
