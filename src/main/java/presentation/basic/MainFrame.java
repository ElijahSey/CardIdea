package presentation.basic;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.data.DataPreperator;
import presentation.menuBar.CommandBar;
import presentation.menuBar.MenuBar;
import presentation.screens.Menu;
import presentation.util.GuiFactory;

public class MainFrame {

	protected DataPreperator dp;
	protected GuiFactory gui;
	private JFrame frame;
	private ContentPanel contentPanel;
	private JPanel mainPanel;
	private Image icon;

	public MainFrame() {

		init();
		SplashScreen splash = showSplashScreen();
		dp = DataPreperator.getInstance();
		gui = GuiFactory.createDefaultGuiFactory();
		frame = createFrame();
		if (splash != null) {
			splash.close();
		}
		frame.setVisible(true);
	}

	private void init() {
		try {
			icon = ImageIO.read(getClass().getResourceAsStream("/images/card_logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private SplashScreen showSplashScreen() {

		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null) {
			System.err.println("Image for splashscreen not found");
		}
		return splash;
	}

	private JFrame createFrame() {

		JFrame frame = new JFrame();
		frame.setSize(800, 500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setIconImage(icon);
		frame.addWindowListener(new WindowCloseListener());

		mainPanel = new JPanel(new GridBagLayout());

		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new BorderLayout());

		JLabel header = new JLabel();
		contentPanel = new ContentPanel(header, frame);
		CommandBar cmdBar = contentPanel.getCommandBar();
		MenuBar menuBar = new MenuBar(contentPanel, header);
		JPanel topBar = menuBar.getPanel();
		contentPanel.addReloadListener(() -> menuBar.rebuild(panel));

		panel.add(topBar, BorderLayout.NORTH);
		panel.add(contentPanel, BorderLayout.CENTER);

		contentPanel.addScreen(new Menu(contentPanel));
		mainPanel.removeAll();
		update(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(panel);

		frame.setJMenuBar(cmdBar);
		frame.add(mainPanel);
		return frame;
	}

	private void update(JComponent c) {
		c.revalidate();
		c.repaint();
	}

	private class WindowCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent we) {

			if (!contentPanel.back()) {
				return;
			}
			frame.dispose();
			dp.close();
			System.exit(0);
		}
	}
}
