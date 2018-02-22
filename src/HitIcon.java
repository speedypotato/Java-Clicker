

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.Icon;

public class HitIcon implements Icon {

	public HitIcon(ArrayList<HitObject> ho, Dimension d) {
		this.displayObjects = ho;
		this.frameD = d;
	}
	@Override
	public int getIconHeight() {
		return (int)frameD.getHeight();
	}

	@Override
	public int getIconWidth() {
		return (int)frameD.getWidth();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D)g;
		for (HitObject ho : displayObjects) {
			ho.draw(g2);
		}
	}
	
	/**
	 * Updates new dimension
	 * @param d
	 * @return
	 */
	public boolean updateDimension(Dimension d) {
		this.frameD = d;
		return true;
	}
	
	/**
	 * Updates arraylist
	 * @param displayObjects
	 * @return
	 */
	public boolean update(ArrayList<HitObject> displayObjects) {
		this.displayObjects = displayObjects;
		return true;
	}
	
	ArrayList<HitObject> displayObjects;
	Dimension frameD;
}
