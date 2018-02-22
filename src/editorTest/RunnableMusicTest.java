package editorTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class RunnableMusicTest extends JFrame implements Runnable {
	public static void main(String[] args) {
		//System.out.print("Filename? ");
		Runnable r = new RunnableMusicTest("alt.wav");//new Scanner(System.in).next());
		
		SwingUtilities.invokeLater(r);
	}
	
	public RunnableMusicTest(String s) {
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.mapName = s;
		this.b = new JButton("P");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (playing) {
					mp.pause();
					playing = false;
				}
				else {
					mp.play();
					playing = true;
				}
			}
		});
		
		double songLength = 1; //in milliseconds
		try {
			//Get song length
			File f = new File("D:\\Documents\\EclipseWorkspace\\cs151 final project\\javasu\\" + mapName);
			AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(ais);
			songLength = c.getMicrosecondLength() * 0.001;
			c.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
		
		this.pb = new JProgressBar(0, (int)songLength);
		pb.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int ms = (int)Math.round(((double)e.getX() / (double)pb.getWidth() * pb.getMaximum()));
				pb.setValue(ms);
				
				mp.seek(new Duration(ms));
			}
		});
		
		add(b);
		add(pb);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Handles playing music
	 */
	@Override
	public void run() {
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				File f = new File("D:\\Documents\\EclipseWorkspace\\cs151 final project\\javasu\\" + mapName);
				
				//Play Song
				mp = new MediaPlayer(new Media(f.toURI().toString()));
				mp.play();
				playing = true;
				
				t = new Timer(10, (e) -> pb.setValue((int)mp.getCurrentTime().toMillis()));
				t.start();
			}
		});
	}
	
	private JProgressBar pb;
	private boolean playing;
	private Timer t;
	private MediaPlayer mp;
	private JButton b;
	private String mapName;
}
