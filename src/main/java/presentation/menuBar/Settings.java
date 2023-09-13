package presentation.menuBar;

import javax.swing.JPanel;

import presentation.basic.ContentPanel;
import presentation.basic.Screen;

public class Settings extends Screen {

	public Settings(ContentPanel mainPanel) {
		super(mainPanel);
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

	@Override
	protected String getHeader() {
		return "Settings";
	}
}
