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

import com.github.kingtim1.jmdp.DP;
import com.github.kingtim1.jmdp.FiniteStateMDP;
import com.github.kingtim1.jmdp.VFunction;

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
public class FiniteHorizonPolicyEvaluation<S, A> implements DP<VFunction<S>> {

	private FiniteStateMDP<S, A> _mdp;
	private FiniteHorizonPolicy<S, A> _policy;

	public FiniteHorizonPolicyEvaluation(FiniteStateMDP<S, A> mdp,
			FiniteHorizonPolicy<S, A> policy) {
		_mdp = mdp;
		_policy = policy;
	}

	@Override
	public VFunction<S> run() {
		int horizon = _policy.horizon();
		MapVFunction<S> vfunc = new MapVFunction<S>(horizon, 0);

		for (int h = horizon - 1; h >= 0; h--) {

			Iterable<S> states = _mdp.states();

			for (S state : states) {
				double v = 0;
				if (h == horizon - 1) {
					v = rPi(state, h);
				}else{
					v = rPi(state, h) + avgV(state, h, vfunc);
				}
				vfunc.set(state, h, v);
			}
		}

		return vfunc;
	}

	private double rPi(S state, Integer timestep) {
		double ravg = 0;

		A action = _policy.policy(state, timestep);
		Iterable<S> nextStates = _mdp.successors(state, action);
		for (S nextState : nextStates) {
			double tprob = _mdp.tprob(state, action, nextState);
			double r = _mdp.r(state, action, nextState);
			ravg += tprob * r;
		}

		return ravg;
	}
	
	private double avgV(S state, Integer timestep, VFunction<S> vfunc){
		double avgV = 0;
		A action = _policy.policy(state, timestep);
		Iterable<S> nextStates = _mdp.successors(state, action);
		for( S nextState : nextStates){
			double tprob = _mdp.tprob(state, action, nextState);
			avgV += tprob * vfunc.value(nextState, timestep + 1);
		}
		return avgV;
	}

}
