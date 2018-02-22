import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.util.Duration;

/**
 * Model for the selected game
 * @author Nicholas Gadjali
 *
 */
public class GameModel {
	/**
	 * Simple GameModel constructor
	 */
	public GameModel() {
	}
	
	/**
	 * Constructor with predefined file
	 * @param s The fileName without its extension
	 */
	public GameModel(String s, Dimension d) {
		this.fileName = s; //Save map name
		this.hitObjects = new ArrayList<>();
		this.listeners = new ArrayList<>();
		this.results = new ArrayList<>();
		readConfig();
		readHitObjects(d);
	}
	
	/**
	 * Constructor intended for editor use
	 * @param mapName The filename
	 * @param ar The approach rate
	 * @param after The persistence after hit-time
	 * @param perfect The leniency allowed for perfect
	 * @param good The leniency allowed for good
	 */
	public boolean editorGameModel(String mapName, double ar, double after, double perfect, double good) {
		this.fileName = mapName;
		this.approachR = ar;
		this.after = after;
		this.perfect = perfect;
		this.good = good;
		hitObjects = new ArrayList<>();
		return true;
	}
	
	/**
	 * Attaches a ChangeListener to the model
	 * @param cl The ChangeListener to attach
	 */
	public void attach(ChangeListener cl) {
		listeners.add(cl);
	}
	
	/**
	 * Sets the fileName
	 * @param s The fileName without its extension
	 * @return If successful
	 */
	public boolean setFileName(String s, Dimension d) {
		this.fileName = s; //Save map name
		this.hitObjects = new ArrayList<>();
		this.listeners = new ArrayList<>();
		this.results = new ArrayList<>();
		readConfig();
		readHitObjects(d);
		return true;
	}
	
	/**
	 * Gets a copy of the hit objects
	 * @return A copy of the hit objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HitObject> getHitObjects() {
		if (hitObjects.size() == 0) return null;
		return (ArrayList<HitObject>)hitObjects.clone();
	}
	
	/**
	 * Updates the GameModel with new input
	 * @param d The user hit-time for the HitObject at the same index
	 * @return If successful
	 */
	public boolean update(Duration d) {
		results.add(d);
		for (ChangeListener cl : listeners) cl.stateChanged(new ChangeEvent(this));
		return true;
	}
	
	/**
	 * Reads first line configuration of .map
	 * @return If successful
	 */
	private boolean readConfig() {
		String fileDir = new File("").getAbsolutePath() + "\\" + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileDir));
			String firstLine = br.readLine();
			if (firstLine.split(",").length != 4) System.out.println("Map Configuration Error");
			int index = 0;
			for (String s : firstLine.split(",")) {
				double temp = Double.parseDouble(s);
				if (index == 0) approachR = temp;
				else if (index == 1) after = temp;
				else if (index == 2) perfect = temp;
				else good = temp;
				index++;
			}
			br.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}
		return true;
	}
	
	/**
	 * Reads the line of hit objects
	 * @return If successful
	 */
	private boolean readHitObjects(Dimension d) {
		String fileDir = new File("").getAbsolutePath() + "\\" + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileDir));
			while (br.readLine() != null) {
				String[] ho = br.readLine().split(",");
				if (ho.length != 5) System.out.println("Hit Object Error: " + ho.toString());
				String type = ho[0];
				if (type.equals("circleHO")) { //If Circle Hit Object
					double hitTimeMillis = Double.parseDouble(ho[1]);
					Duration time = new Duration(hitTimeMillis);
					double size = Double.parseDouble(ho[2]);
					double xPos = Double.parseDouble(ho[3]);
					double yPos = Double.parseDouble(ho[4]);
					hitObjects.add(new CircleHO(type, time, size, xPos, yPos, d)); //Add hit object to list
				} //else if ____
			}
			br.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}
		return true;
	}
	
	private char mode;
	private String fileName;
	private double approachR;
	private double after;
	private double perfect;
	private double good;
	private ArrayList<HitObject> hitObjects;
	private ArrayList<ChangeListener> listeners;
	private ArrayList<Duration> results;
}
