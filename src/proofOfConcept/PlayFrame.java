package proofOfConcept;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PlayFrame extends JFrame {
	public PlayFrame(MediaPlayer mediaPlayer) {
		dataIndex = 0;
		currentlyDisplayed = new ArrayList<>();
		data = new ArrayList<>();
		results = new ArrayList<>();
		ar = 2000;
		perfect = 50;
		good = 200;
		after = good;
		
		final Dimension windowSize = new Dimension(getBounds().width, getBounds().height);
		
		data.add(new CircleHO("circle", new Duration(1000), 0.1, 0.1, 0.1, windowSize));
		data.add(new CircleHO("circle", new Duration(2000), 0.1, 0.3, 0.3, windowSize));
		data.add(new CircleHO("circle", new Duration(3000), 0.1, 0.3, 0.5, windowSize));
		data.add(new CircleHO("circle", new Duration(4000), 0.1, 0.5, 0.3, windowSize));
		
		hi = new HitIcon(currentlyDisplayed, getSize()); //icon for window size
		JLabel playWindow = new JLabel(hi);
		final int DELAY = 16; //update every 16ms [60hz]
		t = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Duration currentTime = mediaPlayer.getStartTime(); //bs define
				try { //dirty but works
					currentTime = mediaPlayer.getCurrentTime();
				} catch (NullPointerException x) {
					int[] sorted = new int[3];
					int total = 0;
					for (int i : results) {
						sorted[i]++;
						total += i;
					}
					double accuracy = 100.0 * total / results.size() / 2;
					String displayResult = "     Accuracy: " + accuracy + "% | Perfects: " + sorted[2] + " | Goods: " + sorted[1] + " | Misses: " + sorted[0] + "     ";
					dispose();
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
				if (dataIndex < data.size()) { //move hitobject to currentlydisplayed
					Duration checkNext = data.get(dataIndex).getHitTime().subtract(new Duration(ar));
					if (checkNext.compareTo(currentTime) <= 0) {
						currentlyDisplayed.add(data.get(dataIndex));
						dataIndex++;
					}
				}
				if (currentlyDisplayed.size() > 0) { //check if objects need to be removed & update approach circle
					for (HitObject o : currentlyDisplayed) {
						Duration oHitTime = o.getHitTime();
						double timeLeft = oHitTime.toMillis() - currentTime.toMillis();
						Duration appearTime = o.getHitTime().subtract(new Duration(ar));
						double totalAppearTime = oHitTime.toMillis() - appearTime.toMillis() - after;
						double frac = 1 + (timeLeft / totalAppearTime);
						if (frac >= 1) o.setARFraction(frac);
					}
					Duration checkExpired = currentlyDisplayed.get(0).getHitTime().add(new Duration(after)); //Time after persistence
					if (checkExpired.compareTo(currentTime) <= 0) {
						currentlyDisplayed.remove(0);
						results.add(0);
					}
				}

				hi.update(currentlyDisplayed);
				playWindow.repaint();
			}
		});
		
		playWindow.addMouseListener(new MouseAdapter() { //User selecting circles
			public void mousePressed(MouseEvent e) {
				Duration playerHitTime = mediaPlayer.getCurrentTime();
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

		addComponentListener(new ComponentAdapter() { //Window resize listener
			public void componentResized(ComponentEvent e) {
				Dimension temp = new Dimension(getBounds().width, getBounds().height);
				windowSize.setSize(temp);
				hi.updateDimension(temp);
			}
		});
		
		setLayout(new BorderLayout());
		add(playWindow, BorderLayout.CENTER);
		t.start();
		
		this.setPreferredSize(new Dimension(1280, 720));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //what does | do?
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Checks if there is an existing .mp3
	 * @param checkMap The map name
	 * @return File with mp3.  null if not found
	 */
	private File findMedia(String checkMap) {
		String currentDir = new File("").getAbsolutePath(); //Get current directory
		File folder = new File(currentDir); //Create File Object in current directory
		File[] listOfFiles = folder.listFiles(); //Get list of files in current directory
		for (File f : listOfFiles) { //Check if there is .mp3 with matching name
			String fullFileName = f.getName();
			if (fullFileName.split("\\.").length >= 2) {
				String fileName = fullFileName.split("\\.")[0];
				String ext = fullFileName.split("\\.")[1];
				if (fileName.equals(checkMap) && ext.equals("mp3")) return f;
			}
		}
		return null;
	}
	
	ArrayList<Integer> results;
	ArrayList<HitObject> currentlyDisplayed;
	ArrayList<HitObject> data;
	int dataIndex;
	HitIcon hi;
	double after;
	private double ar;
	private double perfect;
	private double good;
	private final Timer t;
}
