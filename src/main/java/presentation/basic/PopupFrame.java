package presentation.basic;

import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

import logic.data.DataPreperator;
import presentation.util.LanguageManager;

public abstract class PopupFrame {

	protected LanguageManager i18n;
	protected DataPreperator dp;

	protected JDialog dialog;
	protected Frame frame;

	private static final int MARGIN = 10;

	public PopupFrame(boolean modal) {
//		dialog = new JDialog(mainPanel.getFrame(), modal);
//		frame = mainPanel.getFrame();

		i18n = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
	}

	public void show() {
		create();
		dialog.setVisible(true);
	}

	public void create() {
		JPanel panel = createContent();
		panel.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));

		dialog.setTitle(getHeader());
		dialog.setSize(400, 500);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(frame);
	}

	public void setVisible(boolean flag) {
		dialog.setVisible(flag);
	}

	public void close() {
		dialog.dispose();
	}

	protected abstract JPanel createContent();

	protected String getHeader() {
		return i18n.getString(getClass(), this.getClass().getSimpleName() + ".header");
	}
}
