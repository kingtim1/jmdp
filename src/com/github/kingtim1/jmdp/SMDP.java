/**
	SMDP.java

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

import com.github.kingtim1.jmdp.discounted.DiscountFactor;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * Represents a discrete-time Semi-Markov Decision Process (SMDP).
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public interface SMDP<S, A> {
	/**
	 * Returns the expected <b>undiscounted</b> reinforcement (reward/cost)
	 * associated with the observation (state, action, terminalState, duration).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param terminalState
	 *            the resulting state where the action returned control back to
	 *            the agent
	 * @param duration
	 *            the number of timesteps before the action returns control to
	 *            the agent
	 * @return the expected reinforcement
	 */
	public double r(S state, A action, S terminalState, Integer duration);

	/**
	 * Returns the expected <b>discounted</b> reinforcement (reward/cost)
	 * associated with the observation (state, action, terminalState, duration).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param terminalState
	 *            the resulting state where the action returned control back to
	 *            the agent
	 * @param duration
	 *            the number of timesteps before the action returns control to
	 *            the agent
	 * @param gamma the discount factor used to compute the discounted reinforcement
	 * @return the expected reinforcement
	 */
	public double dr(S state, A action, S terminalState, Integer duration, DiscountFactor gamma);

	/**
	 * Returns the <b>undiscounted</b> transition probability associated with
	 * the observation (state, action, terminalState, duration).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param terminalState
	 *            the resulting state where the action returned control back to
	 *            the agent
	 * @return the probability of observing the event (state, action,
	 *         terminalState, duration)
	 */
	public double tprob(S state, A action, S terminalState, Integer duration);

	/**
	 * Returns the <b>discounted</b> transition probability associated with the
	 * observation (state, action, terminalState, duration).
	 * 
	 * @param state
	 *            the state before executing an action
	 * @param action
	 *            the action that was executed
	 * @param terminalState
	 *            the resulting state where the action returned control back to
	 *            the agent
	 * @param gamma the discount factor used to compute the discounted probability
	 * @return the probability of observing the event (state, action,
	 *         terminalState, duration)
	 */
	public double dtprob(S state, A action, S terminalState, Integer duration, DiscountFactor gamma);
	
	/**
	 * Returns the maximum duration of any action in this SDMP.
	 * 
	 * @return a positive integer which is an upper bound on the duration of any
	 *         action in this SMDP
	 */
	public int maxActionDuration();

	/**
	 * Returns an iterable instance over durations with positive probability
	 * given that 'action' is executed from 'state' and terminates in
	 * 'terminalState'. If it is not clear which durations will have non-zero
	 * probability, then it is possible to return an iterable instance over
	 * integers in {1, 2, ... , maxActionDuration()}.
	 * 
	 * @param state
	 *            the state where 'action' is executed from
	 * @param action
	 *            the 'action' to execute
	 * @param terminalState
	 *            the state where 'action' terminates returning control to the
	 *            agent
	 * @return an iterable instance over durations with non-zero probability
	 */
	public Iterable<Integer> durations(S state, A action, S terminalState);

	/**
	 * Returns the optimization type associated with this SMDP (MINIMIZE or
	 * MAXIMIZE the reinforcement signal).
	 * 
	 * @return the optimization type of this SMDP
	 */
	public Optimization opType();
}
