package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.TypedQuery;

@Entity
public class Result implements Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "cardset_id", referencedColumnName = "id", nullable = false)
	private CardSet cardSet;

	private LocalDateTime createdOn;

	private int defaultCount;

	private int correctCount;

	private int neutralCount;

	private int incorrectCount;

	public Result(CardSet cardSet, int[] counts) {

		this.cardSet = cardSet;

		correctCount = counts[Card.CORRECT];
		neutralCount = counts[Card.NEUTRAL];
		incorrectCount = counts[Card.INCORRECT];
		defaultCount = counts[Card.DEFAULT];
	}

	public Result() {

	}

	public static List<Result> ofSet(CardSet set) {

		if (set.isContained()) {
			TypedQuery<Result> query = Repository.findEntitiesByForeignKey(Result.class, "cardSet", set);
			List<Result> list = query.setMaxResults(10).getResultList();
			return list;
		}
		return new ArrayList<>();
	}

	@Override
	public void updateFields() {

	}

	@Override
	public void persist() {

		EntityTransaction et = EM.getTransaction();
		et.begin();
		List<Result> list = ofSet(cardSet);
		if (list.size() >= 10) {
			EM.remove(list.get(0));
		}
		EM.persist(this);
		et.commit();
	}

	// GETTERS AND SETTERS

	public long getId() {

		return id;
	}

	public CardSet getCardSet() {

		return cardSet;
	}

	public void setCardSet(CardSet cardSet) {

		this.cardSet = cardSet;
	}

	public LocalDateTime getCreatedOn() {

		return createdOn;
	}

	@PrePersist
	protected void prePersist() {

		createdOn = LocalDateTime.now();
	}

	public int getCorrectCount() {

		return correctCount;
	}

	public void setCorrectCount(int correctCount) {

		this.correctCount = correctCount;
	}

	public int getNeutralCount() {

		return neutralCount;
	}

	public void setNeutralCount(int neutralCount) {

		this.neutralCount = neutralCount;
	}

	public int getIncorrectCount() {

		return incorrectCount;
	}

	public void setIncorrectCount(int incorrectCount) {

		this.incorrectCount = incorrectCount;
	}

	public int getDefaultCount() {

		return defaultCount;
	}

	public void setDefaultCount(int ignoredCount) {

		defaultCount = ignoredCount;
	}

	public int getScore(int i) {

		return new int[] { correctCount, neutralCount, incorrectCount, defaultCount }[i];
	}
}
