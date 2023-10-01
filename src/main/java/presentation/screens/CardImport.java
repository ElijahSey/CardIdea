package presentation.screens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import logic.parsers.CardParser;
import presentation.basic.ContentPanel;
import presentation.basic.Screen;

public class CardImport extends Screen {

	private JList<CardParser> parsers;
	private JTextField path;
	private CardSet cardSet;
	private List<Topic> topics;
	private List<Card> cards;

	public CardImport(ContentPanel mainPanel, CardSet cardSet, List<Topic> topics, List<Card> cards) {
		super(mainPanel);
		this.cardSet = cardSet;
		this.topics = topics;
		this.cards = cards;
	}

	@Override
	protected JPanel createContent() {

		JPanel panel = new JPanel();
		parsers = gui.createList(dp.getParsers(), new CardParser[0]);
		parsers.setFixedCellWidth(150);
		JScrollPane scrollPane = gui.createScrollPane(parsers);
		panel.add(scrollPane);
//		JButton newParser = gui.createButton("New");
//		newParser.addActionListener(e -> mainPanel.addScreen(new ParserCreator(mainPanel)));
//		panel.add(newParser);
		JButton importCards = gui.createButton("Import");
		importCards.addActionListener(e -> importCards());
		panel.add(importCards);

		JPanel filePanel = new JPanel();
		path = gui.createTextField();
		filePanel.add(path);
		JButton browse = gui.createButton("Browse...");
		browse.addActionListener(e -> browse());
		filePanel.add(browse);
		panel.add(filePanel);
		return panel;
	}

	private void browse() {
		JFileChooser explorer = new JFileChooser();
		int status = explorer.showOpenDialog(mainPanel);
		if (status != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = explorer.getSelectedFile();
		if (file != null) {
			path.setText(file.getPath());
		}
	}

	private void importCards() {
		CardParser parser = parsers.getSelectedValue();
		if (parser == null) {
			return;
		}
		File file = new File(path.getText());
		if (file.exists() && file.isFile() && file.canRead()) {

			try {
				parser.parse(Files.readString(file.toPath()), cardSet);
				topics.addAll(parser.getTopics());
				cards.addAll(parser.getCards());
			} catch (IOException e) {
				e.printStackTrace();
			}
			mainPanel.back();
			mainPanel.rebuildActiveScreen();
		}
	}
}
