package presentation.menuBar;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import presentation.basic.ContentPanel;
import presentation.popup.Settings;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public class CommandBar extends JMenuBar {

	private LanguageManager lm;
	private GuiFactory gui;
	private ContentPanel contentPanel;

	public static final int FILE = 0;
	public static final int EDIT = 1;
	public static final int VIEW = 2;

	public CommandBar(ContentPanel contentPanel) {
		super();
		lm = LanguageManager.getInstance();
		gui = GuiFactory.getInstance();
		this.contentPanel = contentPanel;

		String[] menus = { "file", "edit", "view" };

		for (String s : menus) {
			add(gui.createMenu(lm.getString(s)));
		}
		addDefaultMenuItems();
	}

	public void add(int menu, JMenuItem item) {
		((JMenu) getComponent(menu)).add(item, 0);
	}

	public void remove(int menu, JMenuItem item) {
		((JMenu) getComponent(menu)).remove(item);
	}

	public JMenuItem addMenuItem(int menu, String text, String tooltip, KeyStroke key, ActionListener l) {
		JMenuItem item = gui.createMenuItem(text);
		item.addActionListener(l);
		item.setAccelerator(key);
		item.setToolTipText(tooltip);
		((JMenu) getComponent(menu)).add(item, 0);
		return item;
	}

	private void addDefaultMenuItems() {
		addMenuItem(FILE, lm.getString("settings"), null, null, e -> new Settings(contentPanel).show());
	}
}
