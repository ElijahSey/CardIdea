package presentation.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Card;
import entity.CardSet;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import logic.parsers.CardParser;
import presentation.basic.AbstractDialog;
import presentation.basic.MainFrame;

public class CardExport extends AbstractDialog {

	private CardSet cardSet;

	@FXML
	private ListView<CardParser> parserList;

	public CardExport(CardSet cardSet) {
		super(true);
		this.cardSet = cardSet;
	}

	@Override
	public void initialize() {
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

		Map<String, List<Card>> cards = new HashMap<>();
		for (Card card : dp.getCardsOfSet(cardSet)) {
			String name = card.getTopic().getName();
			if (!cards.containsKey(name)) {
				cards.put(name, new ArrayList<>());
			}
			cards.get(name).add(card);
		}
		try {
			parser.write(file, cardSet, cards);
		} catch (IOException e) {
			new ErrorDialog(e);
		}
	}
}
