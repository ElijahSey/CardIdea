package logic.data;

import java.util.ArrayList;
import java.util.List;

import dataAccess.DataManager;
import entity.Card;
import entity.CardSet;
import entity.DBEntity;
import entity.Topic;
import logic.parsers.CardParser;
import logic.parsers.MarkdownParser;

public class DataPreperator implements AutoCloseable {

	private final DataManager dm;
	private final List<CardParser> parsers;

	private static DataPreperator instance;

	private DataPreperator(DataManager dm) {

		this.dm = dm;
		PropertyManager.getInstance();
		parsers = new ArrayList<>();
		parsers.add(new MarkdownParser("Markdown"));
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

	public List<Card> getCardsOfTopic(Topic topic) {

		if (dm.contains(topic)) {
			return dm.findEntitesByForeignKey(Card.class, "topic", topic);
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

		set.setSize(cards.size());
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
