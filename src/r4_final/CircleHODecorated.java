package r4_final;

import java.awt.Color;
import java.io.Serializable;

/**
 * Decorator class for hit objects
 * @author Nicholas Gadjali
 */
public class CircleHODecorated extends HODecorator {

	/**
	 * Creates a CircleHODecorated object
	 * @param ho The hitobject to decorate
	 */
	public CircleHODecorated(HitObject ho) {
		super(ho);
	}

	/**
	 * Sets the color to a random color
	 * @param c unused
	 */
	@Override
	public void setColor(Color c) {
		ho.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
	}
}
