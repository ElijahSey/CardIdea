package presentation.basic;

import javax.swing.JPanel;

import logic.data.DataPreperator;

public abstract class IntegratedPanel extends JPanel {

	protected DataPreperator dp;

	public IntegratedPanel() {

		dp = DataPreperator.getInstance();
	}

	public void reload() {
		revalidate();
		repaint();
	}
}
