import java.awt.*;
import java.awt.geom.*;

import javax.swing.Icon;

import javafx.util.Duration;

/**
 * A simple circle HitObject
 * @author Nicholas Gadjali
 */
public class CircleHO implements HitObject {
	/**
	 * Creates a Circle HitObject
	 * @param type The type of HitObject
	 * @param time2 The hit-time of HitObject
	 * @param size The size of the Circle (relative to window size)
	 * @param x The upper left xCoord (relative to window size)
	 * @param y The upper left yCoord (relative to window size)
	 * @param windowDimension The window dimension.  Used to calculate x, y, and size
	 */
	public CircleHO(String type, Duration time2, double size, double x, double y, Dimension windowDimension) {
		this.type = type;
		this.time = time2;
		this.size = size;
		this.x = x;
		this.y = y;
		this.arFraction = 1;
		this.wd = windowDimension;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Duration getHitTime() {
		return time;
	}

	@Override
	public double getSize() {
		return size * wd.getWidth();
	}

	@Override
	public double getX() {
		return x * wd.getWidth();
	}

	@Override
	public double getY() {
		return y * wd.getHeight();
	}
	
	public double getARFraction() {
		return arFraction;
	}
	
	@Override
	public void setARFraction(double ar) {
		this.arFraction = ar;
	}
	
	/**
	 * Draws the circle without the hit circle
	 * @param g2 Graphics2D
	 */
	@Override
	public void draw(Graphics2D g2) {
		Ellipse2D.Double circle = new Ellipse2D.Double(getX(), getY(), getSize(), getSize());
		
		double arSize = getSize() * arFraction;
		double offset = (arSize - getSize()) / 2;
		Ellipse2D.Double approach = new Ellipse2D.Double(getX() - offset, getY() - offset, arSize, arSize);
		
		g2.setColor(Color.lightGray);
		g2.fill(circle);
		g2.setColor(Color.darkGray);
		g2.draw(approach);
	}
	
	@Override
	public String toString() {
		return type + "," + time.toString() + "," + size + "," + x + "," + y + "," + wd.toString();
	}
	
	private String type;
	private Duration time;
	private double size;
	private double x;
	private double y;
	private double arFraction;
	private Dimension wd;
}
