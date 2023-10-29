package entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CardSet implements Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 45, nullable = false)
	private String name;

	@Column(columnDefinition = "integer default 0")
	private int size;

	public CardSet() {

		size = 0;
	}

	public CardSet(String name) {

		super();
		this.name = name;
	}

	@Override
	public void updateFields() {

		setName(name);
		setSize(size);
	}

	@Override
	public String toString() {

		return name;
	}

	public static List<CardSet> all() {

		return Repository.findAll(CardSet.class);
	}

	public void persist(List<Topic> topics, List<Card> cards) {

		this.persist();
		Repository.persistAll(topics);
		Repository.persistAll(cards);
	}

	public void update(List<Topic> topics, List<Card> cards) {

		setSize(cards.size());
		if (!isContained()) {
			persist(topics, cards);
		} else {
			List<Card> cardDB = Card.ofSet(this);
			cardDB.removeAll(cards);
			Repository.removeAll(cardDB);
			List<Topic> topicsDB = Topic.ofSet(this);
			topicsDB.removeAll(topics);
			Repository.removeAll(topicsDB);
			this.update();
			Repository.updateAll(topics);
			Repository.updateAll(cards);
		}
	}

	@Override
	public void remove() {

		Repository.removeAll(Card.ofSet(this));
		Repository.removeAll(Topic.ofSet(this));
		remove();
	}

	// GETTERS AND SETTERS

	public long getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public int getSize() {

		return size;
	}

	public void setSize(int size) {

		this.size = size;
	}
}
