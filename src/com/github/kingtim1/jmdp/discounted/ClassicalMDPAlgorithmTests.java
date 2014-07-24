/**
	ClassicalMDPAlgorithmTests.java

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

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Collection;

import org.junit.Test;

import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.StationaryPolicy;
import com.github.kingtim1.jmdp.lib.mdps.ChainMDP;

/**
 * Provides tests for classical MDP algorithms in the discounted setting. The
 * algorithms tested are policy evaluation, value iteration, and policy
 * iteration.
 * 
 * @author Timothy A. Mann
 *
 */
public class ClassicalMDPAlgorithmTests {

	public static final DiscountFactor DF = new DiscountFactor(0.95);
	public static final int PI_MAX_ITERATIONS = -1; // Infinite
	public static final int VI_MAX_ITERATIONS = 100;
	public static final int PE_MAX_ITERATIONS = 1000;

	public static final double VALUE_EPSILON = 0.05;
	public static final double CONVERGENCE_THRESHOLD = 0;
	public static final double PROB_EPSILON = Math.pow(10, -4);

	/**
	 * Tests whether {@link MatrixInversePolicyEvaluation} and
	 * {@link IterativePolicyEvaluation} produce similar value functions for the
	 * same policy. Also tests whether these policy evaluation algorithms
	 * produce the same value for the optimal policy as {@link ValueIteration}.
	 */
	@Test
	public void testPolicyEvaluationAlgorithmsProduceSameValueFunctions() {
		ChainMDP mdp = new ChainMDP();
		MapPolicy<Integer, Integer> optimalPolicy = mdp.optimalPolicy();

		MatrixInversePolicyEvaluation<Integer, Integer> mipe = new MatrixInversePolicyEvaluation<Integer, Integer>(
				mdp, DF);
		IterativePolicyEvaluation<Integer, Integer> ipe = new IterativePolicyEvaluation<Integer, Integer>(
				mdp, DF, PE_MAX_ITERATIONS, CONVERGENCE_THRESHOLD);
		ValueIteration<Integer, Integer> vi = new ValueIteration<Integer, Integer>(
				mdp, DF, PE_MAX_ITERATIONS, CONVERGENCE_THRESHOLD);

		DiscountedVFunction<Integer> mipeVFunc = mipe.eval(optimalPolicy);
		DiscountedVFunction<Integer> ipeVFunc = ipe.eval(optimalPolicy);
		DiscountedVFunction<Integer> viVFunc = vi.run().greedy();

		assertTrue(vfuncsAreEqual(mdp.states(), mipeVFunc, ipeVFunc,
				VALUE_EPSILON));
		assertTrue(vfuncsAreEqual(mdp.states(), mipeVFunc, viVFunc,
				VALUE_EPSILON));
	}

	public static <S> void printVFuncs(Iterable<S> states,
			DiscountedVFunction<S>... vfuncs) {
		DecimalFormat df = new DecimalFormat("0.000");

		for (S state : states) {
			System.out.print("V(" + state + ") = ");
			for (int i = 0; i < vfuncs.length; i++) {
				DiscountedVFunction<S> vfunc = vfuncs[i];
				double val = vfunc.value(state);
				System.out.print("| " + df.format(val) + " |");
			}
			System.out.println();
		}
	}

	/**
	 * Returns true if the specified value functions are equivalent. False is
	 * returned to indicate the the value functions are not sufficiently
	 * similar.
	 * 
	 * @param states
	 *            the collection of states to test the value functions over
	 * @param vfuncA
	 *            a value function
	 * @param vfuncB
	 *            a value function
	 * @param eps
	 *            a threshold used to determine if two values are sufficiently
	 *            close to be considered the same
	 * @return true if <code>vfuncA</code> is equivalent to <code>vfuncB</code>
	 */
	public static <S> boolean vfuncsAreEqual(Iterable<S> states,
			DiscountedVFunction<S> vfuncA, DiscountedVFunction<S> vfuncB,
			double eps) {
		boolean same = true;

		for (S state : states) {
			double vA = vfuncA.value(state);
			double vB = vfuncB.value(state);

			if (Math.abs(vA - vB) > eps) {
				same = false;
			}
		}

		return same;
	}

	/**
	 * Tests if {@link PolicyIteration} produces the optimal policy for a
	 * benchmark MDP.
	 */
	@Test
	public void testPolicyIterationReturnsOptimalPolicy() {
		ChainMDP mdp = new ChainMDP();
		MapPolicy<Integer, Integer> optimalPolicy = mdp.optimalPolicy();

		PolicyIteration<Integer, Integer> pi = new PolicyIteration<Integer, Integer>(
				mdp, DF, PI_MAX_ITERATIONS);

		StationaryPolicy<Integer, Integer> piPolicy = pi.run();

		assertTrue(policiesAreEqual(mdp, piPolicy, optimalPolicy, PROB_EPSILON));
	}

	/**
	 * Tests if {@link ValueIteration} produces the optimal policy for a
	 * benchmark MDP.
	 */
	@Test
	public void testValueIterationReturnsOptimalPolicy() {
		ChainMDP mdp = new ChainMDP();
		MapPolicy<Integer, Integer> optimalPolicy = mdp.optimalPolicy();

		ValueIteration<Integer, Integer> vi = new ValueIteration<Integer, Integer>(
				mdp, DF, VI_MAX_ITERATIONS, CONVERGENCE_THRESHOLD);
		DiscountedQFunction<Integer, Integer> viPolicy = vi.run();

		assertTrue(policiesAreEqual(mdp, viPolicy, optimalPolicy, PROB_EPSILON));
	}

	/**
	 * Returns true if the given polices are equivalent. Returns false if the
	 * policies differ.
	 * 
	 * @param smdp
	 *            an SMDP
	 * @param policyA
	 *            a stationary policy
	 * @param policyB
	 *            a stationary policy
	 * @param eps
	 *            a threshold used to determine whether or not two probabilities
	 *            are nearly the same
	 * @return true if the given policies are equivalent; otherwise false
	 */
	public static <S, A> boolean policiesAreEqual(FiniteStateSMDP<S, A> smdp,
			StationaryPolicy<S, A> policyA, StationaryPolicy<S, A> policyB,
			double eps) {
		if (policyA.isDeterministic() != policyB.isDeterministic()) {
			return false;
		}

		Iterable<S> states = smdp.states();

		if (policyA.isDeterministic()) {
			boolean same = true;
			for (S state : states) {
				A actionA = policyA.policy(state);
				A actionB = policyB.policy(state);
				if (!actionA.equals(actionB)) {
					same = false;
				}
			}
			return same;
		} else {
			boolean same = true;
			for (S state : states) {
				Collection<A> actions = smdp.actions(state);
				for (A action : actions) {
					double aprobA = policyA.aprob(state, action);
					double aprobB = policyB.aprob(state, action);

					if (Math.abs(aprobA - aprobB) > eps) {
						same = false;
					}
				}
			}
			return same;
		}
	}

}
