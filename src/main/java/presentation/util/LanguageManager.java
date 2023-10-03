package presentation.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

	private ResourceBundle bundle;

	private static LanguageManager instance;

	private LanguageManager() {
	}

	public String getString(String key) {
		return bundle.getString(key);
	}

	public void setLanguage(Locale language) {

		bundle = getBundle(language);
	}

	public void setLanguage(String language) {
		setLanguage(new Locale(language));
	}

	private ResourceBundle getBundle(Locale language) {
		return ResourceBundle.getBundle("internationalization.i18n", language);
	}

	public static LanguageManager getInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}
}
