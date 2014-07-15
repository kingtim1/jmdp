/**
	Policy.java

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
 * Represents a policy. Policies decide which action to execute given the
 * current state and timestep. This interface is a stub for all policies.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface Policy<S, A> {

	/**
	 * Selects an action-based on a given state and the current timestep. The
	 * rules used to select an action may be stochastic or deterministic.
	 * 
	 * @param state a state
	 * @param timestep the non-negative value representing a timestep
	 * @return an action
	 */
	public A policy(S state, Integer timestep);
	
}
