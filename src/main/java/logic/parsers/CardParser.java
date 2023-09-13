package logic.parsers;

import java.util.List;

import entity.Card;
import entity.CardSet;
import logic.data.DataPreperator;

public abstract class CardParser {

	private String name;
	protected DataPreperator dp;

	public CardParser(String name, DataPreperator dp) {
		this.name = name;
		this.dp = dp;
	}

	public abstract List<Card> parse(String text, CardSet cardSet);

	@Override
	public String toString() {
		return name;
	}
}
