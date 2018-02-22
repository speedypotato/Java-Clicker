package r3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javafx.util.Duration;

public class CircleHO implements HitObject {
	/**
	 * Creates a Circle HitObject
	 * @param time The hit-time of HitObject
	 * @param size The size of the Circle (relative to window size)
	 * @param x The upper left xCoord (relative to window size)
	 * @param y The upper left yCoord (relative to window size)
	 */
	public CircleHO(Duration time, double size, double x, double y) {
		this.hitTime = time;
		this.size = size;
		this.x = x;
		this.y = y;
		this.arFraction = 1;
	}

	/**
	 * Draw circle relative to given window dimension
	 */
	@Override
	public void draw(Graphics2D g2, Dimension windowDimension) {
		this.wd = windowDimension;
		Ellipse2D.Double circle = new Ellipse2D.Double(getX(), getY(), getSize(), getSize());
		
		double arSize = getSize() * arFraction;
		double offset = (arSize - getSize()) / 2;
		Ellipse2D.Double approach = new Ellipse2D.Double(getX() - offset, getY() - offset, arSize, arSize);
		
		g2.setColor(Color.darkGray);
		g2.fill(circle);
		g2.setColor(Color.black);
		g2.draw(approach);
	}
	
	public Duration getHitTime() { return hitTime; }

	public double getSize() { return size * wd.getWidth(); }
	public double getX() { return x * wd.getWidth(); }
	public double getY() { return y * wd.getHeight(); }
	
	public double getSizeFrac() { return size; }
	public double getXFrac() { return x; }
	public double getYFrac() { return y; }
	
	public double getARFraction() { return arFraction; }
	
	public void setARFraction(double ar) { this.arFraction = ar; }
	
	private Duration hitTime;
	private double x;
	private double y;
	private double size;
	private double arFraction;
	private Dimension wd;
}
