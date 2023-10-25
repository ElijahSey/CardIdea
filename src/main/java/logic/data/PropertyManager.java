package logic.data;

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

import dataAccess.DataManager;
import entity.Property;
import presentation.util.LanguageManager;

public class PropertyManager {

	private final DataManager dm;

	private static List<Locale> availableLanguages;
	private static PropertyManager instance;

	public static final String PROPERTIES_FOLDER = "/properties/";
	private static final String LANGUAGE_KEY = "language";

	private PropertyManager() {
		dm = DataManager.getInstance();
		try {
			initializeProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeProperties() throws IOException {
		if (dm.countProperties() < 1) {
			initializeDatabase();
		}
		Map<String, String> props = getAllProperties();
		LanguageManager.getInstance().setLocale(props.get(LANGUAGE_KEY));
	}

	private void initializeDatabase() throws IOException {
		Properties defaults = new Properties();
		defaults.load(getClass().getResourceAsStream(PROPERTIES_FOLDER + "Default.properties"));
		dm.persist(new Property(LANGUAGE_KEY, defaults.getProperty(LANGUAGE_KEY)));
	}

	private List<Locale> findAvailableLanguages() {
		List<Locale> locales = new ArrayList<>();
		List<Path> list = listFilesOfDir("/" + LanguageManager.I18N_FOLDER);
		for (Path p : list) {
			String file = p.getFileName().toString();
			if (file.endsWith(".properties")) {
				locales.add(new Locale(file.split("_")[1].substring(0, 2)));
			}
		}
		return locales;
	}

	private List<Path> listFilesOfDir(String path) {
		try {
			URI uri = getClass().getResource(path).toURI();
			try (FileSystem fileSystem = (uri.getScheme().equals("jar")
					? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap())
					: null)) {
				Path myPath = Paths.get(uri);
				List<Path> list = Files.walk(myPath, 1).toList();
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getAllProperties() {
		return dm.findAll(Property.class).stream()
				.collect(Collectors.toUnmodifiableMap(Property::getName, Property::getContent));
	}

	public Property getProperty(String property) {
		return dm.find(Property.class, property);
	}

	public void setProperty(String property, String value) {
		Property prop = getProperty(property);
		prop.setContent(value);
		dm.update(prop);
	}

	public List<Locale> getAvailableLanguages() {
		if (availableLanguages == null) {
			availableLanguages = findAvailableLanguages();
		}
		return availableLanguages;
	}

	public static PropertyManager getInstance() {
		if (instance == null) {
			instance = new PropertyManager();
		}
		return instance;
	}
}
