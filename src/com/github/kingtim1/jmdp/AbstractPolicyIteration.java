/**
	AbstractPolicyIteration.java

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

/**
 * A generic policy iteration framework. Depending on the implementations of
 * {@link PolicyEvaluation} and {@link PolicyImprovement} provided, this class
 * can implement exact Policy Iteration or Approximate Policy Iteration (API).
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 * @param <P>
 *            the policy type
 * @param <V>
 *            the value function type (either a subclass of {@link VFunction} or
 *            {@link QFunction})
 */
public abstract class AbstractPolicyIteration<S, A, P extends Policy<S, A>, V>
		implements DP<P> {

	/**
	 * Listens for typical events that occur during policy iteration algorithms.
	 * Instances of classes implementing this interface need to be added to a
	 * policy iteration algorithm by making a call to
	 * {@link AbstractPolicyIteration#addPolicyIterationListener}.
	 * 
	 * @author Timothy A. Mann
	 *
	 * @param <S>
	 *            the state type
	 * @param <A>
	 *            the action type
	 * @param <P>
	 *            the policy type
	 * @param <V>
	 *            the value function type
	 */
	public static interface PolicyIterationListener<S, A, P extends Policy<S, A>, V> {

		/**
		 * Called after the initial policy is generated and evaluated.
		 * 
		 * @param policy
		 *            a policy
		 * @param vfunc
		 *            the result of evaluating the policy
		 * @param policyGenerationTimeInMillis
		 *            the time in milliseconds taken to generate the policy
		 * @param policyEvaluationTimeInMillis
		 *            the time in milliseconds taken to evaluate the policy
		 */
		public void initialEvaluation(P policy, V vfunc,
				long policyGenerationTimeInMillis,
				long policyEvaluationTimeInMillis);

		/**
		 * Called after an iteration has completed.
		 * 
		 * @param iteration
		 *            the iteration number that completed
		 * @param oldPolicy
		 *            the policy before running policy improvement
		 * @param oldVFunc
		 *            the estimate of the value function of
		 *            <code>oldPolicy</code>
		 * @param newPolicy
		 *            the result of running policy improvement
		 * @param newVFunc
		 *            the estimate of the value function of
		 *            <code>newPolicy</code>
		 * @param policyImprovementTimeInMillis
		 *            the time in milliseconds taken to perform policy
		 *            improvement
		 * @param policyEvaluationTimeInMillis
		 *            the time in milliseconds taken to perform policy
		 *            evaluation
		 */
		public void iteration(int iteration, P oldPolicy, V oldVFunc,
				P newPolicy, V newVFunc, long policyImprovementTimeInMillis,
				long policyEvaluationTimeInMillis);

		/**
		 * Called when the policy iteration algorithm finishes its last
		 * iteration.
		 * 
		 * @param policy
		 *            the final policy
		 * @param vfunc
		 *            the estimate of the value of the final policy
		 */
		public void finished(P policy, V vfunc);
	}

	private PolicyEvaluation<S, A, P, V> _pe;
	private PolicyImprovement<S, A, P, V> _pi;
	private List<PolicyIterationListener<S, A, P, V>> _listeners;

	/**
	 * Constructs an instance of AbstractPolicyEvaluation given algorithms for
	 * performing {@link PolicyEvaluation} and {@link PolicyImprovement}.
	 * 
	 * @param pe a policy evaluation algorithm
	 * @param pi a policy improvement algorithm
	 */
	public AbstractPolicyIteration(PolicyEvaluation<S, A, P, V> pe,
			PolicyImprovement<S, A, P, V> pi) {
		_pe = pe;
		_pi = pi;

		_listeners = new ArrayList<PolicyIterationListener<S, A, P, V>>();
	}

	/**
	 * Returns the instance used to perform policy evaluation.
	 * 
	 * @return the policy evaluation algorithm
	 */
	public PolicyEvaluation<S, A, ? extends P, ? extends V> policyEvaluation() {
		return _pe;
	}

	/**
	 * Returns the instance used to perform policy improvement.
	 * 
	 * @return the policy improvement algorithm
	 */
	public PolicyImprovement<S, A, ? extends P, ? extends V> policyImprovement() {
		return _pi;
	}

	/**
	 * Adds a {@link PolicyIterationListener} to this instance.
	 * 
	 * @param l
	 *            a listener for events that occur during policy iteration
	 */
	public void addPolicyIterationListener(PolicyIterationListener<S, A, P, V> l) {
		_listeners.add(l);
	}

	/**
	 * Removes a {@link PolicyIterationListener} from this instance.
	 * 
	 * @param l
	 *            a listener for events that occur during policy iteration
	 */
	public void removePolicyIterationListener(
			PolicyIterationListener<S, A, P, V> l) {
		_listeners.remove(l);
	}

	/**
	 * Returns true if the policy iteration process can terminate. False is
	 * returned to indicate that the algorithm should continue.
	 * 
	 * @param policy
	 *            a policy
	 * @param policyValue
	 *            the value of the specified policy
	 * @param iteration
	 *            the last iteration completed
	 * @return true if policy iteration is finished; false otherwise
	 */
	public abstract boolean isFinished(P policy, V policyValue, int iteration);

	/**
	 * Generates an initial policy.
	 * 
	 * @return a policy
	 */
	public abstract P initialPolicy();

	/**
	 * Implements the core policy iteration algorithm using the
	 * {@link PolicyImprovement} and {@link PolicyEvaluation} instances given
	 * when this instance was constructed.
	 */
	@Override
	public P run() {
		long pgStart = System.currentTimeMillis();
		P policy = initialPolicy();
		long pgEnd = System.currentTimeMillis();

		long ipeStart = System.currentTimeMillis();
		V vfunc = _pe.eval(policy);
		long ipeEnd = System.currentTimeMillis();

		long pgTime = pgEnd - pgStart;
		long ipeTime = ipeEnd - ipeStart;
		for (PolicyIterationListener<S, A, P, V> l : _listeners) {
			l.initialEvaluation(policy, vfunc, pgTime, ipeTime);
		}

		int iteration = 0;
		while (!isFinished(policy, vfunc, iteration)) {
			iteration++;

			long peStart = System.currentTimeMillis();
			P newPolicy = _pi.improve(policy, vfunc);
			long peEnd = System.currentTimeMillis();
			long piStart = System.currentTimeMillis();
			V newVFunc = _pe.eval(newPolicy);
			long piEnd = System.currentTimeMillis();

			long peTime = peEnd - peStart;
			long piTime = piEnd - piStart;
			for (PolicyIterationListener<S, A, P, V> l : _listeners) {
				l.iteration(iteration, policy, vfunc, newPolicy, newVFunc,
						piTime, peTime);
			}

			policy = newPolicy;
			vfunc = newVFunc;
		}

		for (PolicyIterationListener<S, A, P, V> l : _listeners) {
			l.finished(policy, vfunc);
		}

		return policy;
	}

}
