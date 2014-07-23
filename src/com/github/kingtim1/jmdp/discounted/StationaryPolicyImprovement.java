/**
	StationaryPolicyImprovement.java

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

import com.github.kingtim1.jmdp.ActionSet;
import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.PolicyImprovement;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * Performs policy improvement with respect to stationary policies and a
 * state-value function.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class StationaryPolicyImprovement<S, A> implements
		PolicyImprovement<S, A, StationaryPolicy<S, A>, DiscountedVFunction<S>> {

	private FiniteStateSMDP<S, A> _smdp;
	private DiscountFactor _df;

	public StationaryPolicyImprovement(FiniteStateSMDP<S, A> smdp,
			DiscountFactor df) {
		if (smdp == null) {
			throw new NullPointerException("SMDP model cannot be null.");
		}
		_smdp = smdp;
		if (df == null) {
			throw new NullPointerException(
					"Cannot perform policy improvement with a null discount factor.");
		}
		_df = df;
	}

	public FiniteStateSMDP<S, A> smdp() {
		return _smdp;
	}

	public DiscountFactor discountFactor() {
		return _df;
	}

	@Override
	public StationaryPolicy<S, A> improve(StationaryPolicy<S, A> oldPolicy,
			DiscountedVFunction<S> vfunc) {
		MapQFunction<S, A> qfunc = new MapQFunction<S, A>(_smdp.actionSet(),
				0.0, _smdp.opType());
		Iterable<S> states = _smdp.states();
		ActionSet<S, A> actionSet = _smdp.actionSet();
		for (S state : states) {
			for (A action : actionSet.actions(state)) {
				double rAvg = FiniteStateSMDP.avgR(_smdp, state, action);
				double avgNextV = FiniteStateSMDP.avgNextV(_smdp, state,
						action, vfunc, _df);
				double qval = rAvg + avgNextV;
				qfunc.set(state, action, qval);
			}
		}

		return qfunc;
	}

}
