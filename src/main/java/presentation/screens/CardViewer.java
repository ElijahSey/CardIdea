package presentation.screens;

import java.util.Collections;
import java.util.List;

import entity.Card;
import entity.CardSet;
import entity.Result;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import logic.util.SelectionIterator;
import presentation.basic.MainFrame;
import presentation.basic.Screen;

public class CardViewer extends Screen {

	private CardSet cardSet;
	private SelectionIterator<Card> iterator;
	private List<Card> cards;
	private Pane[] scoreTiles;

	private static final String FX_BG_COLOR = "-fx-background-color: -fx-color";
	private static final String SELECTED = "selected";

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
		cards = Card.ofSet(cardSet);
		Collections.shuffle(cards);
		iterator = new SelectionIterator<>(cards);
	}

	@Override
	public void initialize() {

		scoreTiles = new Pane[cards.size()];
		for (int i = 0; i < cards.size(); i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPrefWidth(40);
			scoreBar.getColumnConstraints().add(column);
			Pane pane = new Pane();
			pane.getStyleClass().add("progress-tile");
			pane.setStyle(FX_BG_COLOR + Card.DEFAULT);
			scoreBar.addColumn(i, (Node) pane);
			scoreTiles[i] = pane;
		}
		updateContent();
	}

	@FXML
	private void handlePrevious() {

		scoreTiles[iterator.index()].getStyleClass().remove(SELECTED);
		if (iterator.hasPrevious()) {
			iterator.previous();
			updateContent();
		}
	}

	@FXML
	private void handleIncorrect() {

		next(Card.INCORRECT);
	}

	@FXML
	private void handleNeutral() {

		next(Card.NEUTRAL);
	}

	@FXML
	private void handleCorrect() {

		next(Card.CORRECT);
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

	private void next(int score) {

		scoreTiles[iterator.index()].setStyle(FX_BG_COLOR + score);
		scoreTiles[iterator.index()].getStyleClass().remove(SELECTED);
		iterator.element().setScore(score);
		iterator.element().update();
		if (iterator.hasNext()) {
			iterator.next();
			updateContent();
		}
	}

	private void updateContent() {

		Card card = iterator.element();
		scoreTiles[iterator.index()].getStyleClass().add(SELECTED);
		revealButton.setVisible(true);
		questionLabel.setText(card.getQuestion());
		solutionArea.setText(card.getSolution());
		answerArea.setText("");
	}

	@Override
	public boolean beforeClose() {

		int[] counts = new int[4];
		for (Card card : cards) {
			counts[card.getScore()]++;
		}
		new Result(cardSet, counts).persist();
		return super.beforeClose();
	}

	@Override
	public String getHeader() {

		return cardSet.getName();
	}
}
