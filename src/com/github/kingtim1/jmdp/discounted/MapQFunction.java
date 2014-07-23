/**
	MapQFunction.java

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

import com.github.kingtim1.jmdp.ActionSet;
import com.github.kingtim1.jmdp.util.MapUtil;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * An action-value function implemented by a {@link java.util.Map}.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public class MapQFunction<S, A> implements DiscountedQFunction<S, A> {

	private Map<S,Map<A,Double>> _qvals;
	
	private ActionSet<S,A> _actionSet;
	private double _defaultValue;
	private Optimization _opType;
	
	public MapQFunction(ActionSet<S,A> actionSet, double defaultValue, Optimization opType){
		_actionSet = actionSet;
		_defaultValue = defaultValue;
		_opType = opType;
		
		_qvals = new HashMap<S,Map<A,Double>>();
	}
	
	@Override
	public double value(S state, A action, Integer timestep) {
		return value(state, action);
	}

	@Override
	public double greedyValue(S state, Integer timestep) {
		return greedyValue(state);
	}

	@Override
	public A greedyAction(S state, Integer timestep) {
		return greedyAction(state);
	}

	@Override
	public Optimization opType() {
		return _opType;
	}

	@Override
	public double value(S state, A action) {
		Map<A,Double> avals = MapUtil.get2ndMap(_qvals, state);
		double qval = MapUtil.getWithDefault(avals, action, _defaultValue);
		return qval;
	}

	@Override
	public double greedyValue(S state) {
		Map<A,Double> avals = MapUtil.get2ndMap(_qvals, state);
		Double val = MapUtil.optimalValueSearch(avals, _opType);
		if(val == null){
			return _defaultValue;
		}else{
			return val.doubleValue();
		}
	}

	@Override
	public A greedyAction(S state) {
		Map<A,Double> avals = MapUtil.get2ndMap(_qvals, state);
		Map.Entry<A, Double> pair = MapUtil.optimalKeyValueSearch(avals, _opType);
		if(pair == null){
			return _actionSet.actions(state).get(0);
		}else{
			return pair.getKey();
		}
	}

	@Override
	public DiscountedVFunction<S> greedy() {
		return new DiscountedVFunction.GreedyQ<S, A>(this);
	}
	
	/**
	 * Sets the value of a state-action pair.
	 * @param state a state
	 * @param action an action
	 * @param value the value of (state, action)
	 */
	public void set(S state, A action, double value){
		Map<A,Double> avals = MapUtil.get2ndMap(_qvals, state);
		avals.put(action, value);
	}

	@Override
	public A policy(S state) {
		return greedyAction(state);
	}

	@Override
	public double aprob(S state, A action) {
		return policy(state).equals(action)? 1 : 0;
	}

	@Override
	public boolean isDeterministic() {
		return true;
	}

	@Override
	public A policy(S state, Integer timestep) {
		return policy(state);
	}

}
