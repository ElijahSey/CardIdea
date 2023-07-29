package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataAccess.DataManager;
import entity.Card;
import entity.CardSet;

class DatabaseUT {

	static DataManager mgr;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		mgr = DataManager.getTestInstance();
		mgr.insertTestData();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		mgr.close();
	}

	@Test
	void persistCardSetTest() {

	}

	@Test
	void persistCardTest() {
		CardSet set = new CardSet("Python");
		mgr.persist(set);
		Card card = new Card(set, "Topic", "Question", "Solution", "Hint");
		mgr.persist(card);
		assertTrue(mgr.getEntityManager().contains(card));
		mgr.remove(card);
	}

	@Test
	void retrieveAllSetsTest() {
		List<CardSet> list = mgr.retrieveAllSets();
		Set<CardSet> set = new HashSet<>(list);
		assertEquals(set.size(), list.size());
	}
}
