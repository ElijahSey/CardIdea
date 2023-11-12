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
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logic.parsers.CardParser;
import presentation.basic.AbstractDialog;
import presentation.basic.MainFrame;

public class CardImport extends AbstractDialog {

	private final CardSet cardSet;
	private final ImportAction action;
	private final ObservableList<CardParser> parsers;

	@FXML
	private ListView<CardParser> parserList;

	@FXML
	private TextField pathField;

	public CardImport(CardSet cardSet, ImportAction action) {

		this.cardSet = cardSet;
		this.action = action;

		parsers = FXCollections.observableArrayList(Repository.getParsers());
		parserList.setItems(parsers);

		show(true);
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
				Map<Topic, List<Card>> map = parser.read(file, cardSet);
				action.execute(map);
			} catch (IOException e) {
				new ErrorDialog(e);
			}
		}
		close();
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
		List<CardParser> cp = Repository.getParsers();
		String extension = file.getName().substring(file.getName().lastIndexOf("."));
		cp.removeIf(p -> !p.getExtension().equals(extension));
		parsers.addAll(cp);
		parserList.getSelectionModel().select(0);
	}

	@Override
	public void show(boolean modal) {

		if (browse()) {
			super.show(modal);
		}
	}

	public interface ImportAction {

		void execute(Map<Topic, List<Card>> map);
	}
}
