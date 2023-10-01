package logic.parsers;

import java.util.ArrayList;
import java.util.List;

import entity.Card;
import entity.CardSet;
import entity.Topic;

public abstract class CardParser {

	private String name;
	protected List<Topic> topics;
	protected List<Card> cards;

	public CardParser(String name) {
		this.name = name;
		topics = new ArrayList<>();
		cards = new ArrayList<>();
	}

	public abstract void parse(String text, CardSet cardSet);

	public abstract CardParser newInstance();

	@Override
	public String toString() {
		return name;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public List<Card> getCards() {
		return cards;
	}
}
