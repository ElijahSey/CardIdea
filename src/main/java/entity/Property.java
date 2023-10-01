package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Property implements DBEntity {

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
	public void update() {
		setContent(content);
	}

	// GETTERS AND SETTERS

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
