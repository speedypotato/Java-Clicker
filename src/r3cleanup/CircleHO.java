package r3cleanup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javafx.util.Duration;

public class CircleHO implements HitObject {
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
		this.circleColor = Color.lightGray;
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
	 * Gets the hit-time of HitObject
	 * @return hit-time in Duration
	 */
	public Duration getHitTime() { return hitTime; }

	/**
	 * Gets the size of the circle
	 * @return The size of the circle in pixels
	 */
	public double getSize() { return size * wd.getWidth(); }
	
	/**
	 * Gets the upper left xCoord pixel
	 * @return The upper left xCoord pixel
	 */
	public double getX() { return x * wd.getWidth(); }
	
	/**
	 * Gets the upper left yCoord pixel
	 * @return The upper left yCoord pixel
	 */
	public double getY() { return y * wd.getHeight(); }
	
	/**
	 * Gets the HitObject % relative to width
	 * @return HitObject size % relative to width
	 */
	public double getSizeFrac() { return size; }
	
	/**
	 * Gets the upper left xCoord % relative to width
	 * @return The upper left xCoord % relative to width
	 */
	public double getXFrac() { return x; }
	
	/**
	 * Gets the upper left yCoord % relative to width
	 * @return The upper left yCoord % relative to width
	 */
	public double getYFrac() { return y; }
	
	/**
	 * Sets approach circle size
	 * @param ar The approach circle size
	 */
	public double getARFraction() { return arFraction; }
	
	/**
	 * Sets approach circle size
	 * @param ar The approach circle size
	 */
	public void setARFraction(double ar) { this.arFraction = ar; }
	
	/**
	 * Sets the circle color for drawing
	 * @param c The new circle color
	 */
	public void setCircleColor(Color c) { this.circleColor = c; }
	
	private Color circleColor;
	private Duration hitTime;
	private double x;
	private double y;
	private double size;
	private double arFraction;
	private Dimension wd;
}
