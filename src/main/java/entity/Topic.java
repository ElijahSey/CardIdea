package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Topic implements Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "cardset_id", referencedColumnName = "id", nullable = false)
	private CardSet cardSet;

	public Topic() {

	}

	public Topic(String name, CardSet cardSet) {

		super();
		setName(name);
		this.cardSet = cardSet;
	}

	@Override
	public void updateFields() {

		setName(name);
		setCardSet(cardSet);
	}

	@Override
	public String toString() {

		return name;
	}

	public static List<Topic> ofSet(CardSet set) {

		if (set.isContained()) {
			return Repository.findEntitiesByForeignKey(Topic.class, "cardSet", set);
		}
		return new ArrayList<>();
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

	public CardSet getCardSet() {

		return cardSet;
	}

	public void setCardSet(CardSet cardSet) {

		this.cardSet = cardSet;
	}
}
