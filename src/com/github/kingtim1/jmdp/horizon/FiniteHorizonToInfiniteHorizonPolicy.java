/**
	FiniteHorizonToInfiniteHorizonPolicy.java

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

import com.github.kingtim1.jmdp.Policy;

/**
 * Converts a finite-horizon policy into an infinite horizon policy be cycling
 * through the non-stationary policies horizon over and over again. This can be
 * useful because a good finite-horizon policy can be seen as an approximation
 * to an infinite horizon policy.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class FiniteHorizonToInfiniteHorizonPolicy<S, A> implements Policy<S, A> {
	private FiniteHorizonPolicy<S, A> _policy;

	/**
	 * Constructs an infinite horizon policy from a finite-horizon policy.
	 * 
	 * @param policy
	 *            a finite-horizon policy
	 */
	public FiniteHorizonToInfiniteHorizonPolicy(FiniteHorizonPolicy<S, A> policy) {
		_policy = policy;
	}

	@Override
	public A policy(S state, Long timestep) {
		long t = timestep % _policy.horizon();
		return _policy.policy(state, t);
	}

	/**
	 * Returns a reference to the underlying finite-horizon policy.
	 * 
	 * @return a reference to the underlying finite-horizon policy
	 */
	public FiniteHorizonPolicy<S, A> finiteHorizonPolicy() {
		return _policy;
	}

}
