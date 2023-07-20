package presentation;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.DataPreperator;
import presentation.menuBar.MenuBar;
import presentation.util.GuiFactory;

public class MainFrame {

	protected DataPreperator dp;
	protected GuiFactory gui;
	private JPanel mainPanel;

	public MainFrame() {

		createFrame();
		dp = DataPreperator.getInstance();
		gui = GuiFactory.createDefaultGuiFactory();

		JPanel center = new JPanel();
		JPanel topBar = new MenuBar(center).getPanel();

		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new BorderLayout());
		panel.add(topBar, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		new Menu(center);
		mainPanel.removeAll();
		update(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(panel);
		update(mainPanel);
	}

	public void createFrame() {

		JFrame frame = new JFrame();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowCloseListener());

		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.add(new JLabel("Starting up..."));
		frame.add(mainPanel);
		frame.setVisible(true);
	}

	public void update(JComponent c) {
		c.revalidate();
		c.repaint();
	}

	private class WindowCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent we) {
			dp.close();
		}
	}
}
