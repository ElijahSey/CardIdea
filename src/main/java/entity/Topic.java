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
import jakarta.persistence.TypedQuery;

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

	@Column
	private Integer position;

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
		setPosition(position);
	}

	@Override
	public String toString() {

		return name;
	}

	public static List<Topic> ofSet(CardSet set) {

		if (set.isContained()) {
			TypedQuery<Topic> query = Repository.findEntitiesByForeignKey(Topic.class, "cardSet", set,
					(q, root, cb) -> q.orderBy(cb.asc(root.get("position"))));
			return query.getResultList();
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

	public int getPosition() {

		return position;
	}

	public void setPosition(int position) {

		this.position = position;
	}
}
