package logic.data;

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
import java.util.stream.Collectors;

import dataAccess.DataManager;
import entity.Card;
import entity.CardSet;
import entity.DBEntity;
import entity.Property;
import entity.Topic;
import logic.parsers.CardParser;
import logic.parsers.MarkdownParser;
import presentation.util.LanguageManager;

public class DataPreperator implements AutoCloseable {

	private DataManager dm;
	private List<CardParser> parsers;
	private List<Locale> availableLanguages;

	private static DataPreperator instance;

	private static final String LANGUAGE_KEY = "Language";

	private DataPreperator(DataManager dm) {
		this.dm = dm;
		initializeProperties();
		parsers = new ArrayList<>();
		parsers.add(new MarkdownParser("Markdown"));
	}

	private void initializeProperties() {
		if (dm.countProperties() < 1) {
			initializeDatabase();
		}
		Map<String, String> props = getAllProperties();
		LanguageManager.getInstance().setLanguage(props.get(LANGUAGE_KEY));
	}

	private void initializeDatabase() {
		dm.persist(new Property(LANGUAGE_KEY, Locale.getDefault().getLanguage()));
	}

	private List<Locale> findAvailableLanguages() {
		List<Locale> locales = new ArrayList<>();
		List<Path> list = listFilesOfDir("/internationalization");
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

	public void saveProperty(String property, String value) {
		Property prop = dm.find(Property.class, property);
		prop.setContent(value);
		update(prop);
	}

	public List<CardSet> getAllSets() {
		return dm.findAll(CardSet.class);
	}

	public List<Card> getCardsOfSet(CardSet set) {
		if (dm.contains(set)) {
			return dm.findCardsOfSet(set);
		}
		return new ArrayList<>();
	}

	public List<Topic> getTopicsOfSet(CardSet set) {
		if (dm.contains(set)) {
			return dm.findEntitesByForeignKey(Topic.class, "cardSet", set);
		}
		return new ArrayList<>();
	}

	public boolean isUniqueName(CardSet set, String newName) {
		if (set.getName().equals(newName)) {
			return true;
		}
		List<CardSet> sets = dm.findAll(CardSet.class);
		for (CardSet s : sets) {
			if (s.getName().equals(newName)) {
				return false;
			}
		}
		return true;
	}

	public void insert(Object entity) {
		dm.persist(entity);
	}

	public void insertAll(Iterable<?> entities) {
		dm.persistAll(entities);
	}

	public <T extends DBEntity> void update(T entity) {
		dm.update(entity);
	}

	public void delete(Object entity) {
		dm.remove(entity);
	}

	public void insertSet(CardSet set, List<Topic> topics, List<Card> cards) {
		dm.persist(set);
		dm.persistAll(topics);
		dm.persistAll(cards);
	}

	public void updateSet(CardSet set, List<Topic> topics, List<Card> cards) {

		if (!dm.contains(set)) {
			insertSet(set, topics, cards);
		} else {
			List<Card> cardDB = getCardsOfSet(set);
			cardDB.removeAll(cards);
			dm.removeAll(cardDB);
			List<Topic> topicsDB = getTopicsOfSet(set);
			topicsDB.removeAll(topics);
			dm.removeAll(topicsDB);
			dm.update(set);
			dm.updateAll(topics);
			dm.updateAll(cards);
		}
	}

	public void deleteSet(CardSet set) {
		dm.removeAll(getCardsOfSet(set));
		dm.removeAll(getTopicsOfSet(set));
		dm.remove(set);
	}

	public void addParser(CardParser p) {
		parsers.add(p);
	}

	public List<CardParser> getParsers() {
		return new ArrayList<>(parsers);
	}

	public List<Locale> getAvailableLanguages() {
		if (availableLanguages == null) {
			availableLanguages = findAvailableLanguages();
		}
		return availableLanguages;
	}

	public static DataPreperator getInstance() {
		if (instance == null) {
			instance = new DataPreperator(DataManager.getInstance());
		}
		return instance;
	}

	public static DataPreperator getTestInstance() {
		if (instance == null) {
			instance = new DataPreperator(DataManager.getTestInstance());
		}
		return instance;
	}

	@Override
	public void close() {
		instance = null;
		dm.close();
	}
}
