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

import java.util.ArrayList;
import java.util.List;

import com.github.kingtim1.jmdp.discounted.DiscountFactor;
import com.github.kingtim1.jmdp.discounted.DiscountedVFunction;
import com.github.kingtim1.jmdp.util.Optimization;

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
public abstract class FiniteStateMDP<S, A> extends FiniteStateSMDP<S,A> implements MDP<S, A> {

	public FiniteStateMDP(ActionSet<S,A> actionSet, Optimization opType) {
		super(actionSet, opType);
	}
	
	@Override
	public double r(S state, A action, S terminalState, Integer duration) {
		if (duration != 1) {
			return 0;
		} else {
			return r(state, action, terminalState);
		}
	}

	@Override
	public double tprob(S state, A action, S terminalState, Integer duration) {
		if (duration != 1) {
			return 0;
		} else {
			return tprob(state, action, terminalState);
		}
	}

	@Override
	public double dtprob(S state, A action, S terminalState, Integer duration,
			DiscountFactor gamma) {
		return tprob(state, action, terminalState, duration);
	}

	@Override
	public final int maxActionDuration() {
		return 1;
	}

	@Override
	public final Iterable<Integer> durations(S state, A action, S terminalState) {
		List<Integer> durs = new ArrayList<Integer>(1);
		durs.add(new Integer(1));
		return durs;
	}

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
