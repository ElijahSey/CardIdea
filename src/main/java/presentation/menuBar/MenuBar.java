package presentation.menuBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import presentation.ContentPanel;

public class MenuBar {

	private JPanel panel;
	private static final int HEIGHT = 40;

	public MenuBar(ContentPanel contentArea, JLabel header) {
		panel = createContent(contentArea, header);
	}

	protected JPanel createContent(ContentPanel contentArea, JLabel header) {
		JPanel panel = new JPanel(new BorderLayout());

		panel.setSize(100, HEIGHT);
		panel.setBackground(new Color(150, 200, 255));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		left.setOpaque(false);
		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
		right.setOpaque(false);

		JButton back = createButton("Back");
		back.addActionListener(e -> contentArea.back());

		JButton home = createButton("Home");
		home.addActionListener(e -> contentArea.home());

		JButton settings = createButton("Settings");
		settings.addActionListener(e -> contentArea.addScreen(new Settings(contentArea)));

		left.add(back);
		left.add(home);
		left.add(header);

		right.add(settings);

		panel.add(left, BorderLayout.WEST);
		panel.add(right, BorderLayout.EAST);
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
