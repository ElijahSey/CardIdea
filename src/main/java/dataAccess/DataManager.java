package dataAccess;

import java.util.List;

import entity.Card;
import entity.CardSet;
import entity.DBEntity;
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

	private DataManager(String unit) {

		emf = Persistence.createEntityManagerFactory(unit);
		em = emf.createEntityManager();
	}

	public int countProperties() {
		final TypedQuery<Number> query = em.createQuery("SELECT COUNT(p) FROM Property p", Number.class);
		return query.getSingleResult().intValue();
	}

	public <T extends DBEntity> T find(Class<T> entity, Object pk) {
		return em.find(entity, pk);
	}

	public <T extends DBEntity> List<T> findAll(Class<T> entity) {
		TypedQuery<T> q = em.createQuery("SELECT e FROM %s e".formatted(entity.getSimpleName()), entity);
		return q.getResultList();
	}

	public <T extends DBEntity, P extends DBEntity> List<T> findEntitesByForeignKey(Class<T> entity, String fk,
			P parent) {
		String query = "SELECT e FROM %s e WHERE e.%s = ?1".formatted(entity.getSimpleName(), fk);
		TypedQuery<T> q = em.createQuery(query, entity);
		q.setParameter(1, parent);
		return q.getResultList();
	}

	public List<Card> findCardsOfSet(CardSet set) {
		TypedQuery<Card> q = em.createQuery("SELECT c FROM Card c JOIN c.topic t ON t.cardSet = ?1", Card.class);
		q.setParameter(1, set);
		return q.getResultList();
	}

	public boolean contains(Object entity) {
		return em.contains(entity);
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

	public <T extends DBEntity> void update(T entity) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		entity.update();
		et.commit();
	}

	public void updateAll(Iterable<? extends DBEntity> entities) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		for (DBEntity entity : entities) {
			if (!em.contains(entity)) {
				em.persist(entity);
			} else {
				entity.update();
			}
		}
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
		instance = null;
		em.close();
		emf.close();
	}

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager("prod");
		}
		return instance;
	}

	public static DataManager getTestInstance() {
		if (instance == null) {
			instance = new DataManager("test");
		}
		return instance;
	}
}
