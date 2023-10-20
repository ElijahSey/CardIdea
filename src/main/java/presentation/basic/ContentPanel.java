package presentation.basic;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import presentation.menuBar.CommandBar;
import presentation.menuBar.MenuBar;

public class ContentPanel extends JPanel {

	private JLabel header;
	private Deque<Screen> screens;
	private List<ReloadListener> reloadListeners;
	private Stage primaryStage;
	private MenuBar menuBar;

	public ContentPanel(Stage primaryStage, JLabel header) {
		super();
		this.header = header;
		this.primaryStage = primaryStage;
		screens = new ArrayDeque<>();
		reloadListeners = new ArrayList<>();
		menuBar = new MenuBar(this, header);
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
		Screen home = screens.peek();
		screens.clear();
		home.rebuild();
		addScreen(home);
	}

	private void displayScreen(Screen screen) {
		BorderPane layout = new BorderPane();
		layout.setTop(menuBar.getNode());
		layout.setCenter(screen.getNode());
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		setHeader(screen.getHeader());
		screen.afterOpening();
		revalidate();
	}

	public void rebuildActiveScreen() {
		Screen screen = getActiveScreen();
		screen.rebuild();
		displayScreen(screen);
	}

	public void rebuildEverything() {
		rebuildCommandBar();
		for (ReloadListener r : reloadListeners) {
			r.reload();
		}
		for (Screen s : screens) {
			s.rebuild();
		}
		displayScreen(screens.peek());
	}

	public void rebuildCommandBar() {
//		commandBar = new CommandBar(this);
//		frame.setJMenuBar(commandBar);
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
		return menuBar.getCommandBar();
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
