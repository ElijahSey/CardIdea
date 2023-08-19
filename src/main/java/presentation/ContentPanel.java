package presentation;

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

	public void openScreen(Screen screen) {

		if (!screens.isEmpty()) {
			screens.peek().executeExitAction();
		}
		screens.push(screen);
		addScreen(screen);
	}

	public void back() {
		if (screens.size() < 2) {
			return;
		}
		screens.pop();
		addScreen(screens.peek());
	}

	public void home() {
		Screen home = screens.firstElement();
		home.rebuild();
		openScreen(home);
		screens.clear();
		screens.push(home);
	}

	private void addScreen(Screen screen) {
		removeAll();
		repaint();
		header.setText(screen.getHeader());
		add(screen.getPanel());
		repaint();
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
