package editorTest;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MusicPlayer extends JFrame {
	public static void main(String[] args) throws Exception {
		File f = new File(new File("").getAbsolutePath() + "/easyTest.wav");
		System.out.println(f.toString());
		
		AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.start();
		
		MusicPlayer mp = new MusicPlayer(f);
	}
	
	public MusicPlayer() {
		frameLength = 0;
		JButton jb = new JButton("alive");
		add(jb);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public MusicPlayer(File f) throws Exception {
		this.playing = true;
		
		AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
		clip = AudioSystem.getClip();
		
		//reflection
		Class aisClass = ais.getClass();
		Field frameLengthField = aisClass.getDeclaredField("frameLength");
		Field framePosField = aisClass.getDeclaredField("framePos");
		frameLengthField.setAccessible(true);
		framePosField.setAccessible(true);
		this.frameLength = Long.parseLong(frameLengthField.get(ais).toString());
		this.currentFramePos = Long.parseLong(framePosField.get(ais).toString());
		
		JButton playPause = new JButton("Play/Pause");
		playPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("button pressed");
				if (playing) {
					System.out.println("pressed");
					playing = false;
					try {
						currentFramePos = Long.parseLong(framePosField.get(ais).toString());
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
					clip.stop();
				} else {
					System.out.println("button");
					playing = true;
					clip.setFramePosition((int)currentFramePos);
					clip.start();
				}
			}
		});
		
		JLabel jl = new JLabel("temp");

		Timer t = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					currentFramePos = Long.parseLong(framePosField.get(ais).toString());
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
				System.out.println(currentFramePos);
				//double perc = (double)currentFramePos / (double)frameLength;
				//jl.setText("" + perc);
			}
		});
		
		t.start();
		clip.open(ais);
		
		setLayout(new FlowLayout());
		add(playPause);
		add(jl);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	Clip clip;
	private final long frameLength;
	private long currentFramePos;
	private boolean playing;
}
