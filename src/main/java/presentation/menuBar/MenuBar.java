package presentation.menuBar;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import presentation.Menu;

public class MenuBar {

	private JPanel panel, mainPanel;
	private static final int HEIGHT = 40;

	public MenuBar(JPanel mainPanel) {
		this.mainPanel = mainPanel;
		panel = createContent();
		panel.setSize(100, HEIGHT);
		panel.setBackground(new Color(150, 200, 255));
	}

	private JPanel createContent() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton home = createButton("Home");
		home.addActionListener(e -> new Menu(mainPanel));
		JButton settings = createButton("Settings");
		settings.addActionListener(e -> new Settings(mainPanel));

		panel.add(home);
		panel.add(settings);
		return panel;
	}

	private JButton createButton(String text) {
		JButton b = new JButton(text);
		b.setFocusPainted(false);
		return b;
	}

	public JPanel getPanel() {
		return panel;
	}
}
