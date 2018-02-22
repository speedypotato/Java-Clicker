package r3;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.Timer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class EditorPanel extends JComponent implements Runnable {
	/**
	 * Creates a new EditorPanel
	 * @param name The song name in /maps/
	 */
	public EditorPanel(String name) {
		this.mapName = name;
		this.songFile = new File(new File("").getAbsolutePath() + "\\maps\\" + name + ".wav");
		this.songLength = getSongLength(songFile);
		this.playing = false;
		this.hitObjects = new ArrayList<>();
		this.index = 0;
		this.currentlyDisplayed = new ArrayList<>();
		
		this.t = new Timer(EditorFrame.REFRESH_RATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) { //update data structure
				checkModel();
			}
		});
		
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
	 * Checks if new hit objects need to be added or removed to "currently drawn"
	 */
	private void checkModel() {
		Duration currentTime = mp.getCurrentTime();
		/*if (index < hitObjects.size()) { //move hitobject to currentlydisplayed
			Duration checkNext = hitObjects.get(index).getHitTime().subtract(new Duration(approachTime));
			if (checkNext.compareTo(currentTime) <= 0) {
				currentlyDisplayed.add(hitObjects.get(index));
				index++;
			}
		}*/ //todo if seek is implemented
		if (currentlyDisplayed.size() > 0) { //check if objects need to be removed & update approach circle
			Duration checkExpired = currentlyDisplayed.get(0).getHitTime().add(new Duration(persistence)); //Time after persistence
			if (checkExpired.compareTo(currentTime) <= 0) { currentlyDisplayed.remove(0); }
		}
	}
	
	/**
	 * Seek to a new location and redraw
	 */
	private void seek() {
		//todo
	}
	
	/**
	 * Handles music playing thread
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
	 * Gets the length of the song in milliseconds
	 * @param f The song File
	 * @return Length of song in millis
	 */
	private double getSongLength(File f) {
		double length = -1;
		try {
			//Get song length
			AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(ais);
			length = c.getMicrosecondLength() * 0.001; //to millis
			c.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
		return length;
	}
	
	/**
	 * Returns a copy of the stored hitObjects
	 * @return
	 */
	public ArrayList<HitObject> getHitObjects() {
		return (ArrayList<HitObject>)hitObjects.clone();
	}
	
	public void setCircleSize(double size) { this.size = size; }
	public double getCircleSize() { return size; }
	public void setApproachTime(double ar) { this.approachTime = ar; }
	public double getApproachTime() { return approachTime; }
	public void setPersistence(double p) { this.persistence = p; }
	public double getPersistence() { return persistence; }

	private String mapName;
	private final File songFile;
	private final double songLength;
	
	private double size;
	private double approachTime;
	private double persistence;
	
	private boolean playing;
	private MediaPlayer mp;
	private Timer t;
	
	private ArrayList<HitObject> hitObjects;
	private int index;
	private ArrayList<HitObject> currentlyDisplayed;
}
