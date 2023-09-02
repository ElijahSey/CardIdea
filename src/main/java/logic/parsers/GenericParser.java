package logic.parsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entity.Card;

public class GenericParser extends CardParser {

	private static final String TOPIC_STUB = "<topic>";
	private static final String QUESTION_STUB = "<question>";
	private static final String SOLUTION_STUB = "<solution>";
	private static final String HINT_STUB = "<hint>";

	private LinkedHashMap<String, String> delimiters;

	public GenericParser(String name, String exampleText) {

		super(name);
		delimiters = new LinkedHashMap<>();

		List<String> stubs = new ArrayList<>();
		if (exampleText.contains(TOPIC_STUB)) {
			stubs.add(TOPIC_STUB);
		}
		if (exampleText.contains(QUESTION_STUB)) {
			stubs.add(QUESTION_STUB);
		}
		if (exampleText.contains(SOLUTION_STUB)) {
			stubs.add(SOLUTION_STUB);
		}
		if (exampleText.contains(HINT_STUB)) {
			stubs.add(HINT_STUB);
		}
		stubs.sort((e1, e2) -> exampleText.indexOf(e1) - exampleText.indexOf(e2));

		String beginning = exampleText.substring(0, exampleText.indexOf(stubs.get(0)));
		String formattedText = exampleText.substring(exampleText.indexOf(stubs.get(0)), exampleText.length())
				+ beginning;

		String[] patterns = formattedText.split("<[a-z]+?>");
		for (int i = 0; i < stubs.size(); i++) {
			delimiters.put(stubs.get(i), patterns[i]);
		}
	}

	@Override
	public List<Card> parse(String text) {

		List<Card> cards = new ArrayList<>();

		try (Scanner sc = new Scanner(text)) {

			while (sc.hasNextLine()) {
				Card c = new Card();
				for (Map.Entry<String, String> entry : delimiters.entrySet()) {

					String snippet = sc.findWithinHorizon(entry.getValue(), 0);
					switch (entry.getKey()) {
					case TOPIC_STUB:
						c.setTopic(snippet);
						break;
					case QUESTION_STUB:
						c.setQuestion(snippet);
						break;
					case SOLUTION_STUB:
						c.setSolution(snippet);
						break;
					case HINT_STUB:
						c.setHint(snippet);
						break;
					}
					cards.add(c);
				}
			}
		}
		return cards;
	}
}
