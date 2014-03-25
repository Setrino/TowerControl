/*
 * Sergei Kostevitch
 * May 10, 2012
 */

package routing;

public class Circular extends Coordinate{
	
	private double angleOfRotation = 0.0;

	public Circular(double x, double y, double angleOfRotation) {
		super(x, y);
		this.angleOfRotation = angleOfRotation;
	}

	public double getAngleOfRotation() {
		return angleOfRotation;
	}

}
