/**
	ListActionSet.java

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

package com.github.kingtim1.jmdp.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.github.kingtim1.jmdp.ActionSet;

/**
 * A naive implementation of the {@link ActionSet} interface. The actions are
 * stored in a list and assumed to be valid in all states.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class ListActionSet<S, A> implements ActionSet<S, A> {

	private Random _rand = new Random();
	private List<A> _actions;

	public ListActionSet(A... actions) {
		_actions = new ArrayList<A>(actions.length);
		for (A action : actions) {
			_actions.add(action);
		}
	}

	public ListActionSet(Collection<A> actions) {
		_actions = new ArrayList<A>(actions);
	}

	@Override
	public boolean isValid(S state, A action) {
		if (_actions.contains(action)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Integer> indices(S state) {
		List<Integer> inds = new ArrayList<Integer>(_actions.size());
		for (int i = 0; i < _actions.size(); i++) {
			inds.add(i);
		}
		return inds;
	}

	@Override
	public List<A> actions(S state) {
		return new ArrayList<A>(_actions);
	}

	@Override
	public A uniformRandom(S state) {
		int r = _rand.nextInt(_actions.size());
		return _actions.get(r);
	}

	@Override
	public A action(Integer index) {
		return _actions.get(index);
	}

	@Override
	public Integer index(A action) {
		return _actions.indexOf(action);
	}

	@Override
	public int numberOfActions() {
		return _actions.size();
	}

	/**
	 * Constructs a new {@link ListActionSet} with {@link Integer} action
	 * symbols. The actions are <code>[0, 1, 2, ... , numActions]</code>.
	 * 
	 * @param numActions
	 *            the number of actions to put in the action set
	 * @return an action set with actions denoted by Integer symbols
	 */
	public static final <S> ListActionSet<S, Integer> buildActionSet(
			int numActions) {
		List<Integer> actions = new ArrayList<Integer>(numActions);
		for (int a = 0; a < numActions; a++) {
			actions.add(a);
		}
		return new ListActionSet<S, Integer>(actions);
	}

}
