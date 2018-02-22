import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Handles editor window view
 * @author Nicholas Gadjali
 */
public class EditorFrame extends JFrame implements ChangeListener {
	private static final String DEFAULT_CIRCLE_RATIO = "0.1";
	private static final String DEFAULT_APPROACH = "2000";
	private static final String DEFAULT_PERFECT = "50";
	private static final String DEFAULT_GOOD = "200";
	
	/**
	 * Simple Starting Constructor
	 */
	public EditorFrame(GameModel gm) {
		new StartEditorFrame(gm);
	}
	
	/**
	 * Returning Constructor
	 * @param s The verified filename
	 */
	public EditorFrame(String s, GameModel gm) {
		setTitle("Now Editing: " + s);
		this.mapName = s;
		setLayout(new BorderLayout());
		
		Container topContainer = new Container(); //Top container has settings
		topContainer.setLayout(new BorderLayout());
			
			Container typeOptionContainer = new Container(); //Vertically align radio buttons
			typeOptionContainer.setLayout(new BoxLayout(typeOptionContainer, BoxLayout.Y_AXIS));
			
			JRadioButton circleOption = new JRadioButton("Circle");
			//JRadioButton sliderOption = new JRadioButton("Slider");
			ButtonGroup typeOptions = new ButtonGroup(); //Groups buttons to ensure only 1 is selected
			typeOptions.add(circleOption);
			//typeOptions.add(sliderOption);
			typeOptionContainer.add(circleOption);
			
			Container circleSizeOptionContainer = new Container(); //Horizontally align label and textfield
			circleSizeOptionContainer.setLayout(new BoxLayout(circleSizeOptionContainer, BoxLayout.X_AXIS));
			JLabel sizeLabel = new JLabel("Obj Size: ");
			JTextField sizeTextField = new JTextField(DEFAULT_CIRCLE_RATIO, 10);
			size = Double.parseDouble(sizeTextField.getText());
			sizeTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
				@Override
				public void focusLost(FocusEvent e) { //update circle size
					JTextField f = (JTextField)e.getSource();
					if (f.getText().isEmpty()) f.setText(size + "");
					size = Double.parseDouble(f.getText());
				}
			});
			circleSizeOptionContainer.add(sizeLabel);
			circleSizeOptionContainer.add(sizeTextField);

			Container hitObjectOptionContainer = new Container(); //Vertically align options
			hitObjectOptionContainer.setLayout(new BoxLayout(hitObjectOptionContainer, BoxLayout.Y_AXIS));
			hitObjectOptionContainer.add(circleSizeOptionContainer);
			hitObjectOptionContainer.add(typeOptionContainer);
		
		Container generalSettingsContainer = new Container(); //Vertically align general settings
		generalSettingsContainer.setLayout(new BoxLayout(generalSettingsContainer, BoxLayout.Y_AXIS));
		
			Container arContainer = new Container();
			arContainer.setLayout(new BoxLayout(arContainer, BoxLayout.X_AXIS));
			JTextField arTextField = new JTextField(DEFAULT_APPROACH);
			ar = Double.parseDouble(arTextField.getText());
			arTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
				@Override
				public void focusLost(FocusEvent e) { //update approach circle time
					JTextField f = (JTextField)e.getSource();
					ar = Double.parseDouble(f.getText());
				}
			});
			arContainer.add(new JLabel("Approach Speed(ms): "));
			arContainer.add(arTextField);
			
			Container aContainer = new Container();
			aContainer.setLayout(new BoxLayout(aContainer, BoxLayout.X_AXIS));
			JTextField aTextField = new JTextField(DEFAULT_GOOD);
			after = Double.parseDouble(aTextField.getText());
			aTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
				@Override
				public void focusLost(FocusEvent e) { //update perfect leniency
					JTextField f = (JTextField)e.getSource();
					after = Double.parseDouble(f.getText());
				}
			});
			aContainer.add(new JLabel("After Persistence(ms): "));
			aContainer.add(aTextField);
			
			Container pContainer = new Container();
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
					perfect = Double.parseDouble(f.getText());
				}
			});
			pContainer.add(new JLabel("Perfect Leniency(ms): "));
			pContainer.add(pTextField);
			
			Container gContainer = new Container();
			gContainer.setLayout(new BoxLayout(gContainer, BoxLayout.X_AXIS));
			JTextField gTextField = new JTextField(DEFAULT_GOOD);
			good = Double.parseDouble(gTextField.getText());
			pTextField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					//unused
				}
				@Override
				public void focusLost(FocusEvent e) { //update perfect leniency
					JTextField f = (JTextField)e.getSource();
					good = Double.parseDouble(f.getText());
				}
			});
			gContainer.add(new JLabel("Good Leniency(ms): "));
			gContainer.add(gTextField);
			generalSettingsContainer.add(arContainer);
			generalSettingsContainer.add(aContainer);
			generalSettingsContainer.add(pContainer);
			generalSettingsContainer.add(gContainer);
			
			JButton saveButton = new JButton("Save Map");
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean success = saveMap(gm);
					if (success) JOptionPane.showMessageDialog(new JFrame(), "BeatMap Saved.");
					else JOptionPane.showMessageDialog(new JFrame(), "Oh no!  Error saving beatmap.");
				}
			});
		
		topContainer.add(hitObjectOptionContainer, BorderLayout.WEST);
		topContainer.add(generalSettingsContainer, BorderLayout.CENTER);
		topContainer.add(saveButton, BorderLayout.EAST);
		gm.editorGameModel(mapName, ar, after, perfect, good); //Fill in the default fields for the model
		
		/**
		//add editor
		playing = false;
		this.mediaFile = findMedia(mapName);
		MediaThread mt = new MediaThread(mediaFile); //Make mediathread for playback
		mt.show(this.mediaFile);
		songLengthMillis = 0;
		try {
			songLengthMillis = (int)mt.getSongLength();
		} catch (Exception e1) {
			e1.printStackTrace();
		} **/
		
		Container playbackContainer = new Container(); //jprogressbar/mouse listener here
		playbackContainer.setLayout(new BoxLayout(playbackContainer, BoxLayout.X_AXIS));
			
			JButton playPause = new JButton("P/P"); //pause/play toggle
			playPause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (playing) {
						playing = false;
						//mt.getMediaPlayer().pause();
					} else {
						playing = true;
						//mt.getMediaPlayer().play();
					}
				}
			});
			JProgressBar seek = new JProgressBar(0, songLengthMillis); //seek bar
			seek.addMouseListener(new MouseAdapter() { //allows click to seek
				public void mousePressed(MouseEvent e) {
					double barSize = seek.getSize().getWidth();
					double seekTo = e.getX();
					double percentage = seekTo / barSize;
					Duration seekTime = new Duration(songLengthMillis * percentage);
					//mt.getMediaPlayer().seek(seekTime);
				}
			});
			
		playbackContainer.add(playPause);
		playbackContainer.add(seek);
		
		add(topContainer, BorderLayout.NORTH);
		add(playbackContainer, BorderLayout.SOUTH);
		
		this.setPreferredSize(new Dimension(1280, 720));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH); //what does | do?
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
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
	 * Saves the map into a data file
	 * @return If map saving was successful
	 */
	private boolean saveMap(GameModel gm) {
		if (gm.getHitObjects() == null) return false;
		String filePath = new File("").getAbsolutePath() + mapName + ".map"; //Get current directory + filename to save to
		File f = new File(filePath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			String bmConfig = ar + "," + after + "," + perfect + "," + good;
			bw.write(bmConfig);
			for (HitObject o : gm.getHitObjects()) {
				bw.newLine();
				String s = o.getType() + "," + o.getHitTime() + "," + o.getSize() + "," + o.getX() + "," + o.getY();
				bw.write(s);
			}
			bw.close();
			return true;
		} catch (IOException x) {
			System.out.println(x.getMessage());
			return false;
		}
	}
	
	public File getMedia() {
		return mediaFile;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Private subclass for initial window
	 * @author Nicholas Gadjali
	 */
	private class StartEditorFrame extends JFrame {
		public StartEditorFrame(GameModel gm) {
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
						new EditorFrame(tempMapName, gm);
						dispose();
					} else {
						result.setText("No matching .mp3!");
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
	
	private File mediaFile;
	private String mapName;
	private double size;
	private double ar;
	private double after;
	private double perfect;
	private double good;
	private int songLengthMillis;
	private boolean playing;
}
