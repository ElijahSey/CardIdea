package presentation.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class VisualException extends RuntimeException {

	public VisualException(Component parent, Exception e) {
		super(e);
		show(parent, e.getMessage());
	}

	public VisualException(Component parent, String message) {
		super(message);
		show(parent, message);
	}

	public void show(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
