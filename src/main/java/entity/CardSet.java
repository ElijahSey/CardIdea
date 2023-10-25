package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CardSet implements DBEntity {

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
	public void update() {

		setName(name);
		setSize(size);
	}

	@Override
	public String toString() {

		return name;
	}

	// GETTERS AND SETTERS

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

	public int getSize() {

		return size;
	}

	public void setSize(int size) {

		this.size = size;
	}
}
