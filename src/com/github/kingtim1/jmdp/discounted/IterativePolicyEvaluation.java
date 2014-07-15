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

import com.github.kingtim1.jmdp.DP;
import com.github.kingtim1.jmdp.FiniteStateMDP;
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
public class IterativePolicyEvaluation<S, A> implements
		DP<DiscountedVFunction<S>> {

	private FiniteStateMDP<S, A> _mdp;
	private DiscountFactor _df;
	private StationaryPolicy<S, A> _policy;
	private int _maxIterations;
	private double _theta;

	public IterativePolicyEvaluation(FiniteStateMDP<S, A> mdp,
			DiscountFactor df, StationaryPolicy<S, A> policy,
			int maxIterations, double convergenceThreshold) {
		_mdp = mdp;
		_df = df;
		_policy = policy;

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
	 * Performs a single Bellman backup according to the dynamics of the MDP and
	 * stationary policy.
	 * 
	 * @param state
	 *            a state
	 * @param vfunc
	 *            the current estimate of the value function
	 * @return the resulting Bellman backup
	 */
	private double backup(S state, DiscountedVFunction<S> vfunc) {
		return rPi(state) + _df.doubleValue() * avgNextV(state, vfunc);
	}

	private double avgNextV(S state, DiscountedVFunction<S> vfunc) {
		if (_policy.isDeterministic()) {
			A action = _policy.policy(state);
			return avgNextV(state, action, vfunc);
		} else {
			double avgV = 0;
			Iterable<A> actions = _mdp.actions(state);
			for (A action : actions) {
				double aprob = _policy.aprob(state, action);
				avgV += aprob * avgNextV(state, action, vfunc);
			}
			return avgV;
		}
	}

	private double avgNextV(S state, A action, DiscountedVFunction<S> vfunc) {
		double avgV = 0;
		Iterable<S> nextStates = _mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = _mdp.tprob(state, action, nextState);
			double v = vfunc.value(nextState);
			avgV += tprob * v;
		}
		return avgV;
	}

	private double rPi(S state) {
		if (_policy.isDeterministic()) {
			A action = _policy.policy(state);
			return rPi(state, action);
		} else {
			double ravg = 0;
			Iterable<A> actions = _mdp.actions(state);
			for (A action : actions) {
				double aprob = _policy.aprob(state, action);
				ravg += aprob * rPi(state, action);
			}
			return ravg;
		}
	}

	private double rPi(S state, A action) {
		double ravg = 0;
		Iterable<S> nextStates = _mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = _mdp.tprob(state, action, nextState);
			double r = _mdp.r(state, action, nextState);
			ravg += tprob * r;
		}
		return ravg;
	}
}
