package presentation.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class LanguageManager {

	private Locale locale;

	private static LanguageManager instance;

	public static final String I18N_FOLDER = "internationalization";

	private LanguageManager() {
	}

	public void setLanguage(Locale locale) {

		this.locale = locale;
		JOptionPane.setDefaultLocale(locale);
		Locale.setDefault(locale);
	}

	public void setLanguage(String language) {
		setLanguage(new Locale(language));
	}

	public ResourceBundle getBundle(String className) {
		return ResourceBundle.getBundle(String.join(".", I18N_FOLDER, locale.getLanguage(), className), locale);
	}

	public String getString(Class<?> clazz, String key) {
		ResourceBundle bundle = getBundle(clazz.getSimpleName());
		return bundle.getString(key);
	}

	public static LanguageManager getInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}
}
