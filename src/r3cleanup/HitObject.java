package r3cleanup;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javafx.util.Duration;

public interface HitObject {
	/**
	 * Draw the HitObject at its relative position
	 * @param g Graphics2D
	 */
	void draw(Graphics2D g, Dimension windowDimension);
	
	/**
	 * Gets the hit-time of HitObject
	 * @return hit-time in Duration
	 */
	public Duration getHitTime();
	
	/**
	 * Sets approach circle size
	 * @param ar The approach circle size
	 */
	public void setARFraction(double ar);
	
	/**
	 * Gets the HitObject % relative to width
	 * @return HitObject size % relative to width
	 */
	public double getSizeFrac();
	
	/**
	 * Gets the upper left xCoord % relative to width
	 * @return The upper left xCoord % relative to width
	 */
	public double getXFrac();
	
	/**
	 * Gets the upper left yCoord % relative to width
	 * @return The upper left yCoord % relative to width
	 */
	public double getYFrac();
	
	/**
	 * Gets the HitObject pixel
	 * @return HitObject size in pixels
	 */
	public double getSize();
	
	/**
	 * Gets the upper left xCoord pixel
	 * @return The upper left xCoord pixel
	 */
	public double getX();
	
	/**
	 * Gets the upper left yCoord pixel
	 * @return The upper left yCoord pixel
	 */
	public double getY();
}
