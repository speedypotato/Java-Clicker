package r3cleanup;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.Timer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * EditorPanel for EditorFrame
 * @author Nicholas Gadjali
 */
public class EditorPanel extends JComponent implements Runnable {
	/**
	 * Creates a new EditorPanel
	 * @param name The song name in /maps/
	 */
	public EditorPanel(String name) {
		this.songFile = new File(new File("").getAbsolutePath() + "\\maps\\" + name + ".wav");
		this.playing = false;
		this.hitObjects = new ArrayList<>();
		this.currentlyDisplayed = new ArrayList<>();
		
		addMouseListener(new MouseAdapter() { //repaint when mouse clicks
			public void mousePressed(MouseEvent e) {
				double xPerc = (double)e.getX() / (double)getWidth();
				double yPerc = (double)e.getY() / (double)getHeight();
				CircleHO temp = new CircleHO(mp.getCurrentTime(), size, xPerc, yPerc);
				hitObjects.add(temp);
				currentlyDisplayed.add(temp);
				repaint();
			}
		});
		
		this.t = new Timer(EditorFrame.REFRESH_RATE, (e) -> checkModel());
	}
	
	/**
	 * Checks if new hit objects need to be added or removed to "currently drawn" and calls repaint
	 */
	private void checkModel() {
		Duration currentTime = mp.getCurrentTime();
		if (currentlyDisplayed.size() > 0) { //check if objects need to be removed & update approach circle
			Duration checkExpired = currentlyDisplayed.get(0).getHitTime().add(new Duration(persistence)); //Time after persistence
			if (checkExpired.compareTo(currentTime) <= 0) {
				currentlyDisplayed.remove(0);
				repaint();
			}
		}
	}
	
	/**
	 * Paints the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		for (HitObject ho : currentlyDisplayed) ho.draw(g2, new Dimension(getBounds().width, getBounds().height));
	}
	
	/**
	 * Pause/Play music
	 */
	public void playPause() {
		if (playing) {
			playing = false;
			mp.pause();
			t.stop();
		} else {
			playing = true;
			mp.play();
			t.start();
		}
	}
	
	/**
	 * Music playing thread
	 */
	@Override
	public void run() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mp = new MediaPlayer(new Media(songFile.toURI().toString()));
				mp.play();
				mp.pause();
				t.start();
			}
		});
	}
	
	/**
	 * Get current time in milliseconds(used for jprogressbar)
	 * @return current time in ms
	 */
	public double getCurrentTime() {
		if (mp == null) return 0;
		return mp.getCurrentTime().toMillis();
	}
	
	/**
	 * Returns a copy of the stored hitObjects
	 * @return a copy of the stored hitObjects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HitObject> getHitObjects() { return (ArrayList<HitObject>)hitObjects.clone(); }
	
	/**
	 * Sets the circle size
	 * @param size The circle size decimal (relative to the width of the panel)
	 */
	public void setCircleSize(double size) { this.size = size; }
	
	/**
	 * Gets the circle size
	 * @return The circle size decimal (relative to the width of the panel)
	 */
	public double getCircleSize() { return size; }
	
	/**
	 * Sets the approach circle time
	 * @param ar The amount of time for the approach circle to reach the hit circle in ms
	 */
	public void setApproachTime(double ar) { this.approachTime = ar; }
	
	/**
	 * Gets the approach circle time
	 * @returnThe amount of time for the approach circle to reach the hit circle in ms
	 */
	public double getApproachTime() { return approachTime; }
	
	/**
	 * Sets the persistence after hit-time
	 * @param p The persistence after hit-time in ms
	 */
	public void setPersistence(double p) { this.persistence = p; }
	
	/**
	 * Gets the persistence after hit-time
	 * @return The persistence after hit-time in ms
	 */
	public double getPersistence() { return persistence; }

	private final File songFile;
	
	private double size;
	private double approachTime;
	private double persistence;
	
	private boolean playing;
	private MediaPlayer mp;
	private Timer t;
	
	private ArrayList<HitObject> hitObjects;
	private ArrayList<HitObject> currentlyDisplayed;
}
