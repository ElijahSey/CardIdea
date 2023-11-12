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
import jakarta.persistence.Transient;
import jakarta.persistence.TypedQuery;

@Entity
public class Card implements Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
	private Topic topic;

	@Column(length = 127)
	private String question;

	@Column(length = 511)
	private String solution;

	@Column(length = 255)
	private String hint;

	@Column
	private Integer position;

	@Transient
	private int score;

	public static final int CORRECT = 0;
	public static final int NEUTRAL = 1;
	public static final int INCORRECT = 2;
	public static final int DEFAULT = 3;

	public Card() {

		score = DEFAULT;
	}

	public Card(Topic topic, String question, String solution) {

		this(topic, question, solution, "");
	}

	public Card(Topic topic, String question, String solution, String hint) {

		this();
		this.topic = topic;
		this.question = question;
		this.solution = solution;
		this.hint = hint;
	}

	@Override
	public void updateFields() {

		setTopic(topic);
		setQuestion(question);
		setSolution(solution);
		setHint(hint);
		setPosition(position);
		setScore(score);
	}

	@Override
	public String toString() {

		return question;
	}

	public static List<Card> ofTopic(Topic topic) {

		if (topic.isContained()) {
			TypedQuery<Card> query = Repository.findEntitiesByForeignKey(Card.class, "topic", topic,
					(q, root, cb) -> q.orderBy(cb.asc(root.get("position"))));
			return query.getResultList();
		}
		return new ArrayList<>();
	}

	public static List<Card> ofSet(CardSet set) {

		if (!set.isContained()) {
			return new ArrayList<>();
		}
		TypedQuery<Card> q = Repository.createQuery("SELECT c FROM Card c JOIN c.topic t ON t.cardSet = ?1",
				Card.class);
		q.setParameter(1, set);
		return q.getResultList();
	}

	// GETTERS AND SETTERS

	public long getId() {

		return id;
	}

	public Topic getTopic() {

		return topic;
	}

	public void setTopic(Topic topic) {

		this.topic = topic;
	}

	public String getQuestion() {

		return question;
	}

	public void setQuestion(String question) {

		this.question = question;
	}

	public String getSolution() {

		return solution;
	}

	public void setSolution(String solution) {

		this.solution = solution;
	}

	public String getHint() {

		return hint;
	}

	public void setHint(String hint) {

		this.hint = hint;
	}

	public int getPosition() {

		return position;
	}

	public void setPosition(int position) {

		this.position = position;
	}

	public int getScore() {

		return score;
	}

	public void setScore(int score) {

		this.score = score;
	}
}
