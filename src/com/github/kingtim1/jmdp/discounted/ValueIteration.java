/**
	ValueIteration.java

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

import com.github.kingtim1.jmdp.DP;
import com.github.kingtim1.jmdp.FiniteStateMDP;

/**
 * Implements the classic Value Iteration (VI) algorithm with asynchronous
 * updates. VI starts with an arbitrary estimate of the optimal value function
 * and converges to the optimal value function as the number of iterations goes
 * to infinity.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class ValueIteration<S, A> implements DP<DiscountedVFunction<S>> {

	private FiniteStateMDP<S, A> _mdp;
	private DiscountFactor _df;
	private int _maxIterations;
	private double _theta;

	public ValueIteration(FiniteStateMDP<S, A> mdp, DiscountFactor df,
			int maxIterations, double convergenceThreshold) {
		_mdp = mdp;
		_df = df;
		_maxIterations = maxIterations;
		_theta = convergenceThreshold;
	}

	@Override
	public DiscountedVFunction<S> run() {
		MapVFunction<S> vfunc = new MapVFunction<S>(0);

		for (int i = 0; i < _maxIterations; i++) {
			double delta = 0;
			Iterable<S> states = _mdp.states();
			for (S state : states) {
				double oldV = vfunc.value(state);
				double newV = backup(state, vfunc);
				vfunc.set(state, newV);
				delta = Math.max(delta, Math.abs(oldV - newV));
			}

			if (delta < _theta) {
				break;
			}
		}

		return vfunc;
	}

	/**
	 * Returns the greedy Bellman backup operator for a specified state.
	 * 
	 * @param state
	 *            a state
	 * @param vfunc
	 *            an estimate of the optimal value function
	 * @return a new estimate of the optimal value of state
	 */
	public double backup(S state, DiscountedVFunction<S> vfunc) {
		Double bestV = null;
		Iterable<A> actions = _mdp.actions(state);
		for (A action : actions) {
			double val = FiniteStateMDP.avgR(_mdp, state, action)
					+ _df.doubleValue()
					* FiniteStateMDP.avgNextV(_mdp, state, action, vfunc);
			if (bestV == null || _mdp.opType().firstIsBetter(val, bestV)) {
				bestV = val;
			}
		}
		return bestV.doubleValue();
	}

}
