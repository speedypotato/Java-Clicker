package proofOfConcept;
import java.awt.Graphics2D;

import javafx.util.Duration;

/**
 * Hit Object Model Interface
 * @author Nicholas Gadjali
 *
 */
public interface HitObject {
	/**
	 * Gets the type of HitObject
	 * @return String representation of the type
	 */
	public String getType();
	
	/**
	 * Gets the hit-time of HitObject
	 * @return hit-time in Duration
	 */
	public Duration getHitTime();
	
	/**
	 * Gets the HitObject
	 * @return HitObject size
	 */
	public double getSize();
	
	/**
	 * Gets the upper left xCoord
	 * @return The upper left xCoord 
	 */
	public double getX();
	
	/**
	 * Gets the upper left yCoord
	 * @return The upper left yCoord
	 */
	public double getY();
	
	/**
	 * Draws the hitObject
	 * @param g2 The Graphics2D
	 */
	public void draw(Graphics2D g2);
	
	/**
	 * Sets approach circle size
	 * @param ar
	 */
	public void setARFraction(double ar);
	
	public String toString();
}
