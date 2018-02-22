package r4_final;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.Serializable;

import javafx.util.Duration;

/**
 * HitObject Interface
 * @author Nicholas Gadjali
 */
public interface HitObject {
	/**
	 * Draw the HitObject at its relative position
	 * @param g Graphics2D
	 * @param windowDimension The window dimension to use for drawing
	 */
	public void draw(Graphics2D g, Dimension windowDimension);
	
	/**
	 * Gets the HitObject color
	 * @return HitObject color
	 */
	public Color getColor();
	
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
	
	/**
	 * Sets the color for drawing
	 * @param c The new color
	 */
	public void setColor(Color c);
}
