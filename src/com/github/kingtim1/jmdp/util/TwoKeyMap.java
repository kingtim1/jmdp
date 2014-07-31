/**
	TwoKeyMap.java

	===================================================================

   Copyright 2014 Timothy A. Mann

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

	===================================================================

	The research leading to these results has received funding from the 
	European Research Council under the European Union's Seventh Framework 
	Programme (FP/2007-2013) / ERC Grant Agreement n.306638.

 */

package com.github.kingtim1.jmdp.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A map from two different keys to a value.
 * 
 * @author Timothy A. Mann
 *
 * @param <K1>
 *            the first key type
 * @param <K2>
 *            the section key type
 * @param <V>
 *            the value type
 */
public class TwoKeyMap<K1, K2, V> {
	private Map<K1, Map<K2, V>> _map;
	private Set<K2> _secondKeySet;

	public TwoKeyMap() {
		_map = new HashMap<K1, Map<K2, V>>();
		_secondKeySet = new HashSet<K2>();
	}

	/**
	 * Clears all of the elements stored in this map.
	 */
	public void clear() {
		_map.clear();
		_secondKeySet.clear();
	}

	public V get(K1 key1, K2 key2) {
		Map<K2, V> vmap = MapUtil.getValueMap(_map, key1);
		return vmap.get(key2);
	}

	public V getWithDefault(K1 key1, K2 key2, V defaultValue) {
		V val = get(key1, key2);
		if (val == null) {
			return defaultValue;
		} else {
			return val;
		}
	}

	public void put(K1 key1, K2 key2, V value) {
		Map<K2, V> vmap = MapUtil.getValueMap(_map, key1);
		_secondKeySet.add(key2);
		vmap.put(key2, value);
	}

	public Set<K1> firstKeySet() {
		return _map.keySet();
	}

	public Set<K2> secondKeySet() {
		return _secondKeySet;
	}
}
