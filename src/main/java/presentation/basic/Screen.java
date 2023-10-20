package presentation.basic;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import javafx.scene.Node;
import javafx.scene.control.Label;
import logic.data.DataPreperator;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public abstract class Screen {

	protected LanguageManager lm;
	protected DataPreperator dp;
	protected GuiFactory gui;

	protected Label header;

	protected MainFrame mainFrame;
	protected Node node;
	protected List<MenuItem> items;

	public Screen() {
		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		mainFrame = MainFrame.getInstance();
	}

	protected void createMenuItems() {
		items = new ArrayList<>();
	}

	public void setHeader(Label header) {
		this.header = header;
//		header.setText(lm.getString(getClass(), "header"));
	}

	protected void addMenuItem(int menu, String text, String tooltip, KeyStroke key, ActionListener l) {
		JMenuItem item = gui.createMenuItem(text);
		item.addActionListener(l);
		item.setAccelerator(key);
		item.setToolTipText(tooltip);
		items.add(new MenuItem(menu, item));
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
