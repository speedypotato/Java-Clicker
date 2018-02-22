package proofOfConcept;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayTest extends Application {
	private Thread thread;
	private MediaPlayer mp;
	private Timer t;
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		File f = new File("D:\\Documents\\EclipseWorkspace\\cs151 final project\\javasu\\easyTest.wav");
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		File f = (new File("D:\\Documents\\EclipseWorkspace\\cs151 final project\\javasu\\easyTest.wav"));
		
		//Get song length
		AudioInputStream ais = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
		Clip c = AudioSystem.getClip();
		c.open(ais);
		double songLength = c.getMicrosecondLength() * 0.001;
		c.close();
		
		//Play Song and create thread
		Media m = new Media(f.toURI().toString());
		mp = new MediaPlayer(m);
		mp.play();
		thread = new Thread();
		thread.start();
		
		//Exit thread after song is done
		t = new Timer((int)songLength + 1, (ActionEvent e) -> Platform.exit());
		t.start();
		
		//Initiate playframe?
		PlayFrame pf = new PlayFrame(mp);
	}
	
	@Override
	public void stop() {
		thread = null;
		t = null;
	}
}
