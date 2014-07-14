/**
	VFunction.java

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
 * Represents the value function of a policy in an MDP.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 */
public interface VFunction<S> {

	/**
	 * Returns the value of a state.
	 * @param state a state
	 * @param timestep a non-negative value representing the timestep
	 * @return the long-term value of the state
	 */
	public double value(S state, Long timestep);
	
	/**
	 * Implements a state value function by taking the greedy value of an action-value function.
	 * @author Timothy A. Mann
	 * 
	 * @param <S> the state type
	 * @param <A> the action type
	 */
	public static class GreedyQ<S,A> implements VFunction<S>, Policy<S,A>{

		private QFunction<S,A> _qfunc;
		
		public GreedyQ(QFunction<S,A> qfunc){
			_qfunc = qfunc;
		}
		
		@Override
		public double value(S state, Long timestep) {
			return _qfunc.greedyValue(state, timestep);
		}

		@Override
		public A policy(S state, Long timestep) {
			return _qfunc.greedyAction(state, timestep);
		}
		
	}
}
