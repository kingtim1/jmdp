/**
	ActionSet.java

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

import java.util.List;

/**
 * Represents a set of finite set of actions and their initiation constraints.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface ActionSet<S, A> {
	/**
	 * Returns true if the specified action can be initiated from a specified
	 * state. Returns false to indicate that the action cannot be initiated from
	 * the specified state.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @return true if the action can be initiated from the state; otherwise
	 *         false.
	 */
	public boolean isValid(S state, A action);

	/**
	 * Returns the list of all indices of actions that can be initiated from the
	 * specified state.
	 * 
	 * @param state
	 *            a state
	 * @return the list of all valid indices
	 */
	public List<Integer> indices(S state);

	/**
	 * Returns the list of all actions that can be initiated from the specified
	 * state.
	 * 
	 * @param state
	 *            a state
	 * @return the list of valid actions
	 */
	public List<A> actions(S state);

	/**
	 * Returns the action symbol specified by an <code>index</code>.
	 * 
	 * @param index
	 *            an integer that is a unique identifier for an action symbol
	 * @return the action symbol mapped to by <code>index</code>
	 */
	public A action(Integer index);

	/**
	 * Returns a unique integer for each action symbol.
	 * 
	 * @param action
	 *            an action symbol
	 * @return an integer mapped to <code>action</code>
	 */
	public Integer index(A action);

	/**
	 * Returns the total number of action symbols.
	 * 
	 * @return the total number of action symbols
	 */
	public int numberOfActions();
}
