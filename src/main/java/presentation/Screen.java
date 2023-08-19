package presentation;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.DataPreperator;
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

	protected void error(String message) {
		JOptionPane.showMessageDialog(mainPanel, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
