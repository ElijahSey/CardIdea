package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Card {

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

	@Column(nullable = true)
	private int score;

	public Card() {
	}

	public Card(Topic topic, String question, String solution) {
		this(topic, question, solution, "");
	}

	public Card(Topic topic, String question, String solution, String hint) {
		super();
		this.topic = topic;
		this.question = question;
		this.solution = solution;
		this.hint = hint;
	}

	@Override
	public String toString() {
		return question;
	}

	@Override
	public boolean equals(Object o) {

		Card c = (Card) o;
		return topic.equals(c.getTopic()) && question.equals(c.getQuestion()) && solution.equals(c.getSolution());
	}

	// GETTERS AND SETTERS

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
