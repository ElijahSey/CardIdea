package logic.parsers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class CardParser {

	private String name;

	protected ExtensionFilter fileFilter;

	public CardParser(String name) {

		this.name = name;
		fileFilter = createFileFilter();
	}

	public abstract Map<Topic, List<Card>> read(File file, CardSet cardSet) throws IOException;

	public abstract void write(File file, CardSet cardSet, Map<Topic, List<Card>> cards) throws IOException;

	public abstract CardParser newInstance();

	protected abstract ExtensionFilter createFileFilter();

	public ExtensionFilter getFileFilter() {

		return fileFilter;
	}

	public String getExtension() {

		return "." + fileFilter.getExtensions().get(0);
	}

	@Override
	public String toString() {

		return name;
	}
}
