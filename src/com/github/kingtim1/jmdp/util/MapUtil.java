package com.github.kingtim1.jmdp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for working with maps.
 * 
 * @author Timothy A. Mann
 *
 */
public class MapUtil {

	public MapUtil() {
	}

	/**
	 * Performs a linear search over the values of a map returning the optimal
	 * value.
	 * 
	 * @param map
	 *            a map whose values are numeric (the key type is essentially
	 *            ignored)
	 * @param opType
	 *            the optimization type (MINIMIZE or MAXIMIZE)
	 * @return the optimal value
	 */
	public static <T> Double linearSearch(Map<T, ? extends Number> map,
			Optimization opType) {
		Double opt = null;
		for (T key : map.keySet()) {
			Double val = map.get(key).doubleValue();
			if (opt == null || opType.firstIsBetter(val, opt)) {
				opt = val;
			}
		}
		return opt;
	}

	/**
	 * Returns the 2nd map specified by a key to the top level map. If the 2nd
	 * map is null, this method creates a new map instance, adds it to the top
	 * level map with the specified key, and returns the new instance.
	 * 
	 * @param map
	 *            a top level map
	 * @param key
	 *            a to the top level map
	 * @return a second level map
	 */
	public static <K1, K2, V> Map<K2, V> get2ndMap(Map<K1, Map<K2, V>> map,
			K1 key) {
		Map<K2, V> innerMap = map.get(key);
		if (innerMap == null) {
			innerMap = new HashMap<K2, V>();
			map.put(key, innerMap);
		}
		return innerMap;
	}

	/**
	 * Increments a map's value by 1 at a specified key. If the key is not
	 * present, then the new value for the key will be set to 1.
	 * 
	 * @param map
	 *            a map from keys to integers
	 * @param key
	 *            a key
	 */
	public static <K> void inc(Map<K, Integer> map, K key) {
		Integer n = map.get(key);
		if (n == null) {
			map.put(key, new Integer(1));
		} else {
			map.put(key, new Integer(n.intValue() + 1));
		}
	}

	/**
	 * Tries to retrieve the value associated with a specified key. If the key
	 * is not associated with a value this function returned the specified
	 * default value.
	 * 
	 * @param map
	 *            a map
	 * @param key
	 *            a key
	 * @param defaultValue
	 *            the default value returned if the key does not map to a value
	 * @return either the value stored in the specified map or the default value
	 */
	public static <K, V> V getWithDefault(Map<K, V> map, K key, V defaultValue) {
		V val = map.get(key);
		if (val == null) {
			return defaultValue;
		} else {
			return val;
		}
	}

}
