/**
	RBoundedSMDP.java

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
 * Represents an SMDP with bounded immediate (1 timestep) reinforcements.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public interface RBoundedSMDP<S,A> extends SMDP<S,A> {
	
	/**
	 * Returns the maximum possible immediate reinforcement for this SMDP.
	 * @return maximum possible immediate reinforcement
	 */
	public double rmax();
	
	/**
	 * Returns the minimum possible immediate reinforcement for this SMDP.
	 * @return minimum possible immediate reinforcement
	 */
	public double rmin();
}
