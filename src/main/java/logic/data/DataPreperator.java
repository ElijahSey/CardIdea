package logic.data;

import java.util.ArrayList;
import java.util.List;

import dataAccess.DataManager;
import dataAccess.Update;
import entity.Card;
import entity.CardSet;
import entity.Topic;
import logic.parsers.CardParser;
import logic.parsers.MarkdownParser;

public class DataPreperator implements AutoCloseable {

	private DataManager dm;
	private static DataPreperator instance;

	private List<CardParser> parsers;

	private DataPreperator() {
		dm = DataManager.getInstance();
		parsers = new ArrayList<>();
		parsers.add(new MarkdownParser("Markdown", this));
//		dm.insertTestData();
	}

	public List<CardSet> getAllSets() {
		return dm.retrieveAllSets();
	}

	public List<Card> getCardsOfSet(CardSet set) {
		return dm.loadCardsOfSet(set);
	}

	public List<Topic> getTopicsOfSet(CardSet set) {
		return dm.loadTopicsOfSet(set);
	}

	public boolean isUniqueName(CardSet set, String newName) {
		if (set.getName().equals(newName)) {
			return true;
		}
		List<CardSet> sets = dm.retrieveAllSets();
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

	public void update(Object entity, Update update) {
		dm.update(entity, update);
	}

	public void deleteCard(Card card) {
		dm.remove(card);
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
		return parsers;
	}

	public static DataPreperator getInstance() {
		if (instance == null) {
			instance = new DataPreperator();
		}
		return instance;
	}

	@Override
	public void close() {
		dm.close();
	}
}
