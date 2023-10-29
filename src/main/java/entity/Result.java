package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import presentation.screens.Menu;
import presentation.util.LanguageManager;

@Entity
public class Result implements Repository {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "cardset_id", referencedColumnName = "id", nullable = false)
	private CardSet cardSet;

	private LocalDateTime createdOn;

	private int defaultCount;

	private int correctCount;

	private int neutralCount;

	private int incorrectCount;

	public Result(CardSet cardSet, int[] counts) {

		this.cardSet = cardSet;

		correctCount = counts[Card.CORRECT];
		neutralCount = counts[Card.NEUTRAL];
		incorrectCount = counts[Card.INCORRECT];
		defaultCount = counts[Card.DEFAULT];
	}

	public Result() {

	}

	public static List<Result> ofSet(CardSet set) {

		if (set.isContained()) {
			List<Result> list = Repository.findEntitiesByForeignKey(Result.class, "cardSet", set);
			return list;
		}
		return new ArrayList<>();
	}

	public static ObservableList<Series<String, Number>> getChartData(CardSet set) {

		ObservableList<Series<String, Number>> data = FXCollections.observableArrayList();

		String[] legend = LanguageManager.getInstance().getArray(Menu.class, "legend");
		List<Result> resultList = ofSet(set);
		for (int i = 0; i < 4; i++) {
			Series<String, Number> series = new Series<>(legend[i], FXCollections.observableArrayList());
			for (Result res : resultList) {
				String time = res.getCreatedOn().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
				series.getData().add(new XYChart.Data<>(time, res.getScore(i)));
			}
			data.add(series);
		}
		return data;
	}

	@Override
	public void updateFields() {

	}

	@Override
	public void persist() {

		EntityTransaction et = em.getTransaction();
		et.begin();
		List<Result> list = ofSet(cardSet);
		if (list.size() >= 10) {
			em.remove(list.get(0));
		}
		em.persist(this);
		et.commit();
	}

	// GETTERS AND SETTERS

	public long getId() {

		return id;
	}

	public CardSet getCardSet() {

		return cardSet;
	}

	public void setCardSet(CardSet cardSet) {

		this.cardSet = cardSet;
	}

	public LocalDateTime getCreatedOn() {

		return createdOn;
	}

	@PrePersist
	protected void prePersist() {

		createdOn = LocalDateTime.now();
	}

	public int getCorrectCount() {

		return correctCount;
	}

	public void setCorrectCount(int correctCount) {

		this.correctCount = correctCount;
	}

	public int getNeutralCount() {

		return neutralCount;
	}

	public void setNeutralCount(int neutralCount) {

		this.neutralCount = neutralCount;
	}

	public int getIncorrectCount() {

		return incorrectCount;
	}

	public void setIncorrectCount(int incorrectCount) {

		this.incorrectCount = incorrectCount;
	}

	public int getDefaultCount() {

		return defaultCount;
	}

	public void setDefaultCount(int ignoredCount) {

		defaultCount = ignoredCount;
	}

	public int getScore(int i) {

		return new int[] { correctCount, neutralCount, incorrectCount, defaultCount }[i];
	}
}
