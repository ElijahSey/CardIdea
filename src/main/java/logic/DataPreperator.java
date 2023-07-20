package logic;

import java.util.List;

import dataAccess.DataManager;
import entity.Card;
import entity.CardSet;

public class DataPreperator implements AutoCloseable {

	private DataManager dm;
	private static DataPreperator instance;

	private DataPreperator() {
		dm = DataManager.getInstance();
	}

	public List<CardSet> getAllSets() {
		return dm.retrieveAllSets();
	}

	public List<Card> getCardsOfSet(CardSet set) {
		return dm.loadCardsOfSet(set);
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
