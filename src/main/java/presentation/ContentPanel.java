package presentation;

import javax.swing.JPanel;

public class ContentPanel extends JPanel {

	private Screen activeScreen;

	public ContentPanel() {
		super();
	}

	public Screen getActiveScreen() {
		return activeScreen;
	}

	public void setActiveScreen(Screen activeScreen) {
		this.activeScreen = activeScreen;
	}
}
