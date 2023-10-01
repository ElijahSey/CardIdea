package presentation.basic;

import javax.swing.JPanel;

import logic.data.DataPreperator;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public abstract class Screen {

	protected DataPreperator dp;
	protected GuiFactory gui;
	protected ContentPanel mainPanel;
	protected JPanel panel;
	protected LanguageManager i18n;

	public Screen(ContentPanel mainPanel) {
		i18n = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		gui = GuiFactory.getInstance();
		this.mainPanel = mainPanel;
	}

	protected void revalidate() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	protected abstract JPanel createContent();

	protected String getHeader() {
		return i18n.getString(this.getClass().getSimpleName() + ".header");
	}

	protected void afterOpening() {

	}

	protected boolean afterClosing() {
		return true;
	}

	public void rebuild() {
		panel = createContent();
		revalidate();
	}

	public JPanel getPanel() {
		if (panel == null) {
			panel = createContent();
		}
		return panel;
	}
}
