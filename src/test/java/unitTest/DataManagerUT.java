package unitTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dataAccess.DataManager;
import entity.CardSet;

class DataManagerUT {

	static DataManager dm;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dm = DataManager.getTestInstance();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		dm.close();
	}

	@Test
	void loadAllTest() {
		List<CardSet> sets = dm.findAll(CardSet.class);
		assertTrue(sets.size() == 2);
	}
}
