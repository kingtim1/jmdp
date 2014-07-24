/**
	StationaryPolicy.java

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
 * A stationary policy selects actions conditional only on the current state. In
 * other words, decisions made by a stationary policy are independent of the
 * current timestep. Stationary policies may be stochastic or deterministic.
 * 
 * @author Timothy A. Mann
 * 
 */
public interface StationaryPolicy<S, A> extends Policy<S, A> {

	/**
	 * Selects an action based on the given state. This method may return an
	 * action based on a probability distribution.
	 * 
	 * @param state
	 *            a state
	 * @return an action
	 */
	public A policy(S state);

	/**
	 * Returns the probability that this policy will select a specified action
	 * from the given state.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @return the probability that <code>action</code> will be selected by this
	 *         policy from <code>state</code>
	 */
	public double aprob(S state, A action);

	/**
	 * Returns true if this is a deterministic policy and false if this is a
	 * stochastic policy.
	 * 
	 * @return true if this is a deterministic policy; false if it is stochastic
	 */
	public boolean isDeterministic();
}
