package presentation;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.DataPreperator;
import presentation.menuBar.MenuBar;
import presentation.util.GuiFactory;

public class MainFrame {

	protected DataPreperator dp;
	protected GuiFactory gui;
	private JFrame frame;
	private JPanel mainPanel;
	private final Object lock = new Object();

	public MainFrame() {

		createFrame();
		dp = DataPreperator.getInstance();
		synchronized (lock) {
			lock.notify();
		}

		SwingUtilities.invokeLater(() -> {
			gui = GuiFactory.createDefaultGuiFactory();

			JPanel panel = new JPanel(new BorderLayout());
			panel.setLayout(new BorderLayout());

			JLabel header = new JLabel();
			ContentPanel center = new ContentPanel(header);
			JPanel topBar = new MenuBar(center, header).getPanel();

			panel.add(topBar, BorderLayout.NORTH);
			panel.add(center, BorderLayout.CENTER);

			center.openScreen(new Menu(center));
			mainPanel.removeAll();
			update(mainPanel);
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(panel);
			update(mainPanel);
		});
	}

	public void createFrame() {

		frame = new JFrame();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

			frame.dispose();
			try {
				synchronized (lock) {
					if (dp == null) { // if window is closed while connecting to db
						lock.wait();
					}
					dp.close();
					System.exit(0);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
