package entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CardSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@OneToMany(orphanRemoval = true)
	private List<Card> cards;

	public CardSet(String name) {
		super();
		this.name = name;
		cards = new ArrayList<>();
	}

	public CardSet() {

	}

	@Override
	public boolean equals(Object o) {

		CardSet s = (CardSet) o;
		return name.equals(s.getName()) && cards.equals(s.getCards());
	}

	@Override
	public String toString() {
		return name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getCards() {
		if (cards == null) {
			Hibernate.initialize(cards);
		}
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
}
