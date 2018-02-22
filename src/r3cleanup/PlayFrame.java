package r3cleanup;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.util.Duration;

/**
 * Play Frame Window
 * @author Nicholas Gadjali
 */
public class PlayFrame extends JFrame {
	public static final int REFRESH_RATE = 6; // 1/60hz ~= 16ms; 1/144hz ~= 6.9ms
	
	/**
	 * Creates a new PlayFrame
	 * @param name Map name
	 */
	public PlayFrame(String name) {
		setTitle("Now Playing: " + name);
		this.mapName = name;
		this.p = new PlayPanel(name);
		this.p.updateMap(readMap());
		
		add(this.p);
		
		SwingUtilities.invokeLater(p); //start javafx audio stuff
		
		this.setPreferredSize(new Dimension(1280, 720));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //what does | do?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Reads map from file
	 */
	private ArrayList<HitObject> readMap() {
		ArrayList<HitObject> ho = new ArrayList<>();
		String filePath = new File("").getAbsolutePath() + "\\maps\\" + mapName + ".map"; //Get current directory + filename to save to
		File f = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String[] bmConfig = br.readLine().split(",");
			if (bmConfig.length != 4) System.out.println("Corrupt map config");
			p.setApproachTime(Double.parseDouble(bmConfig[0])); //[0]approach [1]persist [2]perfect [3]good
			p.setPersistence(Double.parseDouble(bmConfig[1]));
			p.setPerfect(Double.parseDouble(bmConfig[2]));
			p.setGood(Double.parseDouble(bmConfig[3]));
			
			String line;
			while ((line = br.readLine()) != null) { //keep reading lines and adding
				String[] hitObject = line.split(","); //Duration time, double size, double x, double y
				if (hitObject.length != 4) System.out.println("Corrupt hitObject config");
				ho.add(new CircleHO(new Duration(Double.parseDouble(hitObject[0])), Double.parseDouble(hitObject[1]), Double.parseDouble(hitObject[2]), Double.parseDouble(hitObject[3])));
			}
			br.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}
		return ho;
	}
	
	private PlayPanel p;
	private String mapName;
}
