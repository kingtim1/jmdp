/**
	FourKeyMap.java

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
 * A map from four keys to a value.
 * @author Timothy A. Mann
 *
 * @param <K1> the first key type
 * @param <K2> the second key type
 * @param <K3> the third key type
 * @param <K4> the fourth key type
 * @param <V> the value type
 */
public class FourKeyMap<K1, K2, K3, K4, V> {
	private ThreeKeyMap<K1, K2, K3, Map<K4,V>> _map;
	private Set<K4> _fourthKeySet;
	
	public FourKeyMap(){
		_map = new ThreeKeyMap<K1,K2,K3,Map<K4,V>>();
		_fourthKeySet = new HashSet<K4>();
	}
	
	public void clear(){
		_map.clear();
		_fourthKeySet.clear();
	}
	
	public V get(K1 key1, K2 key2, K3 key3, K4 key4){
		Map<K4,V> k4map = _map.get(key1, key2, key3);
		if(k4map == null){
			return null;
		}else{
			return k4map.get(key4);
		}
	}
	
	public V getWithDefault(K1 key1, K2 key2, K3 key3, K4 key4, V defaultValue){
		V val = get(key1, key2, key3, key4);
		if(val == null){
			return defaultValue;
		}else{
			return val;
		}
	}
	
	public void put(K1 key1, K2 key2, K3 key3, K4 key4, V value){
		Map<K4,V> k4map = _map.get(key1, key2, key3);
		if(k4map == null){
			k4map = new HashMap<K4,V>();
		}
		k4map.put(key4, value);
		_fourthKeySet.add(key4);
		_map.put(key1, key2, key3, k4map);
	}
	
	public Set<K1> firstKeySet(){return _map.firstKeySet();}
	public Set<K2> secondKeySet(){return _map.secondKeySet();}
	public Set<K3> thirdKeySet(){return _map.thirdKeySet();}
	public Set<K4> fourthKeySet(){return new HashSet<K4>(_fourthKeySet);}
}
