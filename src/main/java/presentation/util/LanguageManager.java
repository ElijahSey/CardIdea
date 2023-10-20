package presentation.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ibm.icu.text.MessageFormat;

public class LanguageManager {

	private Locale locale;

	private static LanguageManager instance;

	private static final String PARENT_FOLDER = "internationalization";

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
		return ResourceBundle.getBundle(String.join(".", PARENT_FOLDER, locale.getLanguage(), className), locale);
	}

	public String getString(Class<?> clazz, String key) {
		return getBundle(clazz.getSimpleName()).getString(key);
	}

	public String getString(Class<?> clazz, String key, int magnitude) {
		MessageFormat message = new MessageFormat(getString(clazz, key), locale);
		return message.format(new Object[] { magnitude });
	}

	public static LanguageManager getInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}
}
