package presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entity.Card;
import entity.CardSet;

public class CardViewer extends Screen {

	private CardSet cardSet;
	private JLabel question;
	private JTextArea answer, solution;
	private String hint;
	private ListIterator<Card> iterator;

	public CardViewer(JPanel mainPanel, CardSet cardSet) {
		super(mainPanel);
		this.cardSet = cardSet;
		List<Card> cards = dp.getCardsOfSet(cardSet);
		Collections.shuffle(cards);
		iterator = cards.listIterator();
		addContent();
		nextCard(0);
	}

	@Override
	public JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel headerPanel = new JPanel();
		headerPanel.add(new JLabel(cardSet.getName()));
		panel.add(headerPanel, BorderLayout.NORTH);

		question = new JLabel();
		answer = new JTextArea();

		solution = new JTextArea();
		solution.setEditable(false);
		solution.setVisible(false);

		JPanel cardPanel = new JPanel(new BorderLayout());

		cardPanel.add(question, BorderLayout.NORTH);

		JPanel cardBody = new JPanel(new GridLayout(2, 1));
		cardBody.add(answer);
		cardBody.add(solution);

		cardPanel.add(cardBody, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton prev = gui.createButton("<");
		prev.addActionListener(e -> prevCard());
		JButton reveal = gui.createButton("reveal");
		reveal.addActionListener(e -> toggleSolution());
		JButton hint = gui.createButton("hint");
		hint.addActionListener(e -> showHint());

		JPanel nextPanel = new JPanel(new GridLayout(0, 1));

		JButton correct = gui.createButton("correct");
		correct.addActionListener(e -> nextCard(1));
		JButton skip = gui.createButton("unshure");
		skip.addActionListener(e -> nextCard(0));
		JButton wrong = gui.createButton("wrong");
		wrong.addActionListener(e -> nextCard(-1));

		nextPanel.add(correct);
		nextPanel.add(skip);
		nextPanel.add(wrong);

		buttonPanel.add(prev);
		buttonPanel.add(reveal);
		buttonPanel.add(hint);
		buttonPanel.add(nextPanel);

		cardPanel.add(buttonPanel, BorderLayout.SOUTH);

		panel.add(cardPanel, BorderLayout.CENTER);

		return panel;
	}

	private void updateContent(Card card) {

		solution.setVisible(false);

		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		answer.setText("");
		hint = card.getHint();
	}

	private void prevCard() {
		if (iterator.hasPrevious()) {
			updateContent(iterator.previous());
		}
	}

	private void nextCard(int score) {

		// TODO Score speichern
		if (iterator.hasNext()) {
			updateContent(iterator.next());
		}
	}

	private void toggleSolution() {
		solution.setVisible(!solution.isVisible());
	}

	private void showHint() {
		JOptionPane.showMessageDialog(mainPanel, hint);
	}
}
