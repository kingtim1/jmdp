package com.github.kingtim1.jmdp.util;

import java.util.AbstractMap;
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
	 * @return the optimal value (or null if the map is empty)
	 */
	public static <K, V extends Number> V optimalValueSearch(Map<K, V> map,
			Optimization opType) {
		Map.Entry<K, V> entry = optimalKeyValueSearch(map, opType);
		if(entry == null){return null;}else{
			return entry.getValue();
		}
	}

	/**
	 * Performs a linear search over the values of a map returning the optimal
	 * key-value pair, where optimal is determined by the value and the
	 * optimization type.
	 * 
	 * @param map a map from arbitrary keys to numerical values
	 * @param opType the optimization type (MINIMIZE or MAXIMIZE)
	 * @return the optimal key-value pair (or null if the map is empty)
	 */
	public static <K, V extends Number> Map.Entry<K, V> optimalKeyValueSearch(
			Map<K, V> map, Optimization opType) {
		V opt = null;
		K optKey = null;
		for (K key : map.keySet()) {
			V val = map.get(key);
			if (opt == null
					|| opType.firstIsBetter(val.doubleValue(),
							opt.doubleValue())) {
				opt = val;
				optKey = key;
			}
		}
		if(optKey == null || opt == null){
			return null;
		}else{
			return new AbstractMap.SimpleEntry<K, V>(optKey, opt);
		}
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
