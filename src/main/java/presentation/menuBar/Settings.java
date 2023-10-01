package presentation.menuBar;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import presentation.basic.ContentPanel;
import presentation.basic.Screen;
import presentation.util.LanguageManager;

public class Settings extends Screen {

	JComboBox<Locale> languages;

	public Settings(ContentPanel mainPanel) {
		super(mainPanel);
	}

	@Override
	protected JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout());

		languages = gui.createComboBox(dp.getAvailableLanguages(), new Locale[] {});
		JPanel center = new JPanel();
		center.add(languages);

		JButton cancel = gui.createButton(i18n.getString("cancel"));
		cancel.addActionListener(e -> cancel());
		JButton ok = gui.createButton(i18n.getString("ok"));
		ok.addActionListener(e -> saveSettings());
		JPanel south = new JPanel();
		south.add(cancel);
		south.add(ok);

		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		return panel;
	}

	private void cancel() {
		mainPanel.back();
	}

	private void saveSettings() {
		Locale language = (Locale) languages.getSelectedItem();
		if (language != null) {
			LanguageManager.getInstance().setLanguage(language);
		}
		mainPanel.rebuildAllScreens();
		mainPanel.back();
	}
}