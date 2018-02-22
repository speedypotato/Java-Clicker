package r4_final;

import javafx.util.Duration;

/**
 * Factory design pattern for hit objects
 * @author Nicholas Gadjali
 */
public class HOFactory {
	/**
	 * Factory method
	 * @param data Hit object type and settings
	 * @return Proper hit object
	 */
	public static HitObject getHO(String[] data) {
		HitObject ho = null;
		switch (data[4]) {
		case "CircleHO":
			ho = new CircleHO(new Duration(Double.parseDouble(data[0])), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
			break;
		case "slider":
			//not implemented
			break;
		case "spinner":
			//not implemented
			break;
		}
		return ho;
	}
}
