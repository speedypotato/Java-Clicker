package r4_final;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javafx.util.Duration;

/**
 * General HitObject Decorator Class
 * @author Nicholas Gadjali
 */
public class HODecorator implements HitObject {
	public HODecorator(HitObject ho) {
		this.ho = ho;
	}

	@Override
	public void draw(Graphics2D g, Dimension windowDimension) {
		this.ho.draw(g, windowDimension);
	}

	@Override
	public Duration getHitTime() {
		return this.ho.getHitTime();
	}

	@Override
	public void setARFraction(double ar) {
		this.ho.setARFraction(ar);
	}

	@Override
	public double getSizeFrac() {
		return this.ho.getSizeFrac();
	}

	@Override
	public double getXFrac() {
		return this.ho.getXFrac();
	}

	@Override
	public double getYFrac() {
		return this.ho.getYFrac();
	}

	@Override
	public double getSize() {
		return this.ho.getSize();
	}

	@Override
	public double getX() {
		return this.ho.getX();
	}

	@Override
	public double getY() {
		return this.ho.getY();
	}

	@Override
	public void setColor(Color c) {
		this.ho.setColor(c);
	}
	
	@Override
	public Color getColor() {
		return this.ho.getColor();
	}
	
	protected HitObject ho;
}
