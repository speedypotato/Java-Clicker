import java.awt.event.ActionEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MediaThread extends Application {
	public MediaThread() {
		System.out.println(media.toString());
	}
	public MediaThread(File media) {
		this.media = media;
	}
	@Override
	public void start(Stage arg0) throws Exception {
		//Get song length
		AudioInputStream ais = AudioSystem.getAudioInputStream(media.getAbsoluteFile());
		Clip c = AudioSystem.getClip();
		c.open(ais);
		double songLength = c.getMicrosecondLength() * 0.001;
		c.close();
		
		//Play Song and create thread
		Media m = new Media(media.toURI().toString());
		mp = new MediaPlayer(m);
		mp.play();
		mp.pause();
		thread = new Thread();
		thread.start();
		
		//Exit thread after song is done
		t = new Timer((int)songLength + 1, (ActionEvent e) -> Platform.exit());
		t.start();
	}
	@Override
	public void stop() {
		thread = null;
		t = null;
	}
	public MediaPlayer getMediaPlayer() {
		return mp;
	}
	/**
	 * Enter point
	 */
	public void show(File f) {
		//this.mode = mode;
		this.media = f;
		System.out.println(media.toString());
		launch();
	}
	
	public double getSongLength() throws Exception {
		//Get song length
		AudioInputStream ais = AudioSystem.getAudioInputStream(media.getAbsoluteFile());
		Clip c = AudioSystem.getClip();
		c.open(ais);
		double songLength = c.getMicrosecondLength() * 0.001;
		c.close();
		return songLength;
	}
	private char mode;
	private File media;
	private Thread thread;
	private MediaPlayer mp;
	private Timer t;
}
