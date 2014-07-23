/**
	StepSizeSchedule.java

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

package com.github.kingtim1.jmdp.util;

/**
 * Step sizes (or learning rates) control how quickly the parameters of a
 * learning algorithm are changed by parameter updates. A valid step size is a
 * scalar value in [0, 1], but a step size schedule can produce different step
 * sizes over time.
 * 
 * @author Timothy A. Mann
 *
 */
public interface StepSizeSchedule {

	/**
	 * Returns the step size (or learning rate) for the ith training epoch or
	 * parameter update.
	 * 
	 * @param i
	 *            an positive integer representing the current training epoch or
	 *            parameter update
	 * @return a scalar value in [0, 1].
	 */
	public double stepsize(int i);
}
