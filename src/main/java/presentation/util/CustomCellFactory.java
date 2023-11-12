package presentation.util;

import java.util.Objects;

import entity.Card;
import entity.Topic;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class CustomCellFactory implements Callback<TreeView<Object>, TreeCell<Object>> {

	private TreeCell<Object> dropZone;
	private TreeItem<Object> draggedItem;
	private int originIndex;
	private DragListener listener;

	private static final Image TOPIC_IMAGE = Util.loadImage("topic.png");

	private static final Font DEFAULT_FONT = Font.getDefault();
	private static final Font TOPIC_FONT = Font.font(DEFAULT_FONT.getFamily(), FontWeight.BOLD, DEFAULT_FONT.getSize());

	private static final String LOWER_DROP_HINT_STYLE = "-fx-border-color: #000000; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3";
	private static final String UPPER_DROP_HINT_STYLE = "-fx-border-color: #000000; -fx-border-width: 2 0 0 0; -fx-padding: 1 3 3 3";

	public CustomCellFactory(DragListener listener) {

		this.listener = listener;
	}

	@Override
	public TreeCell<Object> call(TreeView<Object> treeView) {

		TreeCell<Object> cell = new TreeCell<>() {

			@Override
			protected void updateItem(Object item, boolean empty) {

				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item instanceof Topic) {
						setFont(TOPIC_FONT);
						setGraphic(Util.imageViewOf(TOPIC_IMAGE, 10, 10));
					} else {
						setFont(DEFAULT_FONT);
						setGraphic(null);
					}
				}
			}
		};
		cell.setOnDragDetected((MouseEvent event) -> dragDetected(event, cell, treeView));
		cell.setOnDragOver((DragEvent event) -> dragOver(event, cell, treeView));
		cell.setOnDragDropped((DragEvent event) -> drop(event, cell, treeView));
		cell.setOnDragDone((DragEvent event) -> clearDropLocation());

		return cell;
	}

	/**
	 * Decide wether item should be dragged and initiate dragging
	 *
	 * @param event
	 * @param treeCell
	 * @param treeView
	 */
	private void dragDetected(MouseEvent event, TreeCell<Object> treeCell, TreeView<Object> treeView) {

		draggedItem = treeCell.getTreeItem();
		originIndex = treeCell.getIndex();

//		// Topics shouldn't be dragged
//		if (draggedItem.getValue() instanceof Topic) {
//			return;
//		}
		Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

		ClipboardContent content = new ClipboardContent();
		content.putString(draggedItem.getValue().toString());
		db.setContent(content);
		db.setDragView(treeCell.snapshot(null, null));
		event.consume();
	}

	/**
	 * Mark drop location
	 *
	 * @param event
	 * @param treeCell
	 * @param treeView
	 */
	private void dragOver(DragEvent event, TreeCell<Object> treeCell, TreeView<Object> treeView) {

		if (!event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
			return;
		}
		TreeItem<Object> thisItem = treeCell.getTreeItem();

		// ignore of objects arent same type
		if (draggedItem == null || thisItem == null
				|| (draggedItem.getValue() instanceof Topic && !(thisItem.getValue() instanceof Topic))) {
			return;
		}
		event.acceptTransferModes(TransferMode.MOVE);
		if (!Objects.equals(dropZone, treeCell)) {
			clearDropLocation();
			dropZone = treeCell;
			if (dropZone.getIndex() > originIndex
					|| thisItem.getValue() instanceof Topic && draggedItem.getValue() instanceof Card) {
				dropZone.setStyle(LOWER_DROP_HINT_STYLE);
			} else if (dropZone.getIndex() < originIndex) {
				dropZone.setStyle(UPPER_DROP_HINT_STYLE);
			} else {
				dropZone.setStyle("");
			}
		}
	}

	/**
	 * Execute the drop
	 *
	 * @param event
	 * @param treeCell
	 * @param treeView
	 */
	private void drop(DragEvent event, TreeCell<Object> treeCell, TreeView<Object> treeView) {

		Dragboard db = event.getDragboard();
		boolean success = false;
		if (!db.hasContent(DataFormat.PLAIN_TEXT)) {
			return;
		}
		TreeItem<Object> thisItem = treeCell.getTreeItem();
		TreeItem<Object> oldParent = draggedItem.getParent();
		TreeItem<Object> newParent = thisItem.getParent();

		// remove from previous location
		int indexInParent = newParent.getChildren().indexOf(thisItem);
		oldParent.getChildren().remove(draggedItem);

		// if a card is dragged onto a topic cell, make it the first child
		if (draggedItem.getValue() instanceof Card && thisItem.getValue() instanceof Topic) {
			thisItem.getChildren().add(0, draggedItem);
		} else {
			newParent.getChildren().add(indexInParent, draggedItem);
		}
		treeView.getSelectionModel().select(draggedItem);
		event.setDropCompleted(success);
		listener.onDrag();
	}

	private void clearDropLocation() {

		if (dropZone != null) {
			dropZone.setStyle("");
		}
	}

	public interface DragListener {

		void onDrag();
	}
}
