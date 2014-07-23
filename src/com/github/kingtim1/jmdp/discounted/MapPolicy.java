/**
	MapPolicy.java

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

import java.util.Map;

import com.github.kingtim1.jmdp.DeterministicPolicy;

/**
 * A policy implemented by a {@link Map} data structure.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public class MapPolicy<S, A> extends DeterministicPolicy<S,A> {

	private Map<S,A> _policy;
	
	public MapPolicy(Map<S,A> policy){
		if(policy == null){
			throw new NullPointerException("Cannot construct policy from a null map.");
		}
		_policy = policy;
	}

	@Override
	public A policy(S state) {
		return _policy.get(state);
	}
	
	/**
	 * Sets this policy's action at a specified state.
	 * @param state a state
	 * @param action an action
	 */
	public void set(S state, A action){
		_policy.put(state, action);
	}

}
