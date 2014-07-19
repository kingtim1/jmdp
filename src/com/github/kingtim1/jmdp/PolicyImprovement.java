/**
	PolicyImprovement.java

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
 * A policy improvement rule.
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
 *            the value function type (a subclass of {@link VFunction} or
 *            {@link QFunction})
 */
public interface PolicyImprovement<S, A, P extends Policy<S, A>, V> {

	/**
	 * A rule that improves the old policy based on the value function.
	 * @param oldPolicy the old policy
	 * @param vfunc an estimate of the value function
	 * @return a new "improved" policy
	 */
	public P improve(P oldPolicy, V vfunc);
}
