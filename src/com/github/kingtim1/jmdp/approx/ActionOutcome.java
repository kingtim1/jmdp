/**
	ActionOutcome.java

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

package com.github.kingtim1.jmdp.approx;

/**
 * An action outcome represents the result of running an action in an SMDP (or
 * MDP) until the action returns control back to the agent.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class ActionOutcome<S, A> {
	private S _state;
	private A _action;
	private S _tstate;
	private double _r;
	private int _duration;

	/**
	 * Constructs an action outcome.
	 * 
	 * @param state
	 *            the state where the action was initialized
	 * @param action
	 *            the action that was executed
	 * @param terminalState
	 *            the state where the action returned control to the agent
	 * @param r
	 *            the cumulative reinforcement obtained during the action's
	 *            lifetime
	 * @param duration
	 *            the number of timesteps that the action executed for
	 */
	public ActionOutcome(S state, A action, S terminalState, double r,
			int duration) {
		_state = state;
		_action = action;
		_tstate = terminalState;
		_r = r;
		if (duration < 1) {
			throw new IllegalArgumentException(
					"Expected positive integer for action's duration. Found : "
							+ duration + ".");
		}
		_duration = duration;
	}

	/**
	 * Returns the state where the action was initialized.
	 * @return the state where the action was initialized
	 */
	public S state() {
		return _state;
	}

	/**
	 * Returns the action that was executed.
	 * @return the action that was executed
	 */
	public A action() {
		return _action;
	}

	/**
	 * Returns the state where the action returned control to the agent.
	 * @return the state where the action terminated
	 */
	public S terminalState() {
		return _tstate;
	}

	/**
	 * Returns the cumulative reinforcement received during the lifetime of the
	 * action.
	 * 
	 * @return the reinforcement
	 */
	public double r() {
		return _r;
	}

	/**
	 * Returns the number of timesteps that the action executed before returning
	 * control to the agent.
	 * 
	 * @return the number of timesteps that the action executed
	 */
	public int duration() {
		return _duration;
	}
}
