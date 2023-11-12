package presentation.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import presentation.basic.MainFrame;
import presentation.util.CustomCellFactory.DragListener;

public class CardTree extends TreeView<Object> {

	public void init(CardSet cardSet, DragListener listener) {

		setCellFactory(new CustomCellFactory(listener));

		TreeItem<Object> root = new TreeItem<>(cardSet);
		root.setExpanded(true);
		ObservableList<TreeItem<Object>> topicList = root.getChildren();
		for (Topic topic : Topic.ofSet(cardSet)) {
			TreeItem<Object> topicItem = new TreeItem<>(topic);
			ObservableList<TreeItem<Object>> cardList = topicItem.getChildren();
			for (Card card : Card.ofTopic(topic)) {
				cardList.add(new TreeItem<>(card));
			}
			topicList.add(topicItem);
		}
		setRoot(root);
	}

	public boolean addTopic(CardSet cardSet) {

		Optional<String> nameOpt = MainFrame.showDialog(new TextInputDialog(), getClass(), "addTopic");
		if (nameOpt.isPresent()) {
			getRoot().getChildren().add(new TreeItem<>(new Topic(nameOpt.get(), cardSet)));
			return true;
		}
		return false;
	}

	public void addCard(TreeItem<Object> selectedItem, String question, String solution, String hint) {

		TreeItem<Object> topicItem = selectedItem;
		if (selectedItem.getValue() instanceof Card) {
			topicItem = selectedItem.getParent();
		}
		Topic topic = (Topic) topicItem.getValue();
		Card card = new Card(topic, question, solution, hint);
		TreeItem<Object> cardItem = addCardItem(topicItem, card);
		getSelectionModel().select(cardItem);
	}

	public boolean editTopic(TreeItem<Object> topicItem) {

		Topic topic = (Topic) topicItem.getValue();
		Optional<String> nameOpt = MainFrame.showDialog(new TextInputDialog(topic.getName()), getClass(), "editTopic");
		if (nameOpt.isPresent()) {
			topic.setName(nameOpt.get());
			refresh();
			return true;
		}
		return false;
	}

	public boolean editCard(TreeItem<Object> cardItem, String question, String solution, String hint) {

		Card card = (Card) cardItem.getValue();
		card.setQuestion(question);
		card.setSolution(solution);
		card.setHint(hint);
		return true;
	}

	public boolean deleteTopic(TreeItem<Object> topic) {

		ObservableList<TreeItem<Object>> topicList = getRoot().getChildren();
		if (topicList.size() > 0) {
			ButtonType type = MainFrame.showAlert(getClass(), "deleteTopic", AlertType.WARNING, ButtonType.OK,
					ButtonType.CANCEL);
			if (!type.getButtonData().equals(ButtonData.OK_DONE)) {
				return false;
			}
		}
		topicList.remove(topic);
		return true;
	}

	public boolean deleteCard(TreeItem<Object> card) {

		card.getParent().getChildren().remove(card);
		return true;
	}

	public void addAll(Map<Topic, List<Card>> map) {

		for (Map.Entry<Topic, List<Card>> entry : map.entrySet()) {
			Topic topic = entry.getKey();
			TreeItem<Object> topicItem = null;
			for (TreeItem<Object> item : getRoot().getChildren()) {
				if (item.getValue().equals(topic)) {
					topicItem = item;
					break;
				}
			}
			if (topicItem == null) {
				topicItem = new TreeItem<>(topic);
			}
			for (Card card : entry.getValue()) {
				addCardItem(topicItem, card);
			}
			if (!getRoot().getChildren().contains(topicItem)) {
				getRoot().getChildren().add(topicItem);
			}
		}
	}

	public LinkedHashMap<Topic, List<Card>> getAll() {

		LinkedHashMap<Topic, List<Card>> map = new LinkedHashMap<>();
		getRoot().getChildren().forEach(topicItem -> {
			List<Card> cards = new LinkedList<>();
			map.put((Topic) topicItem.getValue(), cards);
			topicItem.getChildren().forEach(cardItem -> {
				cards.add((Card) cardItem.getValue());
			});
		});
		return map;
	}

	private TreeItem<Object> addCardItem(TreeItem<Object> topicItem, Card card) {

		TreeItem<Object> cardItem = new TreeItem<>(card);
		topicItem.getChildren().add(cardItem);
		return cardItem;
	}

	@Override
	public void refresh() {

		int index = getSelectionModel().getSelectedIndex();
		getSelectionModel().clearSelection();
		super.refresh();
		getSelectionModel().select(index);
	}
}
