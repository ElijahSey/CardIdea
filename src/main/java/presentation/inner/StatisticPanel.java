package presentation.inner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import entity.Card;
import entity.CardSet;
import presentation.basic.IntegratedPanel;

public class StatisticPanel extends IntegratedPanel {

	private List<Card> cards;

	private int height;

	private static final Color WRONG_COLOR = Color.RED;
	private static final Color SKIP_COLOR = Color.YELLOW;
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Color CORRECT_COLOR = Color.GREEN;
	private static final Color BORDER_COLOR = Color.LIGHT_GRAY;

	public StatisticPanel(CardSet set, int height) {
		super();
		this.height = height;
		if (set != null) {
			cards = dp.getCardsOfSet(set);
		} else {
			cards = List.of(new Card());
		}
		super.reload();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		double width = (g2d.getClipBounds().getWidth() - 1) / cards.size();
		double height = g2d.getClipBounds().getHeight() - 1;
		int i = 0;
		for (Card card : cards) {
			paintCard(g2d, card, 0 + i++ * width, 0, width, height);
		}
	}

	private void paintCard(Graphics2D g, Card card, double x, double y, double width, double height) {

		switch (card.getScore()) {
		case Card.INCORRECT:
			g.setColor(WRONG_COLOR);
			break;
		case Card.NEUTRAL:
			g.setColor(SKIP_COLOR);
			break;
		case Card.CORRECT:
			g.setColor(CORRECT_COLOR);
			break;
		default:
			g.setColor(DEFAULT_COLOR);
			break;
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		g.fill(rect);
		g.setColor(BORDER_COLOR);
		g.setStroke(new BasicStroke(0.01f));
		g.draw(rect);
	}

	@Override
	public int getHeight() {
		return height;
	}
}
