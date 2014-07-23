/**
	PolicyIteration.java

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

import com.github.kingtim1.jmdp.AbstractPolicyIteration;
import com.github.kingtim1.jmdp.ActionSet;
import com.github.kingtim1.jmdp.DeterministicPolicy;
import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * The classic Policy Iteration algorithm for finite-state, finite-action SMDPs.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class PolicyIteration<S, A>
		extends
		AbstractPolicyIteration<S, A, StationaryPolicy<S, A>, DiscountedVFunction<S>> {

	private FiniteStateSMDP<S, A> _smdp;
	private int _maxIterations;

	private StationaryPolicy<S, A> _lastPolicy;

	/**
	 * Constructs an instance of Policy Iteration for an SMDP with a maximum
	 * number of iterations.
	 * 
	 * @param smdp
	 *            an SMDP model
	 * @param df
	 *            the discount factor to use (can be 1)
	 * @param maxIterations
	 *            the maximum number of iterations to run. If maxIterations is
	 *            non-positive, then this argument is ignored and the algorithm
	 *            will run until the policy stops changing.
	 */
	public PolicyIteration(FiniteStateSMDP<S, A> smdp, DiscountFactor df,
			int maxIterations) {
		super(new MatrixInversePolicyEvaluation<S, A>(smdp, df),
				new StationaryPolicyImprovement<S, A>(smdp, df));
		_smdp = smdp;
		_maxIterations = maxIterations;
	}

	@Override
	public boolean isFinished(StationaryPolicy<S, A> policy,
			DiscountedVFunction<S> policyValue, int iteration) {
		if (_maxIterations > 0 && iteration >= _maxIterations) {
			_lastPolicy = policy;
			return true;
		}

		if (_lastPolicy != null) {
			boolean policyChanged = false;
			Iterable<S> states = _smdp.states();
			for (S state : states) {
				if (!_lastPolicy.policy(state).equals(policy.policy(state))) {
					policyChanged = true;
					break;
				}
			}

			_lastPolicy = policy;
			return !policyChanged;
		} else {
			_lastPolicy = policy;
			return false;
		}

	}

	@Override
	public DeterministicPolicy<S, A> initialPolicy() {
		Map<S, A> map = new HashMap<S, A>();
		ActionSet<S, A> actionSet = _smdp.actionSet();
		Iterable<S> states = _smdp.states();
		for (S state : states) {
			map.put(state, actionSet.uniformRandom(state));
		}
		return new MapPolicy<S, A>(map);
	}

}
