package entity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

	private void persist(Collection<Topic> topics, Collection<Card> cards) {

		setSize(cards.size());
		persist();
		Repository.persistAll(topics);
		Repository.persistAll(cards);
	}

	public void update(LinkedHashMap<Topic, List<Card>> map) {

		Set<Topic> topics = map.keySet();
		List<Card> cards = normalize(map);
		if (!isContained()) {
			persist(topics, cards);
		} else {
			setSize(cards.size());
			List<Card> cardDB = Card.ofSet(this);
			cardDB.removeAll(cards);
			Repository.removeAll(cardDB);
			List<Topic> topicsDB = Topic.ofSet(this);
			topicsDB.removeAll(topics);
			Repository.removeAll(topicsDB);
			update();
			Repository.updateAll(topics);
			Repository.updateAll(cards);
		}
	}

	private List<Card> normalize(LinkedHashMap<Topic, List<Card>> map) {

		List<Card> cardList = new LinkedList<>();
		int topicPos = 0;
		for (Entry<Topic, List<Card>> entry : map.entrySet()) {
			entry.getKey().setPosition(topicPos++);
			int cardPos = 0;
			for (Card card : entry.getValue()) {
				card.setPosition(cardPos++);
				cardList.add(card);
			}
		}
		return cardList;
	}

	@Override
	public void remove() {

		Repository.removeAll(Card.ofSet(this));
		Repository.removeAll(Topic.ofSet(this));
		Repository.super.remove();
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
