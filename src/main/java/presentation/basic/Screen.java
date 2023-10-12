package presentation.basic;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import logic.data.DataPreperator;
import presentation.menuBar.CommandBar;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public abstract class Screen {

	protected LanguageManager lm;
	protected DataPreperator dp;
	protected GuiFactory gui;

	protected ContentPanel mainPanel;
	protected JPanel panel;
	protected List<MenuItem> items;

	public Screen(ContentPanel mainPanel) {
		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		gui = GuiFactory.getInstance();
		this.mainPanel = mainPanel;
	}

	protected void revalidate() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	protected abstract JPanel createContent();

	protected void createMenuItems() {
		items = new ArrayList<>();
	}

	protected String getHeader() {
		return lm.getString(this.getClass().getSimpleName() + ".header");
	}

	protected void afterOpening() {
		createMenuItems();
		CommandBar cmd = mainPanel.getCommandBar();
		for (MenuItem item : items) {
			cmd.add(item.menu, item.item);
		}
	}

	protected boolean afterClosing() {
		CommandBar cmd = mainPanel.getCommandBar();
		for (MenuItem item : items) {
			cmd.remove(item.menu, item.item);
		}
		return true;
	}

	protected void addMenuItem(int menu, String text, String tooltip, KeyStroke key, ActionListener l) {
		JMenuItem item = gui.createMenuItem(text);
		item.addActionListener(l);
		item.setAccelerator(key);
		item.setToolTipText(tooltip);
		items.add(new MenuItem(menu, item));
	}

	protected void showUserInfo(String message) {
		JOptionPane.showMessageDialog(mainPanel, message, lm.getString("warning"), JOptionPane.WARNING_MESSAGE);
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

	protected class MenuItem {

		int menu;
		JMenuItem item;

		public MenuItem(int menu, JMenuItem item) {
			this.menu = menu;
			this.item = item;
		}
	}
}
