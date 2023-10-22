package logic.util;

import java.util.List;

public class SelectionIterator<E> {

	private List<E> list;
	private int currentIndex;
	private E element;

	public SelectionIterator(List<E> list) {
		this.list = list;
		currentIndex = 0;
		element = list.get(0);
	}

	public boolean hasNext() {
		return list.size() > currentIndex - 1;
	}

	public E next() {
		currentIndex++;
		element = list.get(currentIndex);
		return element;
	}

	public boolean hasPrevious() {
		return currentIndex > 0;
	}

	public E previous() {
		currentIndex--;
		element = list.get(currentIndex);
		return element;
	}

	public E element() {
		return element;
	}
}
