/**
	DP.java

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
 * A standard interface for dynamic programming algorithms.
 * 
 * @author Timothy A. Mann
 *
 * @param <P>
 *            the type of product returned by this algorithm. This is typically
 *            a value function, action-value function, or a policy
 */
public interface DP<P> {

	/**
	 * Executes this dynamic programming algorithm.
	 * @return the product of this dynamic programming algorithm
	 */
	public P run();
}
