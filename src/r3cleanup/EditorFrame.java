package r3cleanup;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Editor Frame Window
 * @author Nicholas Gadjali
 */
public class EditorFrame extends JFrame {
	private static final String DEFAULT_CIRCLE_RATIO = "0.08";
	private static final String DEFAULT_APPROACH = "2000";
	private static final String DEFAULT_PERFECT = "100";
	private static final String DEFAULT_GOOD = "300";
	public static final int REFRESH_RATE = 6; // 1/60hz ~= 16ms; 1/144hz ~= 6.9ms
	
	/**
	 * Start by calling the starting frame
	 */
	public EditorFrame() {
		new StartEditorFrame();
	}
	
	/**
	 * When map is valid, run the actual editor
	 * @param name the map name
	 */
	private EditorFrame(String name) {
		this.mapName = name;
		setTitle("Now Editing: " + name);
		setLayout(new BorderLayout());
		
		this.ep = new EditorPanel(name);
		SwingUtilities.invokeLater(ep); //start javafx audio stuff
		
		Container topContainer = new Container(); //Top container has settings
		topContainer.setLayout(new BorderLayout());
			
			Container circleSizeOptionContainer = new Container(); //Horizontally align label and textfield
			circleSizeOptionContainer.setLayout(new BoxLayout(circleSizeOptionContainer, BoxLayout.X_AXIS));
			JLabel sizeLabel = new JLabel("Circle Size: ");
			JTextField sizeTextField = new JTextField(DEFAULT_CIRCLE_RATIO, 10);
			ep.setCircleSize(Double.parseDouble(sizeTextField.getText()));
			sizeTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
				@Override
				public void focusLost(FocusEvent e) { //update circle size
					JTextField f = (JTextField)e.getSource();
					if (f.getText().isEmpty()) f.setText(ep.getCircleSize() + "");
					ep.setCircleSize(Double.parseDouble(f.getText()));
				}
			});
			circleSizeOptionContainer.add(sizeLabel);
			circleSizeOptionContainer.add(sizeTextField);
		
			Container generalSettingsContainer = new Container(); //Vertically align general settings
			generalSettingsContainer.setLayout(new BoxLayout(generalSettingsContainer, BoxLayout.Y_AXIS));
			
				Container arContainer = new Container(); //approach entry box
				arContainer.setLayout(new BoxLayout(arContainer, BoxLayout.X_AXIS));
				JTextField arTextField = new JTextField(DEFAULT_APPROACH);
				ep.setApproachTime(Double.parseDouble(arTextField.getText()));
				arTextField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						//unused
					}
					@Override
					public void focusLost(FocusEvent e) { //update approach circle time
						JTextField f = (JTextField)e.getSource();
						if (f.getText().isEmpty()) f.setText(ep.getApproachTime() + "");
						ep.setApproachTime(Double.parseDouble(f.getText()));
					}
				});
				arContainer.add(new JLabel("Approach Time(ms): "));
				arContainer.add(arTextField);
				
				Container aContainer = new Container(); //persistence entry box
				aContainer.setLayout(new BoxLayout(aContainer, BoxLayout.X_AXIS));
				JTextField aTextField = new JTextField(DEFAULT_GOOD);
				ep.setPersistence(Double.parseDouble(aTextField.getText()));
				aTextField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						//unused
					}
					@Override
					public void focusLost(FocusEvent e) { //update persistence
						JTextField f = (JTextField)e.getSource();
						if (f.getText().isEmpty()) f.setText(ep.getPersistence() + "");
						ep.setPersistence(Double.parseDouble(f.getText()));
					}
				});
				aContainer.add(new JLabel("After Persistence(ms): "));
				aContainer.add(aTextField);
				
				Container pContainer = new Container(); //perfect entry box
				pContainer.setLayout(new BoxLayout(pContainer, BoxLayout.X_AXIS));
				JTextField pTextField = new JTextField(DEFAULT_PERFECT);
				perfect = Double.parseDouble(pTextField.getText());
				pTextField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						//unused
					}
					@Override
					public void focusLost(FocusEvent e) { //update perfect leniency
						JTextField f = (JTextField)e.getSource();
						if (f.getText().isEmpty()) f.setText(perfect + "");
						perfect = Double.parseDouble(f.getText());
					}
				});
				pContainer.add(new JLabel("Perfect Leniency(ms): "));
				pContainer.add(pTextField);
				
				Container gContainer = new Container(); //good entry box
				gContainer.setLayout(new BoxLayout(gContainer, BoxLayout.X_AXIS));
				JTextField gTextField = new JTextField(DEFAULT_GOOD);
				good = Double.parseDouble(gTextField.getText());
				pTextField.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
						//unused
					}
					@Override
					public void focusLost(FocusEvent e) { //update good leniency
						JTextField f = (JTextField)e.getSource();
						if (f.getText().isEmpty()) f.setText(good + "");
						good = Double.parseDouble(f.getText());
					}
				});
				gContainer.add(new JLabel("Good Leniency(ms): "));
				gContainer.add(gTextField);
				generalSettingsContainer.add(arContainer);
				generalSettingsContainer.add(pContainer);
				generalSettingsContainer.add(gContainer);
				generalSettingsContainer.add(aContainer);
			
			JButton saveButton = new JButton("Save Map");  //save map to file
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean success = saveMap();
					if (success) JOptionPane.showMessageDialog(new JFrame(), "BeatMap Saved.");
					else JOptionPane.showMessageDialog(new JFrame(), "Oh no!  Error saving beatmap.");
				}
			});
		
			topContainer.add(circleSizeOptionContainer, BorderLayout.WEST);
			topContainer.add(generalSettingsContainer, BorderLayout.CENTER);
			topContainer.add(saveButton, BorderLayout.EAST);
			
		Container controls = new Container(); //add play/pause and progress bar
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		
			JButton playPause = new JButton("Play/Pause");
			playPause.addActionListener((e) -> ep.playPause());
			
			JProgressBar pb = new JProgressBar(0, (int)getSongLength(findMedia(name)));
			Timer pbt = new Timer(REFRESH_RATE, (e) -> pb.setValue((int)ep.getCurrentTime()));
			pbt.addActionListener((e) -> repaint());
			pbt.start();
			
			controls.add(playPause);
			controls.add(pb);
			
		add(topContainer, BorderLayout.NORTH); //add container with various settings
		add(ep, BorderLayout.CENTER); //add panel
		add(controls, BorderLayout.SOUTH); //add controls
		
		this.setPreferredSize(new Dimension(1280, 720)); //720p
		setMinimumSize(new Dimension(500, 200));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //what does | do?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Saves the map into a data file
	 * @return If map saving was successful
	 */
	public boolean saveMap() {
		ArrayList<HitObject> hitObjects = ep.getHitObjects();
		if (hitObjects == null) return false;
		String filePath = new File("").getAbsolutePath() + "\\maps\\" + mapName + ".map"; //Get current directory + filename to save to
		File f = new File(filePath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			String bmConfig = ep.getApproachTime() + "," + ep.getPersistence() + "," + perfect + "," + good;
			bw.write(bmConfig);
			for (HitObject o : hitObjects) {
				bw.newLine();
				String s = o.getHitTime().toMillis() + "," + o.getSizeFrac() + "," + o.getXFrac() + "," + o.getYFrac();
				bw.write(s);
			}
			bw.close();
			return true;
		} catch (IOException x) {
			System.out.println(x.getMessage());
			return false;
		}
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
	 * Checks if there is an existing .wav
	 * @param checkMap The map name
	 * @return File with wav.  null if not found
	 */
	private File findMedia(String checkMap) {
		String currentDir = new File("").getAbsolutePath(); //Get current directory
		File folder = new File(currentDir + "\\maps"); //Create File Object in current directory
		File[] listOfFiles = folder.listFiles(); //Get list of files in current directory
		for (File f : listOfFiles) { //Check if there is .mp3 with matching name
			String fullFileName = f.getName();
			if (fullFileName.split("\\.").length >= 2) {
				String fileName = fullFileName.split("\\.")[0];
				String ext = fullFileName.split("\\.")[1];
				if (fileName.equals(checkMap) && ext.equals("wav")) return f;
			}
		}
		return null;
	}
	
	/**
	 * Private subclass for initial window where user types map name
	 * @author Nicholas Gadjali
	 */
	private class StartEditorFrame extends JFrame {
		/**
		 * Creates a new StartEditorFrame
		 */
		public StartEditorFrame() {
			setTitle("Welcome to the BeatMap Editor!");
			setLayout(new BorderLayout());
			
			JLabel prompt = new JLabel("BeatMap Name: "); //Prompt WEST
			
			JTextField input = new JTextField(); //Input field CENTER
			input.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) { //Update data when focus is lost
					JTextField f = (JTextField)e.getSource();
					tempMapName = f.getText();
				}
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
			});
			
			JLabel result = new JLabel(); //Checking NORTH
			
			JButton okButton = new JButton("OK"); //Button SOUTH
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (findMedia(tempMapName) != null) {
						new EditorFrame(tempMapName);
						dispose();
					} else {
						result.setText("No matching .wav!");
					}
				}
			});
			
			add(prompt, BorderLayout.WEST);
			add(input, BorderLayout.CENTER);
			add(result, BorderLayout.NORTH);
			add(okButton, BorderLayout.SOUTH);

			setLocationRelativeTo(null);
			setMinimumSize(new Dimension(300, 150));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setVisible(true);
		}
		
		private String tempMapName;
	}

	private String mapName;
	private EditorPanel ep;
	private double perfect;
	private double good;
}
