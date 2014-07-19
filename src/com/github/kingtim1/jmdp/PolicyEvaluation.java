/**
	PolicyEvaluation.java

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
 * An interface for policy evaluation algorithms. Policy evaluation algorithms
 * estimate (or solve for) the expected (discounted or undiscounted) sum of rewards
 * received for following a policy at each state in an SMDP (or MDP).
 * 
 * @author Timothy A. Mann
 * 
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 * @param <P>
 *            the policy type
 * @param <V> the value function type (a subclass of either {@link VFunction} or {@link QFunction})
 */
public interface PolicyEvaluation<S, A, P extends Policy<S, A>, V> {

	/**
	 * Estimates the value function for the specified policy.
	 * @param policy a policy
	 * @return the state value function
	 */
	public V eval(P policy);
}
