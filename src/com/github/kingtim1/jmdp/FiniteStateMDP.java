/**
	FiniteStateMDP.java

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

import java.util.Collection;

/**
 * Represents a finite-state, finite-action MDP.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface FiniteStateMDP<S, A> extends MDP<S, A> {

	/**
	 * Returns an iterable instance over all the states in this MDP.
	 * 
	 * @return an iterable instance over all states
	 */
	public Iterable<S> states();

	/**
	 * Returns a collection of all the valid actions at a specified state.
	 * 
	 * @param state
	 *            a state
	 * @return the collection of valid actions
	 */
	public Collection<A> actions(S state);

	/**
	 * The number of states in this MDP.
	 * 
	 * @return the total number of states
	 */
	public int numberOfStates();

	/**
	 * The number of different actions in this MDP. However, there may be fewer
	 * valid actions at each state than the number returned by this method.
	 * 
	 * @return the total number of actions
	 */
	public int numberOfActions();

	/**
	 * Returns an iterable instance over all successor states. This method
	 * allows an MDP to specify a subset of successor states. When the successor
	 * states are unknown this method can simply return an iterable instance over
	 * all states.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @return an iterable instance over next states
	 */
	public Iterable<S> successors(S state, A action);
}
