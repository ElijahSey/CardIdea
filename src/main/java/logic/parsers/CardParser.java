package logic.parsers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Card;
import entity.CardSet;
import entity.Topic;

public abstract class CardParser {

	private String name;

	protected FileNameExtensionFilter fileFilter;

	public CardParser(String name) {
		this.name = name;
		fileFilter = createFileFilter();
	}

	public abstract void read(File file, CardSet cardSet, List<Topic> topics, List<Card> cards) throws IOException;

	public abstract void write(File file, CardSet cardSet, Map<String, List<Card>> cards) throws IOException;

	public abstract CardParser newInstance();

	protected abstract FileNameExtensionFilter createFileFilter();

	public FileFilter getFileFilter() {
		return fileFilter;
	}

	public String getExtension() {
		return "." + fileFilter.getExtensions()[0];
	}

	@Override
	public String toString() {
		return name;
	}
}
