package presentation.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DefaultGuiFactory extends GuiFactory {

	@Override
	public JButton createButton(String text) {
		JButton b = new JButton(text);
		b.setFocusPainted(false);
		return b;
	}

	@Override
	public JTextField createTextField() {
		JTextField t = new JTextField();
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
	public <T> JComboBox<T> createComboBox(Collection<T> data, T[] type) {
		JComboBox<T> c = new JComboBox<>(data.toArray(type));
		return c;
	}

	@Override
	public <T> JList<T> createList(Collection<T> data, T[] type) {
		JList<T> l = new JList<>(data.toArray(type));
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
}
