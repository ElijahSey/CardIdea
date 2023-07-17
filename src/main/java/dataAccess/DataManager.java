package dataAccess;

import java.util.List;

import entity.Card;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

public class DataManager implements AutoCloseable {

	private final EntityManagerFactory emf;
	@PersistenceContext
	private final EntityManager em;
	public static final String PRODUCTION = "prod";
	public static final String TESTING = "test";

	public DataManager(String unit) {

		emf = Persistence.createEntityManagerFactory(unit);
		em = emf.createEntityManager();
	}

	public List<Card> retrieveAllCards() {

		CriteriaQuery<Card> cq = em.getCriteriaBuilder().createQuery(Card.class);
		cq.select(cq.from(Card.class));
		return em.createQuery(cq).getResultList();
	}

	public List<String> retrieveAllSets() {
		CriteriaQuery<String> cq = em.getCriteriaBuilder().createQuery(String.class);
		cq.select(cq.from(Card.class).get("cardSet")).distinct(true);
		return em.createQuery(cq).getResultList();
	}

	public List<Card> retrieveCardsBySet(String set) {

		TypedQuery<Card> q = em.createQuery("select c from Card c where c.cardSet = ?1", Card.class);
		q.setParameter(1, set);
		return q.getResultList();
	}

	public void persist(Object entity) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(entity);
		et.commit();
	}

	public void detach(Object entity) {
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.detach(entity);
		et.commit();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void close() throws Exception {
		em.close();
		emf.close();
	}
}
