package presentation.basic;

import java.awt.BorderLayout;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ContentPanel extends JPanel {

	private JLabel header;
	private Stack<Screen> screens;

	public ContentPanel(JLabel header) {
		super();
		this.header = header;
		screens = new Stack<>();
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
		screens.pop().executeExitAction();
		displayScreen(screens.peek());
	}

	public void home() {
		screens.peek().executeExitAction();
		Screen home = screens.firstElement();
		screens.clear();
		home.rebuild();
		addScreen(home);
	}

	private void displayScreen(Screen screen) {
		removeAll();
		repaint();
		header.setText(screen.getHeader());
		add(screen.getPanel());
		screen.executeOpenAction();
		repaint();
	}

	public void refreshScreen() {
		Screen screen = getActiveScreen();
		screen.rebuild();
		displayScreen(screen);
	}

	public Screen getActiveScreen() {
		return screens.peek();
	}

	@Override
	public void repaint() {
		super.revalidate();
		super.repaint();
	}
}
