package presentation.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class VisualException extends RuntimeException {

	public VisualException(Component parent, Exception e) {
		this(parent, e.getMessage());
	}

	public VisualException(Component parent, String message) {
		super(message);
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
