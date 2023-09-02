package dataAccess;

import java.util.ArrayList;
import java.util.List;

import entity.Card;
import entity.CardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class DataManager implements AutoCloseable {

	private EntityManagerFactory emf;
	@PersistenceContext
	private final EntityManager em;
	private static DataManager instance;

	private static final String PRODUCTION = "prod";
	private static final String TESTING = "test";

	private DataManager(String unit) {

		emf = Persistence.createEntityManagerFactory(unit);
		em = emf.createEntityManager();
	}

	public void insertTestData() {
		List<Card> cards = new ArrayList<>();
		List<String> sets = List.of("Testing", "Architektur", "PVS", "Betriebssysteme", "Agile");

		for (String setName : sets) {
			CardSet set = new CardSet(setName);
			persist(set);
			for (int i = 0; i < 5; i++) {
				for (int j = 1; j < 3; j++) {
					cards.add(new Card(set, setName + "Topic" + i, "Question" + j, "Solution" + j, "Hint" + j));
				}
			}
		}
		persistAll(cards);
	}

	public List<CardSet> retrieveAllSets() {
		TypedQuery<CardSet> q = em.createQuery("SELECT s FROM CardSet s", CardSet.class);
		return q.getResultList();
	}

	public List<Card> loadCardsOfSet(CardSet set) {
		TypedQuery<Card> q = em.createQuery("SELECT c FROM Card c WHERE c.cardSet = ?1", Card.class);
		q.setParameter(1, set);
		return q.getResultList();
	}

	public List<String> loadTopicsOfSet(CardSet set) {
		TypedQuery<String> q = em.createQuery("SELECT DISTINCT c.topic FROM Card c WHERE c.cardSet = ?1", String.class);
		q.setParameter(1, set);
		return q.getResultList();
	}

	public void persist(Object entity) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(entity);
		et.commit();
	}

	public void persistAll(Iterable<?> entities) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		for (Object o : entities) {
			em.persist(o);
		}
		et.commit();
	}

	public void update(Object entity, Update update) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		update.update(entity);
		et.commit();
	}

	public void remove(Object entity) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.remove(entity);
		et.commit();
	}

	public void removeAll(List<?> entities) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		for (Object e : entities) {
			em.remove(e);
		}
		et.commit();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void close() {
		em.close();
		emf.close();
	}

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager(PRODUCTION);
		}
		return instance;
	}

	public static DataManager getTestInstance() {
		if (instance == null) {
			instance = new DataManager(TESTING);
		}
		return instance;
	}
}
