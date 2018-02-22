package r4_final;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
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

/**
 * Play Panel class for Play Frame
 * @author Nicholas Gadjali
 */
public class PlayPanel extends JComponent implements Runnable {
	/**
	 * Creates a new PlayPanel
	 * @param name Map name
	 */
	public PlayPanel(String name) {
		this.songFile = new File(new File("").getAbsolutePath() + "\\maps\\" + name + ".wav");
		this.hitObjects = new ArrayList<>();
		this.currentlyDisplayed = new ArrayList<>();
		this.results = new ArrayList<>();
		this.dataIndex = 0;
		this.t = new Timer(PlayFrame.REFRESH_RATE, new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				Duration currentTime = mp.getCurrentTime();
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
		
		this.producer = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				while (dataIndex < hitObjects.size()) {
					Duration currentTime = mp.getCurrentTime();
					if (dataIndex < hitObjects.size()) { //move hitobject to currentlydisplayed
						Duration checkNext = hitObjects.get(dataIndex).getHitTime().subtract(new Duration(approachTime));
						if (checkNext.compareTo(currentTime) <= 0) {
							currentlyDisplayed.add(hitObjects.get(dataIndex));
							dataIndex++;
						}
					}
					try {
						Thread.sleep(PlayFrame.REFRESH_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
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
	
	/**
	 * Display a dialogue box with end results
	 */
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
	 * @param hitObjects The ArrayList of hitObjects
	 */
	public void updateMap(ArrayList<HitObject> hitObjects) {
		this.hitObjects = hitObjects;
	}

	/**
	 * Paints the panel
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.gray); //background
		g2.fill(getBounds());
		
		ArrayList<HitObject> reversed = (ArrayList<HitObject>)currentlyDisplayed.clone(); //reverse things to draw for better aesthetic
		Collections.reverse(reversed);
		for (HitObject ho : reversed) ho.draw(g2, new Dimension(getBounds().width, getBounds().height));
	}
	
	/**
	 * Run javafx music
	 */
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
				
				producer.start();
			}
		});
	}
	
	/**
	 * Set approach time
	 * @param approachTime The approach time in ms
	 */
	public void setApproachTime(double approachTime) { this.approachTime = approachTime; }
	
	/**
	 * Set after hit-time persistence
	 * @param persistence The persistence in ms
	 */
	public void setPersistence(double persistence) { this.persistence = persistence; }
	
	/**
	 * Set perfect hit-time leniency
	 * @param perfect The perfect leniency in ms
	 */
	public void setPerfect(double perfect) { this.perfect = perfect; }
	
	/**
	 * Set good hit-time leniency
	 * @param good The good leniency in ms
	 */
	public void setGood(double good) { this.good = good; }


	/**
	 * Gets a direct pointer to the hitobjects
	 * @return hitobjects
	 */
	public ArrayList<HitObject> getDirectHitObjects() {
		return hitObjects;
	}
	
	/**
	 * Thread which handles adding items to currentlyDisplayed
	 */
	private Thread producer;
	
	/**
	 * The song file
	 */
	private File songFile;
	
	/**
	 * The set approach time(ms)
	 */
	private double approachTime;
	/**
	 * The set after-persistence time(ms)
	 */
	private double persistence;
	/**
	 * The set perfect leniency(ms)
	 */
	private double perfect;
	/**
	 * The set good leniency(ms)
	 */
	private double good;
	
	/**
	 * The current location in hitObjects
	 */
	private int dataIndex;
	/**
	 * The user results stored as misses, goods, and perfects
	 */
	private ArrayList<Integer> results;
	/**
	 * The map's hitObjects
	 */
	private ArrayList<HitObject> hitObjects;
	/**
	 * The currently displayed hitObjects
	 */
	private ArrayList<HitObject> currentlyDisplayed;
	/**
	 * The MediaPlayer for the media
	 */
	private MediaPlayer mp;
	/**
	 * The Timer for the animation and currentlyDisplayed removal
	 */
	private Timer t;
}
