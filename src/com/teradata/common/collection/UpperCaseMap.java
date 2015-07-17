package com.teradata.common.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpperCaseMap extends HashMap implements Serializable, Cloneable, Map {

	private static final long serialVersionUID = 2376697255153294706L;

	public UpperCaseMap() {
	}

	public UpperCaseMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public UpperCaseMap(int initialCapacity) {
		super(initialCapacity);
	}

	public UpperCaseMap(Map m) {
		putAll(m);
	}

	public Object put(Object key, Object value) {
		return super.put(convertKey(key), value);
	}

	public Object clone() {
		return super.clone();
	}

	public String getString(Object key) {
		if (containsKey(key)) {
			Object value = get(key);
			if (value != null) 
				return value.toString();
		}
		return null;
	}
	
	public String getString(Object key, String defaultValue) {
		String value = getString(key);
		return null == value ? defaultValue : value;
	}
	
	public int getInt(Object key, int defaultValue) {
		if (containsKey(key)) {
			Object value = get(key);
			if (value != null)
				try {
					return Integer.parseInt(value.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
		}
		return defaultValue;
	}

	protected Object convertKey(Object key) {
		if (key != null)
			return key.toString().toUpperCase();
		else
			return null;
	}

}