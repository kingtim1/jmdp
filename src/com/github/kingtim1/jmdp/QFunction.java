/**
	QFunction.java

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

import com.github.kingtim1.jmdp.util.Optimization;

/**
 * Represents an action-value function for a stationary policy in an MDP.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface QFunction<S, A> {

	/**
	 * Returns the action-value for a state-action pair at a given timestep.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @param timestep
	 *            a non-negative value representing the timestep
	 * @return the long-term value of the specified state-action pair
	 */
	public double value(S state, A action, Long timestep);

	/**
	 * Returns the greedy value for a given state. This returns the "best" value
	 * over all valid actions, where best is determined by the optimization
	 * type.
	 * 
	 * @param state
	 *            a state
	 * @param timestep
	 *            a non-negative value representing the timestep
	 * @return the best long-term value of this state over all valid actions
	 */
	public double greedyValue(S state, Long timestep);

	/**
	 * Returns a value function defined by the greedy value at each state.
	 * 
	 * @return a value function defined by the greedy value at each state
	 */
	public VFunction<S> greedy();

	/**
	 * Returns the kind of optimization performed by this action-value function:
	 * MINIMIZE or MAXIMIZE. This determines whether greedy values are chosen to
	 * maximize long-term value or minimize long-term value.
	 * 
	 * @return MINIMIZE or MAXIMIZE
	 */
	public Optimization opType();
}
