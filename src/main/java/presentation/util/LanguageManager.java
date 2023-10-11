package presentation.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ibm.icu.text.MessageFormat;

public class LanguageManager {

	private ResourceBundle bundle;

	private static LanguageManager instance;

	private LanguageManager() {
	}

	public String getString(String key) {
		return bundle.getString(key);
	}

	public String getString(String key, int magnitude) {
		MessageFormat message = new MessageFormat(bundle.getString(key), bundle.getLocale());
		return message.format(new Object[] { magnitude });
	}

	public void setLanguage(Locale locale) {

		bundle = getBundle(locale);
		JOptionPane.setDefaultLocale(locale);
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
