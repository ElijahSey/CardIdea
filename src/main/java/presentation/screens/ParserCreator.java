package presentation.screens;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logic.parsers.GenericParser;
import presentation.basic.ContentPanel;
import presentation.basic.Screen;

@Deprecated
public class ParserCreator extends Screen {

	private JTextField name;
	private JTextArea exampleString;

	public ParserCreator(ContentPanel mainPanel) {
		super(mainPanel);
	}

	@Override
	protected JPanel createContent() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));

		name = new JTextField();
		panel.add(name, BorderLayout.NORTH);

		exampleString = new JTextArea();
		panel.add(exampleString, BorderLayout.CENTER);

		panel.add(createButtonPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		JButton save = gui.createButton("Save");
		save.addActionListener(e -> save());
		panel.add(save);
		return panel;
	}

	public void save() {
		dp.addParser(new GenericParser(name.getText(), exampleString.getText(), dp));
		mainPanel.back();
		mainPanel.refreshScreen();
	}

	@Override
	protected String getHeader() {
		return "Create Parser";
	}

}
