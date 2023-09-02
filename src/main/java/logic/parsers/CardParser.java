package logic.parsers;

import java.util.List;

import entity.Card;

public abstract class CardParser {

	private String name;

	public CardParser(String name) {
		this.name = name;
	}

	public abstract List<Card> parse(String text);

	@Override
	public String toString() {
		return name;
	}
}
