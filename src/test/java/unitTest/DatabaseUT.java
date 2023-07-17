package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataAccess.DataManager;
import entity.Card;

class DatabaseUT {

	static DataManager mgr;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		mgr = new DataManager(DataManager.TESTING);
		List<Card> cards = new ArrayList<>();

		List<String> sets = List.of("Testing", "Architektur", "PVS", "Betriebssysteme", "Agile");

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 3; j++) {
				for (String set : sets) {
					cards.add(new Card(set, set + "Topic" + i, "Question" + j, "Solution" + j, "Hint" + j));
				}
			}
		}

		for (Card c : cards) {
			mgr.persist(c);
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		mgr.close();
	}

	@Test
	void persistCardTest() {
		Card card = new Card("Python", "Topic", "Question", "Solution", "Hint");
		mgr.persist(card);
		assertTrue(mgr.getEntityManager().contains(card));
		mgr.detach(card);
	}

	@Test
	void retrieveCardTest() {
		Card card = new Card("CardSet1", "Topic", "Question", "Solution", "Hint");
		mgr.persist(card);
		List<Card> cards = mgr.retrieveAllCards();
		assertTrue(cards.contains(card));
		mgr.detach(card);
	}

	@Test
	void retrieveAllSetsTest() {
		List<String> list = mgr.retrieveAllSets();
		Set<String> set = new HashSet<>(list);
		assertEquals(set.size(), list.size());
	}

	@Test
	void retrieveCardsBySetTest() {
		List<Card> list = mgr.retrieveCardsBySet("Testing");
		for (Card c : list) {
			if (!c.getCardSet().equals("Testing")) {
				fail(c.getCardSet());
			}
		}
	}
}
