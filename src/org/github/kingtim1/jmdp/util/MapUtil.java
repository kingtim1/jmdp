package org.github.kingtim1.jmdp.util;

import java.util.Map;

/**
 * Utility class for working with maps.
 * @author Timothy A. Mann
 *
 */
public class MapUtil {

	public MapUtil() {
	}
	
	/**
	 * Performs a linear search over the values of a map returning the optimal value.
	 * @param map a map whose values are numeric (the key type is essentially ignored)
	 * @param opType the optimization type (MINIMIZE or MAXIMIZE)
	 * @return the optimal value
	 */
	public static <T> Double linearSearch(Map<T,? extends Number> map, Optimization opType) {
		Double opt = null;
		for(T key : map.keySet()){
			Double val = map.get(key).doubleValue();
			if(opt == null || opType.firstIsBetter(val, opt)){
				opt = val;
			}
		}
		return opt;
	}

}
