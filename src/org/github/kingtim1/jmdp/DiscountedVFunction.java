/**
	DiscountedVFunction.java

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

/**
 * Represents a value function for a discounted-reinforcement MDP. The key difference between
 * a discounted and undiscounted value function is that the value of a state in
 * a discounted MDP does not depend on the timestep.
 * 
 * @author Timothy A. Mann
 * 
 * @param <S> the state type
 */
public interface DiscountedVFunction<S> extends VFunction<S> {
	
	/**
	 * Returns the long-term value of a specified state.
	 * @param state a state
	 * @return the long-term value of <code>state</code>
	 */
	public double value(S state);
	
	/**
	 * Implements a state value function by taking the greedy value of an action-value function.
	 * @author Timothy A. Mann
	 * 
	 * @param <S> the state type
	 * @param <A> the action type
	 */
	public static class GreedyQ<S,A> implements DiscountedVFunction<S> {
		
		private DiscountedQFunction<S,A> _qfunc;
		
		public GreedyQ(DiscountedQFunction<S,A> qfunc){
			_qfunc = qfunc;
		}
		
		@Override
		public double value(S state, Long timestep) {
			return value(state);
		}

		@Override
		public double value(S state) {
			return _qfunc.greedyValue(state);
		}
		
	}

}
