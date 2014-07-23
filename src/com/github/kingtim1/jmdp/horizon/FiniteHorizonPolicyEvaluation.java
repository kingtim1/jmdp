/**
	FiniteHorizonPolicyEvaluation.java

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

package com.github.kingtim1.jmdp.horizon;

import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.PolicyEvaluation;
import com.github.kingtim1.jmdp.VFunction;
import com.github.kingtim1.jmdp.discounted.DiscountFactor;

/**
 * An implementation of policy evaluation for policies with a finite-horizon.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class FiniteHorizonPolicyEvaluation<S, A> implements PolicyEvaluation<S,A,FiniteHorizonPolicy<S,A>, VFunction<S>> {

	private FiniteStateSMDP<S, A> _smdp;
	private DiscountFactor _df;

	public FiniteHorizonPolicyEvaluation(FiniteStateSMDP<S,A> smdp){
		this(smdp, new DiscountFactor(1));
	}
	
	public FiniteHorizonPolicyEvaluation(FiniteStateSMDP<S, A> smdp, DiscountFactor df) {
		_smdp = smdp;
		_df = df;
	}

	private double rPi(FiniteHorizonPolicy<S,A> policy, S state, Integer timestep) {
		A action = policy.policy(state, timestep);
		return FiniteStateSMDP.avgR(_smdp, state, action);
	}
	
	private double avgNextV(FiniteHorizonPolicy<S,A> policy, S state, Integer timestep, VFunction<S> vfunc){
		A action = policy.policy(state, timestep);
		return FiniteStateSMDP.avgNextV(_smdp, state, action, timestep, vfunc, _df);
	}

	@Override
	public VFunction<S> eval(FiniteHorizonPolicy<S, A> policy) {
		int horizon = policy.horizon();
		MapVFunction<S> vfunc = new MapVFunction<S>(horizon, 0);
		
		for (int h = horizon - 1; h >= 0; h--) {

			Iterable<S> states = _smdp.states();

			for (S state : states) {
				double v = 0;
				if (h == horizon - 1) {
					v = rPi(policy, state, h);
				}else{
					v = rPi(policy, state, h) + avgNextV(policy, state, h, vfunc);
				}
				vfunc.set(state, h, v);
			}
		}

		return vfunc;
	}

}
