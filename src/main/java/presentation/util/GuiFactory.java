package presentation.util;

import java.awt.Component;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public abstract class GuiFactory {

	private static GuiFactory instance;

	public JButton createButton() {
		return createButton("");
	}

	public abstract JButton createButton(String text);

	public abstract JTextField createTextField();

	public abstract JTextArea createTextArea();

	public <T> JComboBox<T> createComboBox(Collection<T> data, T[] type) {
		return createComboBox(data, type, Object::toString);
	}

	public abstract <T> JComboBox<T> createComboBox(Collection<T> data, T[] type, Label<T> label);

	public <T> JList<T> createList(Collection<T> data, T[] type) {
		return createList(data, type, Object::toString);
	}

	public abstract <T> JList<T> createList(Collection<T> data, T[] type, Label<T> label);

	public abstract JScrollPane createScrollPane(Component view);

	public abstract JSplitPane createSplitPane(int orientation, Component c1, Component c2);

	public abstract JFileChooser createFileChooser();

	public abstract JMenu createMenu(String text);

	public abstract JMenuItem createMenuItem(String text);

	public abstract JSeparator createSeparator();

	public static GuiFactory createDefaultGuiFactory() {
		instance = new DefaultGuiFactory();
		return instance;
	}

	public static GuiFactory getInstance() {
		if (instance == null) {
			return createDefaultGuiFactory();
		}
		return instance;
	}

	protected static Border createInsetBorder(Insets size) {
		return BorderFactory.createEmptyBorder(size.top, size.left, size.bottom, size.right);
	}

	public interface Label<E> {
		String toString(E element);
	}
}
