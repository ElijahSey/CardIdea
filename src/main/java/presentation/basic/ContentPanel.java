package presentation.basic;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ContentPanel extends JPanel {

	private JLabel header;
	private Stack<Screen> screens;
	private List<ReloadListener> reloadListeners;

	public ContentPanel(JLabel header) {
		super();
		this.header = header;
		screens = new Stack<>();
		reloadListeners = new ArrayList<>();
		setLayout(new BorderLayout());
	}

	public void addScreen(Screen screen) {
		screens.push(screen);
		displayScreen(screen);
	}

	public void back() {
		if (screens.size() < 2) {
			getActiveScreen().rebuild();
			return;
		}
		if (!screens.peek().afterClosing()) {
			return;
		}
		screens.pop();
		displayScreen(screens.peek());
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
		header.setText(screen.getHeader());
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

	public Screen getActiveScreen() {
		return screens.peek();
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
