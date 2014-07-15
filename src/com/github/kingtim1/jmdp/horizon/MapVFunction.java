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

package com.github.kingtim1.jmdp.horizon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.kingtim1.jmdp.VFunction;

/**
 * A value function for a finite-state, finite-horizon MDP implemented by a
 * {@link java.util.Map} structure.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 */
public class MapVFunction<S> implements VFunction<S> {

	private List<Map<S, Double>> _values;
	private double _defaultValue;
	private int _horizon;

	/**
	 * Constructs a MapVFunction instance with a specified horizon. All states
	 * at all timesteps are initially assigned the default value.
	 * 
	 * @param horizon
	 *            a positive integer determining the horizon
	 * @param defaultValue
	 *            the default value for all states and timesteps
	 */
	public MapVFunction(int horizon, double defaultValue) {
		if (horizon < 1) {
			throw new IllegalArgumentException(
					"Expected positive integer for 'horizon'. Found : "
							+ horizon + ".");
		}
		_horizon = horizon;
		_defaultValue = defaultValue;

		_values = new ArrayList<Map<S, Double>>(horizon);
		for (int t = 0; t < horizon; t++) {
			Map<S, Double> vmap = new HashMap<S, Double>();
			_values.add(vmap);
		}
	}

	/**
	 * Returns the horizon of this value function.
	 * 
	 * @return the horizon
	 */
	public int horizon() {
		return _horizon;
	}

	@Override
	public double value(S state, Integer timestep) {
		try {
			Map<S, Double> vmap = _values.get(timestep.intValue());
			Double v = vmap.get(state);
			if (v == null) {
				return _defaultValue;
			} else {
				return v.doubleValue();
			}
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Invalid timestep " + timestep
					+ ". Valid timesteps are integers in [0, "
					+ (horizon() - 1) + "].");
		}
	}

	/**
	 * Sets the value for a state at a specified timestep.
	 * 
	 * @param state
	 *            a state
	 * @param timestep
	 *            a timestep within the horizon
	 * @param value
	 *            the value of the state at the specified timestep
	 */
	public void set(S state, Integer timestep, double value) {
		try {
			Map<S, Double> vmap = _values.get(timestep.intValue());
			vmap.put(state, value);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("Invalid timestep " + timestep
					+ ". Valid timesteps are integers in [0, "
					+ (horizon() - 1) + "].");
		}
	}
}
