import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Initial Window Prompt View/Listener
 * @author Nicholas Gadjali
 *
 */
public class StartFrame extends JFrame {
	/**
	 * Creates a new Starting Frame
	 */
	public StartFrame(GameModel gm) {
		setTitle("Welcome!");
		setLayout(new BorderLayout());
		JLabel greeter = new JLabel("Choose a BeatMap:"); //North Greeter
		
		TreeSet<String> mapTreeSet = getListOfMaps(); //Center List
		String[] maps = mapTreeSet.toArray(new String[mapTreeSet.size()]); //Convert to Array
		JList<String> mapList = new JList<>(maps); //Convert to List
		mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Single Selection only
		mapList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent le) {
				int index = 0;
				index = mapList.getSelectedIndex();
				currentlySelected = maps[index];
			}
		});
		JScrollPane scrollingMapList = new JScrollPane(mapList);
		
		Container buttonContainer = new Container(); //South Buttons
		buttonContainer.setLayout(new BorderLayout()); //Horizontal List of Buttons
		JButton editorButton = new JButton("OPEN EDITOR");
		editorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditorFrame ef = new EditorFrame(gm); //Open Editor
				dispose(); //Close StartFrame Window
			}
		});
		JButton playButton = new JButton("PLAY SONG");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (currentlySelected != null) {
					PlayFrame pf = new PlayFrame(currentlySelected, gm); //Open PlayFrame
					dispose(); //Close StartFrame Window
				}
			}
		});
		buttonContainer.add(editorButton, BorderLayout.WEST);
		buttonContainer.add(playButton, BorderLayout.EAST);
		
		add(greeter, BorderLayout.NORTH);
		add(scrollingMapList, BorderLayout.CENTER);
		add(buttonContainer, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(300, 200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Gets a list of valid maps
	 * @return TreeSet of beatMaps
	 */
	private TreeSet<String> getListOfMaps() {
		TreeSet<String> maps = new TreeSet<>(); //ArrayList to return
		
		String currentDir = new File("").getAbsolutePath() + "\\maps"; //Get current directory
		File folder = new File(currentDir); //Create File Object in current directory
		File[] listOfFiles = folder.listFiles(); //Get list of files in current directory
		Arrays.sort(listOfFiles); //Ensure alphabetical order
		
		int index = 0;
		for (File f : listOfFiles) { //Check if .map has matching .wav
			String fullFileName = f.getName();
			if (fullFileName.split("\\.").length >= 2) {
				String fileName = fullFileName.split("\\.")[0];
				String ext = fullFileName.split("\\.")[1];
				if (ext.equals("map") && index + 2 < listOfFiles.length) { //Check if map extension
					index++;
					String checkMp3Ext = listOfFiles[index].getName().split("\\.")[1];
					if (checkMp3Ext.equals("wav")) { //WAV extension should be right after map
						String checkMp3FileName = listOfFiles[index].getName().split("\\.")[0];
						if (fileName.equals(checkMp3FileName)) maps.add(fileName); //Add to TreeSet if names match
					}
				} else {
					index++;
				}
			}
		}
		return maps;
	}
	
	private String currentlySelected;
}
