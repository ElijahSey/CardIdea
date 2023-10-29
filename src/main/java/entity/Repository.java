package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import logic.parsers.CardParser;
import logic.parsers.MarkdownParser;

public interface Repository extends Updatable {

	static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("prod");
	@PersistenceContext
	static final EntityManager em = emf.createEntityManager();
	static final CriteriaBuilder cb = em.getCriteriaBuilder();

	static final List<CardParser> parsers = List.of(new MarkdownParser("Markdown"));

	static <T> CriteriaQuery<T> getCriteriaQuery(Class<T> resultClass) {

		return cb.createQuery(resultClass);
	}

	static <T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {

		return em.createQuery(queryString, resultClass);
	}

	static <T> T find(Class<T> clazz, Object pk) {

		return em.find(clazz, pk);
	}

	static <T> List<T> findAll(Class<T> clazz) {

		TypedQuery<T> q = em.createQuery("SELECT e FROM %s e".formatted(clazz.getSimpleName()), clazz);
		return q.getResultList();
	}

	static <T, P> List<T> findEntitiesByForeignKey(Class<T> entity, String fk, P parent) {

		String query = "SELECT e FROM %s e WHERE e.%s = ?1".formatted(entity.getSimpleName(), fk);
		TypedQuery<T> q = em.createQuery(query, entity);
		q.setParameter(1, parent);
		return q.getResultList();
	}

	static <T, P> List<T> findLimitedEntitiesByForeignKey(Class<T> entity, String fk, P parent, int limit) {

		CriteriaQuery<T> q = cb.createQuery(entity);
		Root<T> root = q.from(entity);
		q.select(root).where(cb.equal(root.get(fk), parent));
		return em.createQuery(q).setMaxResults(limit).getResultList();
	}

	default boolean isContained() {

		return em.contains(this);
	}

	default void persist() {

		EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(this);
		et.commit();
	}

	static void persistAll(Iterable<?> entities) {

		EntityTransaction et = em.getTransaction();
		et.begin();
		for (Object o : entities) {
			em.persist(o);
		}
		et.commit();
	}

	default void update() {

		EntityTransaction et = em.getTransaction();
		et.begin();
		updateFields();
		et.commit();
	}

	static void updateAll(Iterable<? extends Updatable> entities) {

		EntityTransaction et = em.getTransaction();
		et.begin();
		for (Updatable entity : entities) {
			if (!em.contains(entity)) {
				em.persist(entity);
			} else {
				entity.updateFields();
			}
		}
		et.commit();
	}

	default void remove() {

		EntityTransaction et = em.getTransaction();
		et.begin();
		em.remove(this);
		et.commit();
	}

	static void removeAll(Iterable<?> entities) {

		EntityTransaction et = em.getTransaction();
		et.begin();
		for (Object e : entities) {
			em.remove(e);
		}
		et.commit();
	}

	static void close() {

		em.close();
		emf.close();
	}

	static List<CardParser> getParsers() {

		return new ArrayList<>(parsers);
	}
}
