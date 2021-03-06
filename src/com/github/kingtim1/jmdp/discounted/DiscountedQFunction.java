/**
	DiscountedQFunction.java

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

import com.github.kingtim1.jmdp.QFunction;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * Represents an action-value function for a discounted-reinforcement MDP. This
 * class also implements the {@link StationaryPolicy} interface so that it can
 * be used to control an agent. The stationary policy implemented by this class
 * selects the greedy action with respect to the action-values at a state.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface DiscountedQFunction<S, A> extends QFunction<S, A>,
		StationaryPolicy<S, A> {

	public double value(S state, A action);

	public double greedyValue(S state);

	public A greedyAction(S state);

	public DiscountedVFunction<S> greedy();
}
