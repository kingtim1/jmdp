/**
	TwoStateMDP.java

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
import java.util.List;
import java.util.Map;

import com.github.kingtim1.jmdp.ActionSet;
import com.github.kingtim1.jmdp.FiniteStateMDP;
import com.github.kingtim1.jmdp.RBoundedSMDP;
import com.github.kingtim1.jmdp.actions.ListActionSet;
import com.github.kingtim1.jmdp.discounted.MapPolicy;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * This is a simple two-state MDP with four actions for testing purposes. The
 * optimal policy is:
 * <OL>
 * <LI>STATE1 -> ACTION2</LI>
 * <LI>STATE2 -> ACTION4</LI>
 * </OL>
 * Both the states and actions are denoted by integers.
 * 
 * @author Timothy A. Mann
 *
 */
public class TwoStateMDP extends FiniteStateMDP<Integer, Integer> implements
		RBoundedSMDP<Integer, Integer> {

	public static final int NUM_STATES = 2;
	public static final int NUM_ACTIONS = 4;

	public static final int ACTION1 = 0;
	public static final int ACTION2 = 1;
	public static final int ACTION3 = 2;
	public static final int ACTION4 = 3;

	public static final int STATE1 = 0;
	public static final int STATE2 = 1;

	public static final double RMAX = 1;
	public static final double RMIN = -1;

	private double[][][] _tmat;

	public TwoStateMDP() {
		super(ListActionSet.<Integer>buildActionSet(NUM_ACTIONS), Optimization.MAXIMIZE);
		_tmat = buildTMat();
	}

	private double[][][] buildTMat() {
		double[][][] tmat = new double[NUM_STATES][NUM_ACTIONS][NUM_STATES];

		// What happens from STATE1?
		tmat[STATE1][ACTION1][STATE1] = 0.5;
		tmat[STATE1][ACTION1][STATE2] = 0.5;

		tmat[STATE1][ACTION2][STATE1] = 0.2;
		tmat[STATE1][ACTION2][STATE2] = 0.8;

		tmat[STATE1][ACTION3][STATE1] = 0.8;
		tmat[STATE1][ACTION3][STATE2] = 0.2;

		tmat[STATE1][ACTION4][STATE1] = 0.9;
		tmat[STATE1][ACTION4][STATE2] = 0.1;

		// What happens from STATE2?
		tmat[STATE2][ACTION1][STATE1] = 0.9;
		tmat[STATE2][ACTION1][STATE2] = 0.1;

		tmat[STATE2][ACTION2][STATE1] = 0.8;
		tmat[STATE2][ACTION2][STATE2] = 0.2;

		tmat[STATE2][ACTION3][STATE1] = 0.7;
		tmat[STATE2][ACTION3][STATE2] = 0.3;

		tmat[STATE2][ACTION4][STATE1] = 0.6;
		tmat[STATE2][ACTION4][STATE2] = 0.4;

		return tmat;
	}

	@Override
	public double r(Integer state, Integer action, Integer nextState) {
		if (nextState == STATE2) {
			return rmax();
		} else {
			return rmin();
		}
	}

	@Override
	public double tprob(Integer state, Integer action, Integer nextState) {
		return _tmat[state][action][nextState];
	}

	@Override
	public Iterable<Integer> states() {
		List<Integer> states = new ArrayList<Integer>();
		states.add(STATE1);
		states.add(STATE2);
		return states;
	}

	@Override
	public int numberOfStates() {
		return NUM_STATES;
	}

	@Override
	public Iterable<Integer> successors(Integer state, Integer action) {
		return states();
	}

	/**
	 * Returns the optimal policy for this MDP.
	 * 
	 * @return the optimal policy
	 */
	public static final MapPolicy<Integer, Integer> optimalPolicy() {
		Map<Integer, Integer> pmap = new HashMap<Integer, Integer>();
		pmap.put(STATE1, ACTION2);
		pmap.put(STATE2, ACTION4);
		return new MapPolicy<Integer, Integer>(pmap);
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
