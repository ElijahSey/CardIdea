package presentation.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class DefaultGuiFactory extends GuiFactory {

	@Override
	public JButton createButton(String text) {
		JButton b = new JButton(text);
		b.setFocusPainted(false);
		return b;
	}

	@Override
	public JTextField createTextField() {
		JTextField t = new JTextField(16);
		t.setMinimumSize(new Dimension(50, 20));
		return t;
	}

	@Override
	public JTextArea createTextArea() {
		JTextArea t = new JTextArea();
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		t.setMinimumSize(new Dimension(50, 20));
		return t;
	}

	@Override
	public <T> JComboBox<T> createComboBox(Collection<T> data, T[] type, Label<T> label) {
		JComboBox<T> c = new JComboBox<>(data.toArray(type));
		c.setRenderer(getRenderer(label));
		return c;
	}

	@Override
	public <T> JList<T> createList(Collection<T> data, T[] type, Label<T> label) {
		JList<T> l = new JList<>(data.toArray(type));
		l.setCellRenderer(getRenderer(label));
		return l;
	}

	@Override
	public JScrollPane createScrollPane(Component view) {
		JScrollPane s = new JScrollPane(view);
		return s;
	}

	@Override
	public JSplitPane createSplitPane(int orientation, Component c1, Component c2) {
		JSplitPane s = new JSplitPane(orientation, c1, c2);
		return s;
	}

	@Override
	public JFileChooser createFileChooser() {
		return new JFileChooser();
	}

	@Override
	public JMenu createMenu(String text) {
		return new JMenu(text);
	}

	@Override
	public JMenuItem createMenuItem(String text) {
		return new JMenuItem(text);
	}

	@Override
	public JSeparator createSeparator() {
		return new JSeparator();
	}

	private <T> ListCellRenderer<T> getRenderer(Label<T> label) {
		return (list, value, index, isSelected, cellHasFocus) -> {
			JLabel l = new JLabel(label.toString(value));
			l.addMouseListener(new HoverListener(Color.WHITE, Color.LIGHT_GRAY));
			l.setOpaque(true);
			if (isSelected) {
				l.setForeground(Color.WHITE);
				l.setBackground(Color.GRAY);
			} else {
				l.setForeground(Color.BLACK);
				l.setBackground(Color.WHITE);
			}
			return l;
		};
	}

	private class HoverListener implements MouseListener {

		private Color defaultBackground;
		private Color hoverBackground;

		public HoverListener(Color defaultColor, Color hoverColor) {
			defaultBackground = defaultColor;
			hoverBackground = hoverColor;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			e.getComponent().setBackground(hoverBackground);
			System.out.println("we");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			e.getComponent().setBackground(defaultBackground);
		}
	}
}
