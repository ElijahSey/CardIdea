package presentation.screens;

import java.util.Collections;
import java.util.List;

import entity.Card;
import entity.CardSet;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import logic.util.SelectionIterator;
import presentation.basic.Screen;

public class CardViewer extends Screen {

	private CardSet cardSet;
	private SelectionIterator<Card> iterator;

	@FXML
	private Label questionLabel;

	@FXML
	private TextArea answerArea, solutionArea;

	@FXML
	private Button revealButton;

	public CardViewer(CardSet set) {
		cardSet = set;
		List<Card> cards = dp.getCardsOfSet(cardSet);
		Collections.shuffle(cards);
		iterator = new SelectionIterator<>(cards);
	}

	@Override
	public void initialize() {
		updateContent();
	}

	@FXML
	private void handlePrevious() {
		if (iterator.hasPrevious()) {
			iterator.previous();
			updateContent();
		}
	}

	@FXML
	private void handleNext() {

		int score = 0;
		iterator.element().setScore(score);
		dp.update(iterator.element());
		if (iterator.hasNext()) {
			iterator.next();
			updateContent();
		}
	}

	@FXML
	private void handleHint() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(lm.getString(getClass(), "hint"));
		alert.setContentText(iterator.element().getHint());
		alert.show();
	}

	@FXML
	private void handleReveal() {

		revealButton.setVisible(false);
	}

	@FXML
	private void handleHide() {

		revealButton.setVisible(true);
	}

	private void updateContent() {

		Card card = iterator.element();
		revealButton.setVisible(true);
		questionLabel.setText(card.getQuestion());
		solutionArea.setText(card.getSolution());
		answerArea.setText("");
	}

	@Override
	public String getHeader() {
		return cardSet.getName();
	}
}
