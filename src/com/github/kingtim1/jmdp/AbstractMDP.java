/**
	AbstractMDP.java

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

import java.util.ArrayList;
import java.util.List;

import com.github.kingtim1.jmdp.discounted.DiscountFactor;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * An MDP with default implementations for many of the methods required by the
 * {@link SMDP} interface.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public abstract class AbstractMDP<S, A> implements MDP<S, A> {
	
	private Optimization _opType;
	
	public AbstractMDP(Optimization opType)
	{
		_opType = opType;
	}

	@Override
	public double r(S state, A action, S terminalState, Integer duration) {
		if (duration != 1) {
			return 0;
		} else {
			return r(state, action, terminalState);
		}
	}

	@Override
	public double dr(S state, A action, S terminalState, Integer duration,
			DiscountFactor gamma) {
		return r(state, action, terminalState, duration);
	}

	@Override
	public double tprob(S state, A action, S terminalState, Integer duration) {
		if (duration != 1) {
			return 0;
		} else {
			return tprob(state, action, terminalState);
		}
	}

	@Override
	public double dtprob(S state, A action, S terminalState, Integer duration,
			DiscountFactor gamma) {
		return tprob(state, action, terminalState, duration);
	}

	@Override
	public final int maxActionDuration() {
		return 1;
	}

	@Override
	public final Iterable<Integer> durations(S state, A action, S terminalState) {
		List<Integer> durs = new ArrayList<Integer>(1);
		durs.add(new Integer(1));
		return durs;
	}

	@Override
	public final Optimization opType() {
		return _opType;
	}
	
	

}
