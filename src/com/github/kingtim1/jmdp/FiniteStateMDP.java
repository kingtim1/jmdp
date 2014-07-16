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

import com.github.kingtim1.jmdp.discounted.DiscountedVFunction;

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
public abstract class FiniteStateMDP<S, A> implements MDP<S, A> {

	/**
	 * Returns an iterable instance over all the states in this MDP.
	 * 
	 * @return an iterable instance over all states
	 */
	public abstract Iterable<S> states();

	/**
	 * Returns a collection of all the valid actions at a specified state.
	 * 
	 * @param state
	 *            a state
	 * @return the collection of valid actions
	 */
	public abstract Collection<A> actions(S state);

	/**
	 * The number of states in this MDP.
	 * 
	 * @return the total number of states
	 */
	public abstract int numberOfStates();

	/**
	 * The number of different actions in this MDP. However, there may be fewer
	 * valid actions at each state than the number returned by this method.
	 * 
	 * @return the total number of actions
	 */
	public abstract int numberOfActions();

	/**
	 * Returns an iterable instance over all successor states. This method
	 * allows an MDP to specify a subset of successor states. When the successor
	 * states are unknown this method can simply return an iterable instance
	 * over all states.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @return an iterable instance over next states
	 */
	public abstract Iterable<S> successors(S state, A action);

	/**
	 * Returns the expected reinforcement at the specified state-action pair.
	 * 
	 * @param mdp a finite-state MDP
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @return the expected reinforcement for (state, action)
	 */
	public static <S, A> double avgR(FiniteStateMDP<S, A> mdp, S state, A action) {
		double ravg = 0;
		Iterable<S> nextStates = mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = mdp.tprob(state, action, nextState);
			double r = mdp.r(state, action, nextState);
			ravg += tprob * r;
		}
		return ravg;
	}

	/**
	 * Returns the expected value associated with the state immediately
	 * transitioned to from (state, action).
	 * 
	 * @param mdp a finite-state MDP
	 * @param state a state
	 * @param action an action
	 * @param vfunc an estimate of the value function
	 * @return the expected value of the next state
	 */
	public static <S, A> double avgNextV(FiniteStateMDP<S, A> mdp, S state,
			A action, DiscountedVFunction<S> vfunc) {
		double avgV = 0;
		Iterable<S> nextStates = mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = mdp.tprob(state, action, nextState);
			double v = vfunc.value(nextState);
			avgV += tprob * v;
		}
		return avgV;
	}
	
	/**
	 * Returns the expected value associated with the state immediately
	 * transitioned to from (state, action, timestep).
	 * 
	 * @param mdp a finite-state MDP
	 * @param state a state
	 * @param action an action
	 * @param timestep the current timestep
	 * @param vfunc an estimate of the value function
	 * @return the expected value of the next state
	 */
	public static <S, A> double avgNextV(FiniteStateMDP<S, A> mdp, S state,
			A action, Integer timestep, VFunction<S> vfunc) {
		double avgV = 0;
		Iterable<S> nextStates = mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = mdp.tprob(state, action, nextState);
			double v = vfunc.value(nextState, timestep + 1);
			avgV += tprob * v;
		}
		return avgV;
	}
}
