/**
	DiscountFactor.java

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

/**
 * A scalar value in the range of [0, 1] representing the rate at which
 * reinforcements are discounted over time.
 * 
 * @author Timothy A. Mann
 *
 */
public class DiscountFactor extends Number {
	private static final long serialVersionUID = -5480682789420158526L;

	private Double _df;

	/**
	 * Constructs a discount factor instance from a scalar in [0, 1].
	 * 
	 * @param gamma
	 *            a scalar value in [0, 1] that will be the value of the
	 *            discount factor
	 */
	public DiscountFactor(double gamma) {
		if (gamma < 0 || gamma > 1) {
			throw new IllegalArgumentException(
					"Expected discount factor to be in [0, 1]. Found " + gamma
							+ ".");
		}
		_df = gamma;
	}

	@Override
	public double doubleValue() {
		return _df.doubleValue();
	}

	@Override
	public float floatValue() {
		return _df.floatValue();
	}

	@Override
	public int intValue() {
		return _df.intValue();
	}

	@Override
	public long longValue() {
		return _df.longValue();
	}

}