package presentation.basic;

import javax.swing.JPanel;

import logic.data.DataPreperator;
import presentation.util.GuiFactory;

public abstract class Screen {

	protected DataPreperator dp;
	protected GuiFactory gui;
	protected ContentPanel mainPanel;
	protected JPanel panel;

	public Screen(ContentPanel mainPanel) {
		dp = DataPreperator.getInstance();
		gui = GuiFactory.getInstance();
		this.mainPanel = mainPanel;
	}

	protected void reload() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	protected abstract JPanel createContent();

	protected abstract String getHeader();

	protected void executeOpenAction() {

	}

	protected void executeExitAction() {

	}

	public void rebuild() {
		panel = createContent();
		reload();
	}

	public JPanel getPanel() {
		if (panel == null) {
			panel = createContent();
		}
		return panel;
	}
}
