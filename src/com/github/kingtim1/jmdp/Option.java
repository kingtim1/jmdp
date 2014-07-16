/**
	Option.java

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
 * The options framework provides a common interface for both primitive and
 * temporally extended actions. An option is defined by a 3 things:
 * <UL>
 * <LI>A set of states where the option can be initiated.</LI>
 * <LI>A stationary policy that selects actions while the option is running.</LI>
 * <LI>A rule that determines the probability of the option stopping and returning control to the agent.</LI>
 * </UL>
 * 
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public interface Option<S, A> extends StationaryPolicy<S, A> {
	
	/**
	 * Returns the termination probability associated with a given state.
	 * 
	 * @param state
	 *            a state
	 * @param duration the number of timesteps that this option has been executing for
	 * @return the termination probability associated with <code>state</code>
	 */
	public double terminationProb(S state, int duration);
	
	/**
	 * Returns true if the given state is in a state where this option can be
	 * initialized from, or false otherwise.
	 * 
	 * @param state
	 *            a state
	 * @return true if <code>state</code> is in this option's initial state set;
	 *         otherwise false
	 */
	public boolean inInitialSet(S state);
}
