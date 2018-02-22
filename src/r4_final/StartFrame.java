package r4_final;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.junit.Test;

/**
 * Initial Window Prompt View/Listener
 * @author Nicholas Gadjali
 */
public class StartFrame extends JFrame {
	/**
	 * Creates a new Starting Frame
	 */
	@SuppressWarnings("unchecked")
	public StartFrame() {
		setTitle("Welcome!");
		setLayout(new BorderLayout());
		JLabel greeter = new JLabel("Choose a BeatMap:"); //North Greeter
		
		//reflection
		Method m = null;
		try {
			m = this.getClass().getDeclaredMethod("getListOfMaps", null);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		m.setAccessible(true);
		
		TreeSet<String> mapTreeSet = null;
		try {
			mapTreeSet = (TreeSet<String>) m.invoke(this, null); //Center List
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
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
			public void actionPerformed(ActionEvent ae) {
				sft = new TemplateEditor(); //Open Editor via Template method
				sft.launchWindow(currentlySelected);
				//dispose(); //Close StartFrame Window
			}
		});
		JButton playButton = new JButton("PLAY SONG");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (currentlySelected != null) {
					sft = new TemplatePlayer();
					sft.launchWindow(currentlySelected);
					//dispose(); //Close StartFrame Window
				}
			}
		});
		buttonContainer.add(editorButton, BorderLayout.WEST);
		buttonContainer.add(playButton, BorderLayout.EAST);
		
		add(greeter, BorderLayout.NORTH);
		add(scrollingMapList, BorderLayout.CENTER);
		add(buttonContainer, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(250, 100));
		setPreferredSize(new Dimension(250, 175));
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
				if (ext.equals("map") && index + 1 < listOfFiles.length) { //Check if map extension
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

	/**
	 * Test method for getListOfMaps
	 */
	@Test
	public void testListOfMaps() {
		StartFrame sf = new StartFrame();
		TreeSet<String> maps = sf.getListOfMaps();
		
		TreeSet<String> test = new TreeSet<>();
		test.add("shelterDemoHard");
		test.add("stayDemo");
		test.add("stayDemoMedium");
		assertEquals(test, maps);
		sf.dispose();
	}
	
	private String currentlySelected;
	private StartFrameTemplate sft;
}
