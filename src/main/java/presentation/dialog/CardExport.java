package presentation.dialog;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import entity.Card;
import entity.CardSet;
import entity.Repository;
import entity.Topic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import logic.parsers.CardParser;
import presentation.basic.AbstractDialog;
import presentation.basic.MainFrame;

public class CardExport extends AbstractDialog {

	private CardSet cardSet;
	private Map<Topic, List<Card>> map;

	private final ObservableList<CardParser> parsers;

	@FXML
	private ListView<CardParser> parserList;

	public CardExport(CardSet cardSet, Map<Topic, List<Card>> map) {

		this.cardSet = cardSet;
		this.map = map;
		parsers = FXCollections.observableArrayList(Repository.getParsers());
		parserList.setItems(parsers);
		show(true);
	}

	@FXML
	private void handleExport() {

		if (parserList.getSelectionModel().isEmpty()) {
			parserList.getSelectionModel().select(0);
		}
		CardParser parser = parserList.getSelectionModel().getSelectedItem();

		FileChooser explorer = new FileChooser();
		explorer.setSelectedExtensionFilter(parser.getFileFilter());
		explorer.setInitialFileName(cardSet.getName() + parser.getExtension());
		File file = explorer.showSaveDialog(MainFrame.getInstance().getWindow());
		if (file == null) {
			return;
		}
		try {
			parser.write(file, cardSet, map);
		} catch (IOException e) {
			new ErrorDialog(e);
		}
		close();
	}
}
