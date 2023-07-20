package presentation;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import logic.DataPreperator;
import presentation.util.GuiFactory;

public abstract class Screen {

	protected DataPreperator dp;
	protected GuiFactory gui;
	protected JPanel mainPanel;

	public Screen(JPanel mainPanel) {
		dp = DataPreperator.getInstance();
		gui = GuiFactory.getInstance();
		this.mainPanel = mainPanel;
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

	protected abstract JPanel createContent();
}
