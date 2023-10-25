package presentation.screens;

import java.util.Collections;
import java.util.List;

import entity.Card;
import entity.CardSet;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.util.SelectionIterator;
import presentation.basic.MainFrame;
import presentation.basic.Screen;

public class CardViewer extends Screen {

	private CardSet cardSet;
	private SelectionIterator<Card> iterator;
	private int size;
	private Pane[] scoreTiles;

	private static final Color DEFAULT_COLOR = Color.valueOf("#E0E0E0");
	private static final Color INCORRECT_COLOR = Color.valueOf("#FF3333");
	private static final Color NEUTRAL_COLOR = Color.valueOf("#F0F033");
	private static final Color CORRECT_COLOR = Color.valueOf("#44FF44");

	@FXML
	private Label questionLabel;

	@FXML
	private GridPane scoreBar;

	@FXML
	private TextArea answerArea, solutionArea;

	@FXML
	private Button revealButton;

	public CardViewer(CardSet set) {

		cardSet = set;
		List<Card> cards = dp.getCardsOfSet(cardSet);
		size = cards.size();
		Collections.shuffle(cards);
		iterator = new SelectionIterator<>(cards);
	}

	@Override
	public void initialize() {

		scoreTiles = new Pane[size];
		for (int i = 0; i < size; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPrefWidth(40);
			scoreBar.getColumnConstraints().add(column);
			Pane pane = new Pane();
			pane.setBackground(Background.fill(DEFAULT_COLOR));
			pane.getStyleClass().add("progress-tile");
			scoreBar.addColumn(i, (Node) pane);
			scoreTiles[i] = pane;
		}
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
	private void handleIncorrect() {

		next(Card.INCORRECT, INCORRECT_COLOR);
	}

	@FXML
	private void handleNeutral() {

		next(Card.NEUTRAL, NEUTRAL_COLOR);
	}

	@FXML
	private void handleCorrect() {

		next(Card.CORRECT, CORRECT_COLOR);
	}

	@FXML
	private void handleHint() {

		Alert alert = new Alert(AlertType.INFORMATION);
		MainFrame.setDialogIcon(alert);
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

	private void next(int score, Color color) {

		scoreTiles[iterator.index()].setBackground(Background.fill(color));
		iterator.element().setScore(score);
		dp.update(iterator.element());
		if (iterator.hasNext()) {
			iterator.next();
			updateContent();
		}
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
