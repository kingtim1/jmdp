/**
	MapVFunction.java

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

package com.github.kingtim1.jmdp.discounted;

import java.util.HashMap;
import java.util.Map;

/**
 * A value function implemented by a {@link java.util.Map}.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 */
public class MapVFunction<S> implements DiscountedVFunction<S> {

	private Map<S,Double> _values;
	private double _defaultValue;
	
	public MapVFunction(Map<S,Double> values, double defaultValue){
		if(values == null){
			_values = new HashMap<S,Double>();
		}else{
			_values = values;
		}
		_defaultValue = defaultValue;
	}
	
	public MapVFunction(double defaultValue){
		this(new HashMap<S,Double>(), defaultValue);
	}
	
	@Override
	public double value(S state, Integer timestep) {
		return value(state);
	}

	@Override
	public double value(S state) {
		Double v = _values.get(state);
		if(v == null){
			return _defaultValue;
		}else{
			return v.doubleValue();
		}
	}

	/**
	 * Sets the value at a specified state.
	 * @param state a state
	 * @param value the value of the state
	 */
	public void set(S state, double value){
		_values.put(state, value);
	}
}
