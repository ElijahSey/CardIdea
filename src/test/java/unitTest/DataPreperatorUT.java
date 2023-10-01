package unitTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import logic.data.DataPreperator;

class DataPreperatorUT {

	static DataPreperator dp;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dp = DataPreperator.getTestInstance();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		dp.close();
	}

	@Test
	void loadTopicsOfSetTest() {
		List<CardSet> sets = dp.getAllSets();
		List<Topic> topics = dp.getTopicsOfSet(sets.get(0));
		assertTrue(topics.size() == 2);
	}

	@Test
	void loadCardsOfSetTest() {
		List<CardSet> sets = dp.getAllSets();
		List<Card> cards = dp.getCardsOfSet(sets.get(0));
		assertTrue(cards.size() == 4);
	}
}
