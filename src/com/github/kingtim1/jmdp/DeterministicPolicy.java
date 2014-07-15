/**
	DeterministicPolicy.java

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

package com.github.kingtim1.jmdp;

/**
 * Represents a deterministic, stationary policy. A deterministic policy is
 * simply a mapping from states to actions.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public abstract class DeterministicPolicy<S, A> implements
		StationaryPolicy<S, A> {

	public DeterministicPolicy() {
	}

	@Override
	public A policy(S state, Integer timestep) {
		return policy(state);
	}

	@Override
	public double aprob(S state, A action) {
		return action.equals(policy(state)) ? 1 : 0;
	}

	@Override
	public boolean isDeterministic() {
		return true;
	}

}
