/**
	MDP.java

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

package org.github.kingtim1.jmdp;

import org.github.kingtim1.jmdp.util.Optimization;

/**
 * Represents a discrete-time Markov Decision Process model.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public interface MDP<S, A> {
	/**
	 * Returns the expected immediate reinforcement (reward/cost) associated
	 * with the observation (state, action, nextState).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param nextState
	 *            the resulting state
	 * @return the expected reinforcement
	 */
	public double r(S state, A action, S nextState);

	/**
	 * Returns the transition probability associated with the observation
	 * (state, action, nextState).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param nextState
	 *            the resulting state
	 * @return the probability of observing the event (state, action, nextState)
	 */
	public double tprob(S state, A action, S nextState);

	/**
	 * Returns the optimization type associated with this MDP (MINIMIZE or
	 * MAXIMIZE the reinforcement signal).
	 * 
	 * @return the optimization type of this MDP
	 */
	public Optimization opType();
}
