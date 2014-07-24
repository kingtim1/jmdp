/**
	SequenceOfStationaryPolicies.java

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
import java.util.Collection;
import java.util.List;

import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * Converts a sequence of stationary policies into a {@link FiniteHorizonPolicy}
 * . Each policy in the sequence is queried once to obtain an action in the
 * order of the sequence. The policy with index 0 is queried first, followed by
 * the policy with index 1, etc. The horizon of this policy is effectively the
 * number of policies in the sequence. However, here horizon refers to the
 * number of decisions (not the number of timesteps).
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class SequenceOfStationaryPolicies<S, A> implements
		FiniteHorizonPolicy<S, A> {

	private List<StationaryPolicy<S, A>> _policySeq;

	/**
	 * Constructs a sequence of stationary policies given a collection of
	 * stationary policies. The order of the sequence is determined by the
	 * iterator returned by {@link Iterable#iterator()}. If you want a specific
	 * ordering it is recommended that you pass an instance that implements
	 * {@link List}.
	 * 
	 * @param policySeq
	 *            a sequence of policies
	 */
	public SequenceOfStationaryPolicies(
			Collection<? extends StationaryPolicy<S, A>> policySeq) {
		_policySeq = new ArrayList<StationaryPolicy<S, A>>(policySeq);
	}

	/**
	 * Constructs a sequence of stationary policies given one or more policies.
	 * The sequence of policies will have the same order as the order of the
	 * arguments to this constructor method.
	 * 
	 * @param policies a sequence of policies
	 */
	public SequenceOfStationaryPolicies(StationaryPolicy<S, A>... policies) {
		_policySeq = new ArrayList<StationaryPolicy<S, A>>(policies.length);
		for (StationaryPolicy<S, A> policy : policies) {
			_policySeq.add(policy);
		}
	}

	/**
	 * Adds a policy to the beginning of this sequence. The added policy will be
	 * queried for an action first.
	 * 
	 * @param policy
	 *            a stationary policy
	 */
	public void prepend(StationaryPolicy<S, A> policy) {
		List<StationaryPolicy<S, A>> newPolicySeq = new ArrayList<StationaryPolicy<S, A>>(
				_policySeq.size() + 1);
		newPolicySeq.add(policy);
		newPolicySeq.addAll(_policySeq);
		_policySeq = newPolicySeq;
	}

	/**
	 * Adds a policy to the end of this sequence. The added policy will be
	 * queried for an action last.
	 * 
	 * @param policy
	 *            a stationary policy
	 */
	public void append(StationaryPolicy<S, A> policy) {
		_policySeq.add(policy);
	}

	/**
	 * Returns the stationary policy given its index in this sequence of
	 * policies.
	 * 
	 * @param index
	 *            the index of a policy
	 * @return the stationary policy in this sequence with the specified
	 *         <code>index</code>
	 */
	public StationaryPolicy<S, A> policy(int index) {
		return _policySeq.get(index);
	}

	@Override
	public A policy(S state, Integer timestep) {
		StationaryPolicy<S, A> policy = _policySeq.get(timestep - 1);
		return policy.policy(state);
	}

	@Override
	public int horizon() {
		return _policySeq.size();
	}

}
