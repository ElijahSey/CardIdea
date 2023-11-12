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

	static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("prod");
	@PersistenceContext
	static final EntityManager EM = EMF.createEntityManager();
	static final CriteriaBuilder CB = EM.getCriteriaBuilder();

	static final List<CardParser> parsers = List.of(new MarkdownParser("Markdown"));

	static <T> TypedQuery<T> createQuery(String queryString, Class<T> resultClass) {

		return EM.createQuery(queryString, resultClass);
	}

	static <T> T find(Class<T> clazz, Object pk) {

		return EM.find(clazz, pk);
	}

	static <T> List<T> findAll(Class<T> clazz) {

		TypedQuery<T> q = EM.createQuery("SELECT e FROM %s e".formatted(clazz.getSimpleName()), clazz);
		return q.getResultList();
	}

	static <T, P> TypedQuery<T> findEntitiesByForeignKey(Class<T> entity, String fk, P parent) {

//		String query = "SELECT e FROM %s e WHERE e.%s = ?1".formatted(entity.getSimpleName(), fk);
//		TypedQuery<T> q = EM.createQuery(query, entity);
//		q.setParameter(1, parent);
//		return q.getResultList();

		CriteriaQuery<T> q = CB.createQuery(entity);
		Root<T> root = q.from(entity);
		q.select(root).where(CB.equal(root.get(fk), parent));
		return EM.createQuery(q);
	}

	static <T, P> TypedQuery<T> findEntitiesByForeignKey(Class<T> entity, String fk, P parent,
			QueryModifier<T> modifier) {

		CriteriaQuery<T> q = CB.createQuery(entity);
		Root<T> root = q.from(entity);
		q.select(root).where(CB.equal(root.get(fk), parent));
		modifier.execute(q, root, CB);
		return EM.createQuery(q);
	}

	default boolean isContained() {

		return EM.contains(this);
	}

	default void persist() {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		EM.persist(this);
		et.commit();
	}

	static void persistAll(Iterable<?> entities) {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		for (Object o : entities) {
			EM.persist(o);
		}
		et.commit();
	}

	default void update() {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		updateFields();
		et.commit();
	}

	static void updateAll(Iterable<? extends Updatable> entities) {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		for (Updatable entity : entities) {
			if (!EM.contains(entity)) {
				EM.persist(entity);
			} else {
				entity.updateFields();
			}
		}
		et.commit();
	}

	default void remove() {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		EM.remove(this);
		et.commit();
	}

	static void removeAll(Iterable<?> entities) {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		for (Object e : entities) {
			EM.remove(e);
		}
		et.commit();
	}

	static void close() {

		EM.close();
		EMF.close();
	}

	static List<CardParser> getParsers() {

		return new ArrayList<>(parsers);
	}

	public interface QueryModifier<T> {

		void execute(CriteriaQuery<T> q, Root<T> root, CriteriaBuilder cb);
	}
}
