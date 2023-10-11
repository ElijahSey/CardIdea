package presentation.basic;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;

import presentation.menuBar.CommandBar;

public class ContentPanel extends JPanel {

	private JLabel header;
	private Stack<Screen> screens;
	private List<ReloadListener> reloadListeners;
	private Frame frame;
	private CommandBar commandBar;

	public ContentPanel(JLabel header, Frame frame) {
		super();
		this.header = header;
		this.frame = frame;
		screens = new Stack<>();
		reloadListeners = new ArrayList<>();
		commandBar = new CommandBar(this);
		setLayout(new BorderLayout());
	}

	public void addScreen(Screen screen) {
		screens.push(screen);
		displayScreen(screen);
	}

	public boolean back() {
		if (screens.size() < 2) {
			getActiveScreen().rebuild();
			return true;
		}
		if (!screens.peek().afterClosing()) {
			return false;
		}
		screens.pop();
		displayScreen(screens.peek());
		return true;
	}

	public void home() {
		if (!screens.peek().afterClosing()) {
			return;
		}
		Screen home = screens.firstElement();
		screens.clear();
		home.rebuild();
		addScreen(home);
	}

	private void displayScreen(Screen screen) {
		removeAll();
		revalidate();
		setHeader(screen.getHeader());
		add(screen.getPanel());
		screen.afterOpening();
		revalidate();
	}

	public void rebuildActiveScreen() {
		Screen screen = getActiveScreen();
		screen.rebuild();
		displayScreen(screen);
	}

	public void rebuildAllScreens() {
		for (ReloadListener r : reloadListeners) {
			r.reload();
		}
		for (Screen s : screens) {
			s.rebuild();
		}
	}

	public void addReloadListener(ReloadListener l) {
		reloadListeners.add(l);
	}

	public void setHeader(String text) {
		header.setText(text);
	}

	public Screen getActiveScreen() {
		return screens.peek();
	}

	public CommandBar getCommandBar() {
		return commandBar;
	}

	public Frame getFrame() {
		return frame;
	}

	@Override
	public void revalidate() {
		super.revalidate();
		super.repaint();
	}

	public interface ReloadListener {
		void reload();
	}
}
