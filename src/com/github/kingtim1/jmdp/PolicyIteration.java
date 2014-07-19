/**
	PolicyIteration.java

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
 *            the value function type
 */
public abstract class PolicyIteration<S, A, P extends Policy<S, A>, V>
		implements DP<P> {

	private PolicyEvaluation<S, A, P, V> _pe;
	private PolicyImprovement<S, A, P, V> _pi;

	public PolicyIteration(PolicyEvaluation<S, A, P, V> pe,
			PolicyImprovement<S, A, P, V> pi) {
		_pe = pe;
		_pi = pi;
	}

	/**
	 * Returns true if the policy iteration process can terminate. False is
	 * returned to indicate that the algorithm should continue.
	 * 
	 * @param policy
	 *            a policy
	 * @param policyValue
	 *            the value of the specified policy
	 * @param iteration the last iteration completed
	 * @return true if policy iteration is finished; false otherwise
	 */
	public abstract boolean isFinished(P policy, V policyValue, int iteration);

	/**
	 * Generates an initial policy.
	 * 
	 * @return a policy
	 */
	public abstract P initialPolicy();

	@Override
	public P run() {
		P policy = initialPolicy();
		V vfunc = null;
		int iteration = 0;
		
		do {
			vfunc = _pe.eval(policy);
			P newPolicy = _pi.improve(policy, vfunc);
			policy = newPolicy;
			iteration++;
		} while (!isFinished(policy, vfunc, iteration));

		return policy;
	}

}
