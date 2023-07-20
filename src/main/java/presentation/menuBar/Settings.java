package presentation.menuBar;

import javax.swing.JPanel;

import presentation.Screen;

public class Settings extends Screen {

	public Settings(JPanel mainPanel) {
		super(mainPanel);
		addContent();
	}

	@Override
	protected JPanel createContent() {
		JPanel panel = new JPanel();
		return panel;
	}
}
