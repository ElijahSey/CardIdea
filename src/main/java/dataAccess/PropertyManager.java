package dataAccess;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import entity.Property;
import entity.Repository;
import presentation.util.LanguageManager;

public final class PropertyManager {

	private static List<Locale> availableLanguages;

	public static final String PROPERTIES_FOLDER = "/properties/";
	public static final String LANGUAGE_KEY = "language";

	private PropertyManager() {

	}

	public static void initializeProperties() {

		if (Property.count() < 1) {
			try {
				initializeDatabase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, String> props = getAllProperties();
		LanguageManager.getInstance().setLocale(props.get(LANGUAGE_KEY));
	}

	private static void initializeDatabase() throws IOException {

		Properties defaults = new Properties();
		defaults.load(PropertyManager.class.getResourceAsStream(PROPERTIES_FOLDER + "Default.properties"));

		String defaultLoc = Locale.getDefault().getLanguage();
		String initialLoc = defaults.getProperty(LANGUAGE_KEY);
		for (Locale loc : findAvailableLanguages()) {
			if (loc.getLanguage().equals(defaultLoc)) {
				initialLoc = loc.getLanguage();
			}
		}
		new Property(LANGUAGE_KEY, initialLoc).persist();
	}

	private static List<Locale> findAvailableLanguages() {

		List<Locale> locales = new ArrayList<>();
		List<Path> list = listDirectoryContent("/" + LanguageManager.I18N_FOLDER);
		for (Path p : list) {
			String loc = p.toString();
			locales.add(new Locale(loc.substring(loc.length() - 2)));
		}
		return locales;
	}

	private static List<Path> listDirectoryContent(String path) {

		try {
			URI uri = PropertyManager.class.getResource(path).toURI();
			try (FileSystem fileSystem = (uri.getScheme().equals("jar")
					? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap())
					: null)) {
				Path myPath = Paths.get(uri);
				List<Path> list = Files.walk(myPath, 1).toList();
				return list.subList(1, list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> getAllProperties() {

		return Repository.findAll(Property.class).stream()
				.collect(Collectors.toUnmodifiableMap(Property::getName, Property::getContent));
	}

	public static Property getProperty(String property) {

		return Repository.find(Property.class, property);
	}

	public static void setProperty(String property, String value) {

		Property prop = getProperty(property);
		prop.setContent(value);
		prop.update();
	}

	public static List<Locale> getAvailableLanguages() {

		if (availableLanguages == null) {
			availableLanguages = findAvailableLanguages();
		}
		return availableLanguages;
	}
}
