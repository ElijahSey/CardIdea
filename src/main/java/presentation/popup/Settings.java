package presentation.popup;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import presentation.basic.ContentPanel;
import presentation.basic.PopupFrame;
import presentation.util.LanguageManager;

public class Settings extends PopupFrame {

	private JComboBox<Locale> languages;

	public Settings(ContentPanel mainPanel) {
		super(mainPanel, true);
	}

	@Override
	protected JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout());

		languages = gui.createComboBox(dp.getAvailableLanguages(), new Locale[] {}, l -> l.getDisplayLanguage(l));
		JPanel center = new JPanel();
		center.add(languages);

		JButton cancel = gui.createButton(i18n.getString("cancel"));
		cancel.addActionListener(e -> super.close());
		JButton ok = gui.createButton(i18n.getString("ok"));
		ok.addActionListener(e -> saveSettings());
		JPanel south = new JPanel();
		south.add(cancel);
		south.add(ok);

		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		return panel;
	}

	private void saveSettings() {
		Locale language = (Locale) languages.getSelectedItem();
		if (language != null) {
			LanguageManager.getInstance().setLanguage(language);
		}
		mainPanel.rebuildEverything();
		super.close();
	}
}