/*
 * Sergei Kostevitch
 * May 10, 2012
 */

package routing;

/**
 * Routing Coordinates - abstract class used for redifinition by Straight and Circular Routing Types
 * @author SergeiKostevitch
 *
 */

public abstract class Coordinate {

	protected double x = 0.0;
	protected double y = 0.0;

	public Coordinate(double x, double y){
		
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
