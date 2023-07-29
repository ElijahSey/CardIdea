package presentation.menuBar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import presentation.ContentPanel;
import presentation.Screen;

public class Settings extends Screen {

	public Settings(ContentPanel mainPanel, JLabel header) {
		super(mainPanel, header);
		header.setText("Settings");
		addContent();
	}

	@Override
	protected JPanel createContent() {
		JPanel panel = new JPanel();
		return panel;
	}

	@Override
	protected void executeExitAction() {
		System.out.println("Exit Settings");
	}
}
