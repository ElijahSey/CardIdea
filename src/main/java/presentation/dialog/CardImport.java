package presentation.dialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logic.parsers.CardParser;
import presentation.basic.AbstractDialog;
import presentation.basic.MainFrame;

public class CardImport extends AbstractDialog {

	private final CardSet cardSet;
	private final List<Topic> topics;
	private final List<Card> cards;
	private final ObservableList<CardParser> parsers;

	@FXML
	private ListView<CardParser> parserList;

	@FXML
	private TextField pathField;

	public CardImport(CardSet cardSet, List<Topic> topics, List<Card> cards) {
		super(true);
		this.cardSet = cardSet;
		this.topics = topics;
		this.cards = cards;

		parsers = FXCollections.observableArrayList(dp.getParsers());
	}

	@Override
	public void initialize() {

		parserList.setItems(parsers);
	}

	@FXML
	private void handleBrowse() {
		browse();
	}

	@FXML
	private void handleImport() {
		CardParser parser = parserList.getSelectionModel().getSelectedItem();
		File file = new File(pathField.getText());
		if (file.exists() && file.isFile() && file.canRead()) {
			try {
				parser.read(file, cardSet, topics, cards);
			} catch (IOException e) {
				new ErrorDialog(e);
			}
		}
	}

	private boolean browse() {
		FileChooser explorer = new FileChooser();
		File file = explorer.showOpenDialog(MainFrame.getInstance().getWindow());
		if (file == null) {
			return false;
		}
		pathField.setText(file.getPath());
		selectParsers(file);
		return true;
	}

	private void selectParsers(File file) {
		parsers.clear();
		List<CardParser> cp = dp.getParsers();
		String extension = file.getName().substring(file.getName().lastIndexOf("."));
		cp.removeIf(p -> !p.getExtension().equals(extension));
		parsers.addAll(cp);
		parserList.getSelectionModel().select(0);
	}

	@Override
	public void beforeDisplay() {
		if (browse()) {
			// open
		} else {
			// close
		}
	}
}
