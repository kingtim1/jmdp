package com.github.kingtim1.jmdp.approx;

/**
 * A simulator for an SMDP.
 * @author Timothy A. Mann
 *
 * @param <S> the state type
 * @param <A> the action type
 */
public interface Simulator<S, A> {
	
	/**
	 * Samples "what would happen if 'action' was executed from 'state'?".
	 * @param state a state
	 * @param action an action
	 * @return an action outcome
	 */
	public ActionOutcome<S,A> sim(S state, A action);
}
