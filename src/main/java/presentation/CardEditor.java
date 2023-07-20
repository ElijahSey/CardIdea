package presentation;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import entity.Card;
import entity.CardSet;

public class CardEditor extends Screen {

	private JTextField name;
	private CardSet cardSet;
	private JList<Card> list;

	public CardEditor(JPanel mainPanel, CardSet cardSet) {
		super(mainPanel);
		if (cardSet == null) {
			this.cardSet = new CardSet();
			this.cardSet.setName("");
			this.cardSet.setCards(new ArrayList<>());
		} else {
			this.cardSet = cardSet;
		}
		addContent();
	}

	@Override
	public JPanel createContent() {
		JPanel panel = new JPanel();
		name = new JTextField(16);
		name.setText(cardSet.getName());

		panel.add(name);

		list = new JList<>(cardSet.getCards().toArray(new Card[] {}));

		JScrollPane pane = new JScrollPane(list);
		panel.add(pane);
		return panel;
	}
}
