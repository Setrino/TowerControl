/*
 * Sergei Kostevitch
 * May 10, 2012
 */

package routing;

/**
 * Routing Type Circular - contains the angle and points to go to - used in RoutingDecisionMaker
 * Used in Coordinates File for creating a List for each type of Circle
 * @author SergeiKostevitch
 *
 */

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
