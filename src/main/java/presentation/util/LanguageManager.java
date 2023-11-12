package presentation.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public final class LanguageManager {

	private Locale locale;

	private static LanguageManager instance;

	public static final String I18N_FOLDER = "internationalization";

	private LanguageManager() {

	}

	public ResourceBundle getBundle(String className) {

		return ResourceBundle.getBundle(String.join(".", I18N_FOLDER, locale.getLanguage(), className), locale);
	}

	public String getString(String key) {

		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		if (className.contains(".")) {
			className = className.substring(className.lastIndexOf(".") + 1);
		}
		return getBundle(className).getString(key);
	}

	public String getString(Class<?> clazz, String key) {

		ResourceBundle bundle = getBundle(clazz.getSimpleName());
		return bundle.getString(key);
	}

	public String[] getArray(Class<?> clazz, String key) {

		return getString(clazz, key).split(";");
	}

	public void setLocale(Locale locale) {

		this.locale = locale;
		JOptionPane.setDefaultLocale(locale);
		Locale.setDefault(locale);
	}

	public void setLocale(String locale) {

		setLocale(new Locale(locale));
	}

	public Locale getLocale() {

		return locale;
	}

	public static LanguageManager getInstance() {

		if (instance == null) {
			instance = new LanguageManager();
		}
		return instance;
	}
}
