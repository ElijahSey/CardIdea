package presentation.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entity.Card;
import entity.CardSet;
import presentation.basic.ContentPanel;
import presentation.basic.Screen;

public class CardViewer extends Screen {

	private CardSet cardSet;
	private JLabel question;
	private JTextArea answer, solution;
	private String hint;
	private ListIterator<Card> iterator;
	private List<Integer> scores;

	public CardViewer(ContentPanel mainPanel, CardSet cardSet) {
		super(mainPanel);
		this.cardSet = cardSet;
		scores = new LinkedList<>();
		List<Card> cards = dp.getCardsOfSet(cardSet);
		Collections.shuffle(cards);
		iterator = cards.listIterator();
		nextCard(0);
	}

	@Override
	public JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel headerPanel = new JPanel();
		question = new JLabel();
		headerPanel.add(question);
		panel.add(headerPanel, BorderLayout.NORTH);

		answer = gui.createTextArea();
		solution = gui.createTextArea();
		solution.setEditable(false);
		solution.setVisible(false);

		JPanel center = new JPanel(new GridBagLayout());

		JPanel cardBody = new JPanel(new GridLayout(2, 1, 0, 10));
		cardBody.add(gui.createScrollPane(answer));
		cardBody.add(gui.createScrollPane(solution));
		cardBody.setPreferredSize(new Dimension(400, 250));

		center.add(cardBody);
		panel.add(center, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		JButton prev = gui.createButton("<");
		prev.addActionListener(e -> prevCard());
		JButton reveal = gui.createButton("reveal");
		reveal.addActionListener(this::toggleSolution);
		reveal.setPreferredSize(new Dimension(75, 26));
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

		panel.add(buttonPanel, BorderLayout.SOUTH);

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

		scores.add(score);
		if (iterator.hasNext()) {
			updateContent(iterator.next());
		}
	}

	private void toggleSolution(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		boolean visible = solution.isVisible();
		if (visible) {
			b.setText("reveal");
		} else {
			b.setText("hide");
		}
		solution.setVisible(!visible);
	}

	private void showHint() {
		JOptionPane.showMessageDialog(mainPanel, hint, "Hint", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	protected String getHeader() {
		return cardSet.getName();
	}
}
