package r4_final;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import javafx.util.Duration;

/**
 * A simple circle hit object
 * @author Nicholas Gadjali
 */
public class CircleHO implements HitObject, Serializable {
	private static final double CIRCLE_BORDER = 0.1; //0.1 of circle diameter is border
	
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
		setColor(Color.lightGray);
	}

	/**
	 * Draw circle relative to given window dimension
	 * @param windowDimension Dimension with panel dimensions
	 */
	@Override
	public void draw(Graphics2D g2, Dimension windowDimension) {
		this.wd = windowDimension;
		
		Ellipse2D.Double circle = new Ellipse2D.Double(getX(), getY(), getSize(), getSize());
		double innerOffset = CIRCLE_BORDER * getSize();
		Ellipse2D.Double innerCircle = new Ellipse2D.Double(getX() + innerOffset / 2, getY() + innerOffset / 2, getSize() - innerOffset, getSize() - innerOffset);
		
		double arSize = getSize() * arFraction;
		double offset = (arSize - getSize()) / 2;
		Ellipse2D.Double approach = new Ellipse2D.Double(getX() - offset, getY() - offset, arSize, arSize);
		
		g2.setColor(circleColor.darker()); //approach circle
		g2.setStroke(new BasicStroke((int)(innerOffset / 2)));
		g2.draw(approach);
		
		g2.setColor(Color.white); //outer circle
		g2.fill(circle);
		
		g2.setColor(circleColor); //inner circle
		g2.fill(innerCircle);
	}
	
	/**
	 * Overridden equals method
	 * @param o the object to compare to
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		HitObject that = (HitObject)o;
		if (this.getHitTime().equals(that.getHitTime()) && this.getX() == that.getX() && this.getY() == that.getY() && this.getColor().equals(that.getColor())) return true;
		else return false;
	}
	
	/**
	 * Gets the hit-time of HitObject
	 * @return hit-time in Duration
	 */
	@Override
	public Duration getHitTime() { return hitTime; }

	/**
	 * Gets the size of the circle
	 * @return The size of the circle in pixels
	 */
	@Override
	public double getSize() { return size * wd.getWidth(); }
	
	/**
	 * Gets the upper left xCoord pixel
	 * @return The upper left xCoord pixel
	 */
	@Override
	public double getX() { return x * wd.getWidth(); }
	
	/**
	 * Gets the upper left yCoord pixel
	 * @return The upper left yCoord pixel
	 */
	@Override
	public double getY() { return y * wd.getHeight(); }
	
	/**
	 * Gets the HitObject % relative to width
	 * @return HitObject size % relative to width
	 */
	@Override
	public double getSizeFrac() { return size; }
	
	/**
	 * Gets the upper left xCoord % relative to width
	 * @return The upper left xCoord % relative to width
	 */
	@Override
	public double getXFrac() { return x; }
	
	/**
	 * Gets the upper left yCoord % relative to width
	 * @return The upper left yCoord % relative to width
	 */
	@Override
	public double getYFrac() { return y; }
	
	/**
	 * Sets approach circle size
	 * @return The approach circle fraction size
	 */
	public double getARFraction() { return arFraction; }
	
	/**
	 * Sets approach circle size
	 * @param ar The approach circle size
	 */
	@Override
	public void setARFraction(double ar) { this.arFraction = ar; }
	
	/**
	 * Sets the circle color for drawing
	 * @param c The new circle color
	 */
	@Override
	public void setColor(Color c) { this.circleColor = c; }
	
	/**
	 * Gets the circle color for drawing
	 * @return circle color
	 */
	@Override
	public Color getColor() {
		return this.circleColor;
	}
	
	private Color circleColor;
	private Duration hitTime;
	private double x;
	private double y;
	private double size;
	private double arFraction;
	private Dimension wd;
}
