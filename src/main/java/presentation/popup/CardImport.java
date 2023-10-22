package presentation.popup;

import java.util.List;

import javax.swing.JList;
import javax.swing.JTextField;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import logic.parsers.CardParser;

public class CardImport {

	private JList<CardParser> parsers;
	private JTextField path;
	private CardSet cardSet;
	private List<Topic> topics;
	private List<Card> cards;

	public CardImport(CardSet cardSet, List<Topic> topics, List<Card> cards) {
		this.cardSet = cardSet;
		this.topics = topics;
		this.cards = cards;
	}

//	@Override
//	protected JPanel createContent() {
//
//		JPanel panel = new JPanel(new BorderLayout(15, 15));
//
//		JPanel filePanel = new JPanel();
//		path = gui.createTextField();
//		path.setEditable(false);
//		filePanel.add(path);
//		JButton browse = gui.createButton("Browse...");
//		browse.addActionListener(e -> browse());
//		filePanel.add(browse);
//
//		JPanel center = new JPanel(new GridBagLayout());
//		parsers = gui.createList(dp.getParsers(), new CardParser[0]);
//		parsers.setFixedCellWidth(150);
//		JScrollPane scrollPane = gui.createScrollPane(parsers);
//		center.add(scrollPane);
//
//		JPanel buttonPanel = new JPanel();
//		JButton importCards = gui.createButton("Import");
//		importCards.addActionListener(e -> importCards());
//		buttonPanel.add(importCards);
//
//		panel.add(filePanel, BorderLayout.NORTH);
//		panel.add(center, BorderLayout.CENTER);
//		panel.add(buttonPanel, BorderLayout.SOUTH);
//
//		return panel;
//	}
//
//	private boolean browse() {
//		JFileChooser explorer = new JFileChooser();
//		int status = explorer.showOpenDialog(mainPanel);
//		if (status != JFileChooser.APPROVE_OPTION) {
//			return false;
//		}
//		File file = explorer.getSelectedFile();
//		path.setText(file.getPath());
//		selectParsers(file);
//		return true;
//	}
//
//	private void importCards() {
//		if (parsers.isSelectionEmpty()) {
//			parsers.setSelectedIndex(0);
//		}
//		CardParser parser = parsers.getSelectedValue();
//		File file = new File(path.getText());
//		if (file.exists() && file.isFile() && file.canRead()) {
//
//			try {
//				parser.read(file, cardSet, topics, cards);
//			} catch (IOException e) {
//				throw new VisualException(mainPanel, e);
//			}
//			mainPanel.rebuildActiveScreen();
//		}
//	}
//
//	private void selectParsers(File file) {
//		List<CardParser> cp = dp.getParsers();
//		cp.removeIf(p -> !p.getFileFilter().accept(file));
//		parsers.setListData(cp.toArray(new CardParser[0]));
//		if (cp.size() == 1) {
//			parsers.setSelectedIndex(0);
//		}
//	}
//
//	@Override
//	public void show() {
//		super.create();
//		boolean successful = browse();
//		if (successful) {
//			super.setVisible(true);
//		} else {
//			super.close();
//		}
//	}
}
