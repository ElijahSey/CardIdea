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
import entity.Topic;

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
	void persistCardTest() {
		CardSet set = new CardSet("Python");
		mgr.persist(set);
		Topic topic = new Topic("Topic", set);
		mgr.persist(topic);
		Card card = new Card(topic, "Question", "Solution", "Hint");
		mgr.persist(card);
		assertTrue(mgr.getEntityManager().contains(card));
		mgr.remove(card);
		mgr.remove(topic);
		mgr.remove(set);
	}

	@Test
	void retrieveAllSetsTest() {
		List<CardSet> list = mgr.retrieveAllSets();
		Set<CardSet> set = new HashSet<>(list);
		assertEquals(set.size(), list.size());
	}
}
