package presentation.basic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import javafx.scene.Node;
import javafx.scene.control.Label;
import logic.data.DataPreperator;
import presentation.util.LanguageManager;

public abstract class Screen {

	protected LanguageManager lm;
	protected DataPreperator dp;

	protected Label header;

	protected MainFrame mainFrame;
	protected Node node;
	protected List<MenuItem> items;

	public Screen() {
		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		mainFrame = MainFrame.getInstance();
	}

	public abstract void initialize();

	protected void createMenuItems() {
		items = new ArrayList<>();
	}

	public void setHeaderLabel(Label header) {
		this.header = header;
	}

	public void afterLoad() {
		header.setText(getHeader());
	}

	public boolean beforeClose() {
		return true;
	}

	public String getHeader() {
		return lm.getString(getClass(), "header");
	}

//	protected void addMenuItem(int menu, String text, String tooltip, KeyStroke key, ActionListener l) {
//		JMenuItem item = gui.createMenuItem(text);
//		item.addActionListener(l);
//		item.setAccelerator(key);
//		item.setToolTipText(tooltip);
//		items.add(new MenuItem(menu, item));
//	}

	protected class MenuItem {

		int menu;
		JMenuItem item;

		public MenuItem(int menu, JMenuItem item) {
			this.menu = menu;
			this.item = item;
		}
	}
}
