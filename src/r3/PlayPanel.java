package r3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayPanel extends JComponent implements Runnable {
	
	public PlayPanel(String name) {
		this.mapName = name;
		this.songFile = new File(new File("").getAbsolutePath() + "\\maps\\" + name + ".wav");
		this.songLength = getSongLength(songFile);
		this.hitObjects = new ArrayList<>();
		this.currentlyDisplayed = new ArrayList<>();
		this.results = new ArrayList<>();
		this.dataIndex = 0;
		this.t = new Timer(PlayFrame.REFRESH_RATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Duration currentTime = mp.getCurrentTime();
				
				if (dataIndex < hitObjects.size()) { //move hitobject to currentlydisplayed
					Duration checkNext = hitObjects.get(dataIndex).getHitTime().subtract(new Duration(approachTime));
					if (checkNext.compareTo(currentTime) <= 0) {
						currentlyDisplayed.add(hitObjects.get(dataIndex));
						dataIndex++;
					}
				}
				if (currentlyDisplayed.size() > 0) { //check if objects need to be removed & update approach circle
					for (HitObject o : currentlyDisplayed) {
						Duration oHitTime = o.getHitTime();
						double timeLeft = oHitTime.toMillis() - currentTime.toMillis();
						Duration appearTime = o.getHitTime().subtract(new Duration(approachTime));
						double totalAppearTime = oHitTime.toMillis() - appearTime.toMillis() - persistence;
						double frac = 1 + (timeLeft / totalAppearTime);
						if (frac >= 1) o.setARFraction(frac);
					}
					Duration checkExpired = currentlyDisplayed.get(0).getHitTime().add(new Duration(persistence)); //Time after persistence
					if (checkExpired.compareTo(currentTime) <= 0) {
						currentlyDisplayed.remove(0);
						results.add(0);
					}
				}
				repaint();
			}
		});
		
		addMouseListener(new MouseAdapter() { //User selecting circles
			public void mousePressed(MouseEvent e) {
				Duration playerHitTime = mp.getCurrentTime();
				Point2D.Double mousePoint = new Point2D.Double(e.getX(), e.getY());
				if (currentlyDisplayed.size() <= 0) return; //extraneous user input
				HitObject hit = currentlyDisplayed.get(0);
				double radius = hit.getSize() / 2;
				Point2D.Double hitCenter = new Point2D.Double(hit.getX() + radius, hit.getY() + radius);
				double distance = hitCenter.distance(mousePoint);
				if (distance <= radius) {
					double pHitTime = playerHitTime.toMillis();
					double oHitTime = hit.getHitTime().toMillis();
					double difference = Math.abs(pHitTime - oHitTime);
					if (difference <= perfect) results.add(2); //score
					else if (difference <= good) results.add(1);
					else results.add(0);
					currentlyDisplayed.remove(0);
				}
			}
		});
	}
	
	public void displayResults() {
		int[] sorted = new int[3];
		int total = 0;
		for (int i : results) {
			sorted[i]++;
			total += i;
		}
		double accuracy = 100.0 * total / results.size() / 2;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		String displayResult = "     Accuracy: " + df.format(accuracy) + "% | Perfects: " + sorted[2] + " | Goods: " + sorted[1] + " | Misses: " + sorted[0] + "     ";
		t.stop();
		
		JLabel resultLabel = new JLabel(displayResult);
		JButton okButton = new JButton("OK");
		okButton.addActionListener((ActionEvent e) -> System.exit(0));
		JFrame endDialog = new JFrame("Results:");
		endDialog.setLayout(new BorderLayout());
		endDialog.add(resultLabel, BorderLayout.CENTER);
		endDialog.add(okButton, BorderLayout.SOUTH);
		
		endDialog.setLocationRelativeTo(null);
		endDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		endDialog.pack();
		endDialog.setVisible(true);
	}
	
	/**
	 * Updates the data structure
	 * @param hitObjects
	 */
	public void updateMap(ArrayList<HitObject> hitObjects) {
		this.hitObjects = hitObjects;
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
	
	@Override
	public void run() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mp = new MediaPlayer(new Media(songFile.toURI().toString()));
				
				mp.setOnEndOfMedia(() -> displayResults());
				
				mp.play();
				t.start();
			}
		});
	}
	
	public void setApproachTime(double approachTime) { this.approachTime = approachTime; }
	public void setPersistence(double persistence) { this.persistence = persistence; }
	public void setPerfect(double perfect) { this.perfect = perfect; }
	public void setGood(double good) { this.good = good; }

	private String mapName;
	private File songFile;
	private double songLength;
	
	private double approachTime;
	private double persistence;
	private double perfect;
	private double good;
	
	private int dataIndex;
	private ArrayList<Integer> results;
	private ArrayList<HitObject> hitObjects;
	private ArrayList<HitObject> currentlyDisplayed;
	private MediaPlayer mp;
	private Timer t;
}
