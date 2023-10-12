package presentation.menuBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import presentation.basic.ContentPanel;
import presentation.popup.Settings;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public class MenuBar {

	private JPanel panel;
	private ContentPanel contentArea;
	private JLabel header;
	private LanguageManager i18n;
	private static final int HEIGHT = 40;

	public MenuBar(ContentPanel contentArea, JLabel header) {
		this.contentArea = contentArea;
		this.header = header;
		i18n = LanguageManager.getInstance();
		panel = createContent();
		panel.revalidate();
		panel.repaint();
	}

	protected JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.setSize(100, HEIGHT);
		panel.setBackground(new Color(150, 200, 255));

		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		left.setOpaque(false);
		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
		right.setOpaque(false);

		JButton back = createButton(i18n.getString("back"));
		back.setToolTipText(i18n.getString("MenuBar.back.tooltip"));
		back.addActionListener(e -> contentArea.back());

		JButton home = createButton(i18n.getString("home"));
		home.setToolTipText(i18n.getString("MenuBar.home.tooltip"));
		home.addActionListener(e -> contentArea.home());

		JButton settings = createButton(i18n.getString("settings"));
		settings.setToolTipText(i18n.getString("MenuBar.settings.tooltip"));
		settings.addActionListener(e -> new Settings(contentArea).show());

		left.add(back);
		left.add(home);
		left.add(header);

		right.add(settings);

		panel.add(left, BorderLayout.WEST);
		panel.add(right, BorderLayout.EAST);
		return panel;
	}

	public void rebuild(JPanel parent) {
		parent.remove(panel);
		parent.revalidate();
		parent.repaint();
		panel = createContent();
		parent.add(panel, BorderLayout.NORTH);
		parent.revalidate();
		parent.repaint();
	}

	private JButton createButton(String text) {
		return GuiFactory.getInstance().createButton(text);
	}

	public JPanel getPanel() {
		return panel;
	}
}
