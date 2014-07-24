/**
	MatrixInversePolicyEvaluation.java

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.PolicyEvaluation;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * <p>
 * Exact policy evaluation via matrix inversion. This algorithm will only work
 * for SMDPs with a moderate number of states and actions.
 * </p>
 * 
 * <p>
 * The algorithm solves: $V^{\pi} = R^{\pi}(I - P^{\pi})^{-1}$ where $V^{\pi}$
 * is the value function for policy $\pi$, $R^{\pi}$ is the expected, immediate
 * reward for policy $\pi$, $I$ is the identity matrix, and $P^{\pi}$ is the discounted transition
 * probability kernel with respect to $\pi$.
 * </p>
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class MatrixInversePolicyEvaluation<S, A> implements
		PolicyEvaluation<S, A, StationaryPolicy<S, A>, DiscountedVFunction<S>> {

	private FiniteStateSMDP<S, A> _smdp;
	private DiscountFactor _df;

	/**
	 * Constructs a policy evaluator given an SMDP and a discount factor.
	 * 
	 * @param smdp
	 *            a finite state SMDP model
	 * @param df
	 *            a discount factor
	 */
	public MatrixInversePolicyEvaluation(FiniteStateSMDP<S, A> smdp,
			DiscountFactor df) {
		_smdp = smdp;
		_df = df;
	}

	private RealMatrix gammaPPi(StationaryPolicy<S, A> policy, List<S> states) {
		int n = _smdp.numberOfStates();
		double[][] gpp = new double[n][n];

		// Fill the matrix
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				gpp[i][j] = gammaPPi(policy, states, i, j);
			}
		}

		return new Array2DRowRealMatrix(gpp);
	}

	private double gammaPPi(StationaryPolicy<S, A> policy, List<S> states,
			int statei, int statej) {
		S state = states.get(statei);
		S tstate = states.get(statej);

		if (policy.isDeterministic()) {
			A action = policy.policy(state);
			return gammaPPi(state, action, tstate);
		} else {
			double pavg = 0;
			Iterable<A> actions = _smdp.actions(state);
			for (A action : actions) {
				pavg += policy.aprob(state, action)
						* gammaPPi(state, action, tstate);
			}
			return pavg;
		}
	}

	private double gammaPPi(S state, A action, S terminalState) {
		double gprob = 0;
		Iterable<Integer> durs = _smdp.durations(state, action, terminalState);
		for (Integer dur : durs) {
			gprob += _smdp.dtprob(state, action, terminalState, dur, _df);
		}
		return gprob;
	}

	/**
	 * Returns a vector containing the expected reinforcement with respect to
	 * the policy at each state.
	 * 
	 * @param states
	 *            a list of states (this determines the order of the
	 *            reinforcements in the returned vector)
	 * @return a vector containing the immediate expected reinforcement at each
	 *         state
	 */
	private RealVector rPi(StationaryPolicy<S, A> policy, List<S> states) {
		int n = _smdp.numberOfStates();
		double[] rp = new double[n];

		// Fill the vector
		for (int i = 0; i < n; i++) {
			rp[i] = rPi(policy, states, i);
		}

		return new ArrayRealVector(rp);
	}

	/**
	 * Returns the immediate, expected reinforcement at a specified state with
	 * respect to the policy.
	 * 
	 * @param states
	 *            a list of states
	 * @param statei
	 *            an index into the list of states (selects the state to compute
	 *            the expected reward for)
	 * @return the expected reward of the state index by statei
	 */
	private double rPi(StationaryPolicy<S, A> policy, List<S> states, int statei) {
		S state = states.get(statei);
		if (policy.isDeterministic()) {
			A action = policy.policy(state);
			return FiniteStateSMDP.avgR(_smdp, state, action);
		} else {
			Iterable<A> actions = _smdp.actions(state);
			double ravg = 0;
			for (A action : actions) {
				double aprob = policy.aprob(state, action);
				ravg += aprob * FiniteStateSMDP.avgR(_smdp, state, action);
			}
			return ravg;
		}
	}

	@Override
	public DiscountedVFunction<S> eval(StationaryPolicy<S, A> policy) {
		int n = _smdp.numberOfStates();
		List<S> states = new ArrayList<S>(n);
		Iterable<S> istates = _smdp.states();
		for (S state : istates) {
			states.add(state);
		}

		// Construct matrix A and vector b
		RealMatrix id = MatrixUtils.createRealIdentityMatrix(n);
		RealMatrix gpp = gammaPPi(policy, states);
		RealMatrix A = id.subtract(gpp);
		RealVector b = rPi(policy, states);

		// Solve for V^{\pi}
		SingularValueDecomposition decomp = new SingularValueDecomposition(A);
		DecompositionSolver dsolver = decomp.getSolver();
		RealVector vpi = dsolver.solve(b);

		// Construct the value function
		Map<S, Double> valueMap = new HashMap<S, Double>();
		for (int i = 0; i < states.size(); i++) {
			S state = states.get(i);
			double val = vpi.getEntry(i);
			valueMap.put(state, val);
		}

		return new MapVFunction<S>(valueMap, 0);
	}

}
