package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.TypedQuery;

@Entity
public class Property implements Repository {

	@Id
	@Column(length = 30, nullable = false)
	private String name;

	@Column(length = 50, nullable = false)
	private String content;

	public Property() {

	}

	public Property(String name, String content) {

		this.name = name;
		this.content = content;
	}

	@Override
	public void updateFields() {

		setContent(content);
	}

	public static int count() {

		String queryString = "SELECT COUNT(p) FROM Property p";
		TypedQuery<Number> query = Repository.createQuery(queryString, Number.class);
		return query.getSingleResult().intValue();
	}

	// GETTERS AND SETTERS

	public String getName() {

		return name;
	}

	public String getContent() {

		return content;
	}

	public void setContent(String content) {

		this.content = content;
	}
}
