/**
	ChainMDP.java

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

package com.github.kingtim1.jmdp.lib.mdps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.kingtim1.jmdp.FiniteStateMDP;
import com.github.kingtim1.jmdp.RBoundedSMDP;
import com.github.kingtim1.jmdp.actions.ListActionSet;
import com.github.kingtim1.jmdp.discounted.MapPolicy;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * The chain MDP is a popular benchmark where the states are arranged in one
 * long sequence (or chain). The agent has three actions. From the last state
 * all actions transition back to the first state in the chain with probability
 * 1. From other states:
 * <OL>
 * <LI>ACTION1 - stays in the current state with probability 0.2 and transitions
 * to the next state in the chain with probability 0.8</LI>
 * <LI>ACTION2 - stays in the current state with probability 0.1, transitions to
 * the first state in the chain with probability 0.2, and transitions to the
 * next state in the sequence with probability 0.7</LI>
 * <LI>ACTION3 - transitions back to the first state in the chain with
 * probability 1.</LI>
 * </OL>
 * The agent receives a reward of 1 for reaching the last state in the chain.
 * The agent receives a reward of 0 for all other events.
 * 
 * @author Timothy A. Mann
 *
 */
public class ChainMDP extends FiniteStateMDP<Integer, Integer> implements RBoundedSMDP<Integer,Integer> {

	public static final int DEFAULT_NUM_STATES = 20;
	public static final int NUM_ACTIONS = 3;

	public static final int ACTION1 = 0;
	public static final int ACTION2 = 1;
	public static final int ACTION3 = 2;

	public static final double RMAX = 1;
	public static final double RMIN = 0;
	
	private int _numStates;

	public ChainMDP() {
		this(DEFAULT_NUM_STATES);
	}

	public ChainMDP(int numStates) {
		super(ListActionSet.<Integer> buildActionSet(NUM_ACTIONS),
				Optimization.MAXIMIZE);
		if (numStates < 1) {
			throw new IllegalArgumentException("Cannot construct an MDP with "
					+ numStates + " states.");
		}
		_numStates = numStates;
	}

	@Override
	public double r(Integer state, Integer action, Integer nextState) {
		Integer lastState = new Integer(_numStates - 1);
		if (nextState.equals(lastState)) {
			return rmax();
		} else {
			return rmin();
		}
	}

	@Override
	public double tprob(Integer state, Integer action, Integer nextState) {
		Integer statePlusOne = new Integer(state.intValue() + 1);
		Integer lastState = new Integer(_numStates - 1);
		Integer firstState = new Integer(0);

		// From the last state we always transition back to the first state
		if (state.equals(lastState)) {
			return firstState.equals(nextState) ? 1 : 0;
		}

		// ACTION1 stays in the current state with probability 0.2 and moves to
		// the next state with probability 0.8
		if (action.equals(ACTION1)) {
			if (state.equals(nextState)) {
				return 0.2;
			} else if (statePlusOne.equals(nextState)) {
				return 0.8;
			}
		}

		// ACTION2 stays in the current state with probability 0.1, returns to
		// the first state with probability 0.2, and moves forward with
		// probability 0.7.
		if (action.equals(ACTION2)) {
			if (state.equals(nextState)) {
				return 0.1;
			} else if (firstState.equals(nextState)) {
				return 0.2;
			} else if (statePlusOne.equals(nextState)) {
				return 0.7;
			}
		}

		// ACTION3 always returns to the first state
		if (action.equals(ACTION3)) {
			if (firstState.equals(nextState)) {
				return 1;
			}
		}

		// All other cases have probability 0
		return 0;
	}

	@Override
	public Iterable<Integer> states() {
		List<Integer> states = new ArrayList<Integer>();
		for (int i = 0; i < _numStates; i++) {
			states.add(i);
		}
		return states;
	}

	@Override
	public int numberOfStates() {
		return _numStates;
	}

	@Override
	public Iterable<Integer> successors(Integer state, Integer action) {
		Integer lastState = new Integer(_numStates - 1);
		Integer firstState = new Integer(0);
		Set<Integer> succs = new HashSet<Integer>();
		succs.add(firstState);
		succs.add(state);
		if (!state.equals(lastState)) {
			succs.add(state + 1);
		}
		return succs;
	}

	/**
	 * Returns the optimal policy for this MDP.
	 * 
	 * @return the optimal policy
	 */
	public MapPolicy<Integer, Integer> optimalPolicy() {
		Map<Integer, Integer> pmap = new HashMap<Integer, Integer>();
		for (Integer s : states()) {
			pmap.put(s, ACTION1);
		}

		MapPolicy<Integer, Integer> policy = new MapPolicy<Integer, Integer>(
				pmap);
		return policy;
	}

	@Override
	public double rmax() {
		return RMAX;
	}

	@Override
	public double rmin() {
		return RMIN;
	}
}
