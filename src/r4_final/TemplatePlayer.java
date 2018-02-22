package r4_final;

/**
 * Template Player
 * @author Nicholas Gadjali
 */
public class TemplatePlayer extends StartFrameTemplate {
	/**
	 * Launches the window
	 * @param currentlySelected Map Name
	 */
	public void launchWindow(String s) {
		new PlayFrame(s); //Open PlayFrame
	}
}
