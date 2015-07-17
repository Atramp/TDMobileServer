package com.teradata.common.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LowerCaseMap extends HashMap implements Serializable, Cloneable, Map {

	private static final long serialVersionUID = 2376697255153294706L;

	public LowerCaseMap() {
	}

	public LowerCaseMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public LowerCaseMap(int initialCapacity) {
		super(initialCapacity);
	}

	public LowerCaseMap(Map m) {
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

	protected Object convertKey(Object key) {
		if (key != null)
			return key.toString().toLowerCase();
		else
			return null;
	}

}