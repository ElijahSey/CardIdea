package presentation;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.DataPreperator;
import presentation.util.GuiFactory;

public abstract class Screen {

	protected DataPreperator dp;
	protected GuiFactory gui;
	protected ContentPanel mainPanel;
	protected JLabel header;

	public Screen(ContentPanel mainPanel, JLabel header) {
		dp = DataPreperator.getInstance();
		gui = GuiFactory.getInstance();
		this.mainPanel = mainPanel;
		Screen activeScreen = mainPanel.getActiveScreen();
		if (activeScreen != null) {
			activeScreen.executeExitAction();
		}
		mainPanel.setActiveScreen(this);
		this.header = header;
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		mainPanel.setLayout(new BorderLayout());
	}

	protected void addContent() {
		mainPanel.add(createContent());
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	protected void reload() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	protected abstract JPanel createContent();

	protected abstract void executeExitAction();
}
