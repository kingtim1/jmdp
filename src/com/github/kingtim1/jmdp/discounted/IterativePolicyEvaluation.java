/**
	IterativePolicyEvaluation.java

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

import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * An iterative version of the policy evaluation algorithm using asynchronous
 * backups.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class IterativePolicyEvaluation<S, A> implements com.github.kingtim1.jmdp.PolicyEvaluation<S,A,StationaryPolicy<S,A>, DiscountedVFunction<S>> {

	private FiniteStateSMDP<S, A> _smdp;
	private DiscountFactor _df;
	private int _maxIterations;
	private double _theta;

	public IterativePolicyEvaluation(FiniteStateSMDP<S, A> smdp,
			DiscountFactor df,
			int maxIterations, double convergenceThreshold) {
		_smdp = smdp;
		_df = df;

		_maxIterations = maxIterations;
		_theta = convergenceThreshold;
	}

	/**
	 * Performs a single Bellman backup according to the dynamics of the SMDP and
	 * stationary policy.
	 * 
	 * @param policy a stationary policy
	 * @param state
	 *            a state
	 * @param vfunc
	 *            the current estimate of the value function
	 * @return the resulting Bellman backup
	 */
	private double backup(StationaryPolicy<S,A> policy, S state, DiscountedVFunction<S> vfunc) {
		return rPi(policy, state) + avgNextV(policy, state, vfunc);
	}

	private double avgNextV(StationaryPolicy<S,A> policy, S state, DiscountedVFunction<S> vfunc) {
		if (policy.isDeterministic()) {
			A action = policy.policy(state);
			return FiniteStateSMDP.avgNextV(_smdp, state, action, vfunc, _df);
		} else {
			double avgV = 0;
			Iterable<A> actions = _smdp.actions(state);
			for (A action : actions) {
				double aprob = policy.aprob(state, action);
				avgV += aprob * FiniteStateSMDP.avgNextV(_smdp, state, action, vfunc, _df);
			}
			return avgV;
		}
	}

	/**
	 * Returns the expected reinforcement at a specified state given the
	 * distribution over actions selected by the policy.
	 * 
	 * @param policy stationary policy
	 * @param state
	 *            a state
	 * @return the expected reinforcement at the specified state
	 */
	private double rPi(StationaryPolicy<S,A> policy, S state) {
		if (policy.isDeterministic()) {
			A action = policy.policy(state);
			return FiniteStateSMDP.avgR(_smdp, state, action);
		} else {
			double ravg = 0;
			Iterable<A> actions = _smdp.actions(state);
			for (A action : actions) {
				double aprob = policy.aprob(state, action);
				ravg += aprob * FiniteStateSMDP.avgR(_smdp, state, action);
			}
			return ravg;
		}
	}

	@Override
	public DiscountedVFunction<S> eval(StationaryPolicy<S, A> policy) {
		MapVFunction<S> vfunc = new MapVFunction<S>(0);

		for (int i = 0; i < _maxIterations; i++) {
			double delta = 0;
			Iterable<S> states = _smdp.states();
			for (S state : states) {
				double oldV = vfunc.value(state);
				double newV = backup(policy, state, vfunc);
				vfunc.set(state, newV);
				delta = Math.max(delta, Math.abs(oldV - newV));
			}

			if (delta < _theta) {
				break;
			}
		}

		return vfunc;
	}


}
