package logic.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import logic.data.DataPreperator;

public class MarkdownParser extends CardParser {

	public MarkdownParser(String name, DataPreperator dp) {
		super(name, dp);
	}

	@Override
	public List<Card> parse(String text, CardSet cardSet) {

		List<Card> cards = new ArrayList<>();

		int maxDepth = 0;
		try (Scanner sc = new Scanner(text)) {
			maxDepth = checkDepth(sc);
		}
		final String TOPIC_PRE = "#".repeat(maxDepth - 1) + " ";
		final String CARD_PRE = "#".repeat(maxDepth) + " ";

		String[] topicArr = Pattern.compile("^" + TOPIC_PRE, Pattern.MULTILINE).split(text);
		for (int i = 1; i < topicArr.length; i++) {
			String topicString = topicArr[i];
			String[] topicSplit = Pattern.compile("\\R").split(topicString, 2);
			String[] cardArr = Pattern.compile("^" + CARD_PRE, Pattern.MULTILINE).split(topicSplit[1].trim());
			Topic topic = new Topic(topicSplit[0], cardSet);
			dp.insert(topic);
			for (int k = 1; k < cardArr.length; k++) {
				String cardString = cardArr[k];
				String[] cardSplit = Pattern.compile("\\R").split(cardString, 2);
				cards.add(new Card(topic, cardSplit[0], cardSplit[1], ""));
			}
		}
		return cards;
	}

	private int checkDepth(Scanner sc) {

		int hashtags = -1;

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.startsWith("#")) {
				if (hashtags < line.indexOf(" ")) {
					hashtags = line.indexOf(" ");
				}
			}
		}
		return hashtags;
	}
}
