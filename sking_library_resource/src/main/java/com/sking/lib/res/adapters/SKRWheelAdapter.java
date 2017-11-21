package com.sking.lib.res.adapters;

import java.util.ArrayList;

public class SKRWheelAdapter<T> {

	/** The default items length */
	public static final int DEFAULT_LENGTH = 4;

	// items
	private ArrayList<T> items;
	// length
	private int length;

	/**
	 * Constructor
	 * 
	 * @param items
	 *            the items
	 * @param length
	 *            the max items length
	 */
	public SKRWheelAdapter(ArrayList<T> items, int length) {
		this.items = items;
		this.length = length;
	}

	/**
	 * Contructor
	 * 
	 * @param items
	 *            the items
	 */
	public SKRWheelAdapter(ArrayList<T> items) {
		this(items, DEFAULT_LENGTH);
	}

	public Object getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return "";
	}

	public int getItemsCount() {
		return items.size();
	}

	public int indexOf(Object o) {
		return items.indexOf(o);
	}
}
