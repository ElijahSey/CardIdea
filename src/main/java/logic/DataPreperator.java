package logic;

import java.util.List;

import dataAccess.DataManager;
import dataAccess.Update;
import entity.Card;
import entity.CardSet;

public class DataPreperator implements AutoCloseable {

	private DataManager dm;
	private static DataPreperator instance;

	private DataPreperator() {
		dm = DataManager.getInstance();
//		dm.insertTestData();
	}

	public List<CardSet> getAllSets() {
		return dm.retrieveAllSets();
	}

	public List<Card> getCardsOfSet(CardSet set) {
		return dm.loadCardsOfSet(set);
	}

	public List<String> getTopicsOfSet(CardSet set) {
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

	public void update(Object entity, Update update) {
		dm.update(entity, update);
	}

	public void deleteCard(Card card) {
		dm.remove(card);
	}

	public void deleteSet(CardSet set) {
		dm.removeAll(getCardsOfSet(set));
		dm.remove(set);
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
