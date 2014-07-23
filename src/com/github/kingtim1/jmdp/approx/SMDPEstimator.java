/**
	SMDPEstimator.java

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;

import com.github.kingtim1.jmdp.ActionSet;
import com.github.kingtim1.jmdp.FiniteStateSMDP;
import com.github.kingtim1.jmdp.RBoundedSMDP;
import com.github.kingtim1.jmdp.discounted.DiscountFactor;
import com.github.kingtim1.jmdp.util.MapUtil;
import com.github.kingtim1.jmdp.util.Optimization;

/**
 * Estimates the transition dynamics and rewards of an (finite-state and
 * finite-action) SMDP from samples. This estimator uses maximum likelihood
 * estimates of the transition probabilities and expected reinforcements.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class SMDPEstimator<S, A> extends FiniteStateSMDP<S, A> implements
		RBoundedSMDP<S, A> {

	private Set<S> _states;
	private Map<S, Map<A, Set<S>>> _succs;

	private Map<S, Integer> _sCounts;
	private Map<S, Map<A, Integer>> _saCounts;
	private Map<S, Map<A, Map<S, Integer>>> _sasCounts;
	private Map<S, Map<A, Map<S, Map<Integer, Integer>>>> _sasdCounts;

	private Map<S, Map<A, Map<S, Set<Integer>>>> _durations;

	private Map<S, Map<A, Map<S, Map<Integer, Double>>>> _rsum;

	private S _dummyState;
	private int _maxDuration;
	private Interval _rInterval;

	private int _m;
	private boolean _optimistic;

	/**
	 * Constructs an SMDP estimator.
	 * 
	 * @param dummyState
	 *            the dummy state should be a symbol that does not represent any
	 *            real state in the SMDP. When a state-action pair is "unknown"
	 *            it is assumed that they transition with probability 1 to the
	 *            dummy state.
	 * @param actionSet
	 *            an action set
	 * @param numSamplesBeforeKnown
	 *            the number of samples needed at a state-action pair before it
	 *            is considered "known"
	 * @param optimistic
	 *            true if this estimator will be optimistic about "unknown"
	 *            state-action pairs; false if it will be pessimistic about them
	 * @param immediateRInterval
	 *            an interval containing the smallest and largest possible
	 *            reinforcements that can be received in a single timestep
	 * @param opType
	 *            the optimization type (MINIMIZE or MAXIMIZE)
	 */
	public SMDPEstimator(S dummyState, ActionSet<S, A> actionSet,
			int numSamplesBeforeKnown, boolean optimistic,
			Interval immediateRInterval, Optimization opType) {
		super(actionSet, opType);
		if (dummyState == null) {
			throw new NullPointerException("Dummy state cannot be null.");
		}
		_dummyState = dummyState;
		_rInterval = immediateRInterval;

		if (numSamplesBeforeKnown < 1) {
			throw new IllegalArgumentException(
					"The number of samples needed before a "
							+ "state-action pair can be considered"
							+ " known must be positive. "
							+ "Expected positive integer. Found "
							+ numSamplesBeforeKnown + ".");
		}
		_m = numSamplesBeforeKnown;
		_optimistic = optimistic;

		reset();
	}

	/**
	 * Resets this SMDP estimator to its initial state (throwing out all of the
	 * sample collected so far).
	 */
	public void reset() {
		_states = new HashSet<S>();
		_maxDuration = 1;

		_succs = new HashMap<S, Map<A, Set<S>>>();
		_sCounts = new HashMap<S, Integer>();
		_saCounts = new HashMap<S, Map<A, Integer>>();
		_sasCounts = new HashMap<S, Map<A, Map<S, Integer>>>();
		_sasdCounts = new HashMap<S, Map<A, Map<S, Map<Integer, Integer>>>>();
		_durations = new HashMap<S, Map<A, Map<S, Set<Integer>>>>();
		_rsum = new HashMap<S, Map<A, Map<S, Map<Integer, Double>>>>();
	}

	/**
	 * Returns the number of samples of a state-action pair needed before the
	 * dynamics and reinforcement structure of a state-action pair is considered
	 * to be "known".
	 * 
	 * @return the number of samples before a state-action pair is considered
	 *         "known"
	 */
	public int numSamplesUntilKnown() {
		return _m;
	}

	/**
	 * Returns true if this estimator is optimistic about "unknown" state-action
	 * pairs. Returns false if this estimator is pessimistic about "unknown"
	 * state-action pairs. If this estimator is optimistic, then it assumes that
	 * the "unknown" state-action pairs transition to the "dummy state" in a
	 * single step and always receive the highest possible reward.
	 * 
	 * @return true if this estimator is optimistic about "unknown" state-action
	 *         pairs; otherwise false
	 */
	public boolean isOptimistic() {
		return _optimistic;
	}

	@Override
	public double r(S state, A action, S terminalState, Integer duration) {
		int saCount = counts(state, action);
		if (saCount < numSamplesUntilKnown()) {
			return unknownR(state, action, terminalState, duration);
		}

		Map<A, Map<S, Map<Integer, Double>>> aRSum = MapUtil.get2ndMap(_rsum,
				state);
		Map<S, Map<Integer, Double>> asRSum = MapUtil.get2ndMap(aRSum, action);
		Map<Integer, Double> asdRSum = MapUtil.get2ndMap(asRSum, terminalState);
		Double rsum = MapUtil.getWithDefault(asdRSum, duration, rmin());

		return rsum / saCount;
	}

	/**
	 * Computes the reinforcement when the specified state-action pair is
	 * unknown.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @param terminalState
	 *            the terminal state
	 * @param duration
	 *            the duration of the action
	 * @return a reinforcement
	 */
	public double unknownR(S state, A action, S terminalState, Integer duration) {
		if (isOptimistic()) {
			if (opType().equals(Optimization.MAXIMIZE)) {
				return rmax();
			} else {
				return rmin();
			}
		} else {
			if (opType().equals(Optimization.MAXIMIZE)) {
				return rmin();
			} else {
				return rmax();
			}
		}
	}

	@Override
	public double tprob(S state, A action, S terminalState, Integer duration) {
		int saCount = counts(state, action);
		if (saCount < numSamplesUntilKnown()) {
			return unknownTProb(state, action, terminalState, duration);
		}
		int sasdCount = counts(state, action, terminalState, duration);
		return ((double) sasdCount) / saCount;
	}

	/**
	 * Computes the transition probability for (state, action, terminalState,
	 * duration) when the specified state-action pair is unknown.
	 * 
	 * @param state
	 *            a state
	 * @param action
	 *            an action
	 * @param terminalState
	 *            the terminal state
	 * @param duration
	 *            the duration of the action
	 * @return the probability of observing the specified sample
	 */
	public double unknownTProb(S state, A action, S terminalState,
			Integer duration) {
		if (terminalState.equals(_dummyState) && (duration == 1)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double dtprob(S state, A action, S terminalState, Integer duration,
			DiscountFactor gamma) {
		return Math.pow(gamma.doubleValue(), duration)
				* tprob(state, action, terminalState, duration);
	}

	@Override
	public int maxActionDuration() {
		return _maxDuration;
	}

	@Override
	public Iterable<Integer> durations(S state, A action, S terminalState) {
		Map<A, Map<S, Set<Integer>>> adurations = _durations.get(state);
		Set<Integer> asddurations = null;
		if (adurations != null) {
			Map<S, Set<Integer>> asdurations = adurations.get(action);
			if (asdurations != null) {
				asddurations = asdurations.get(terminalState);
			}
		}
		Set<Integer> durs = new HashSet<Integer>();
		durs.add(1);

		if (asddurations != null) {
			durs.addAll(asddurations);
		}

		return durs;
	}

	@Override
	public Iterable<S> states() {
		return _states;
	}

	@Override
	public int numberOfStates() {
		return _states.size();
	}

	@Override
	public Iterable<S> successors(S state, A action) {
		Map<A, Set<S>> asuccs = _succs.get(state);
		Set<S> assuccs = null;
		if (asuccs != null) {
			assuccs = asuccs.get(action);
		}

		Set<S> tstates = new HashSet<S>();
		tstates.add(_dummyState);
		if (assuccs != null) {
			tstates.addAll(assuccs);
		}

		return tstates;
	}

	public void update(ActionOutcome<S, A> outcome) {
		update(outcome.state(), outcome.action(), outcome.terminalState(),
				outcome.r(), outcome.duration());
	}

	public void update(S state, A action, S terminalState, double r,
			int duration) {
		if (duration > _maxDuration) {
			_maxDuration = duration;
		}
		// Add the states to the state set
		_states.add(state);
		_states.add(terminalState);

		// Update the successor states
		updateSuccessors(state, action, terminalState);
		updateDurations(state, action, terminalState, duration);

		incSCount(state);
		incSACount(state, action);
		incSASCount(state, action, terminalState);
		incSASDCount(state, action, terminalState, duration);
		updateR(state, action, terminalState, duration, r);
	}

	private void updateSuccessors(S state, A action, S terminalState) {
		Map<A, Set<S>> asuccs = MapUtil.get2ndMap(_succs, state);
		Set<S> succStates = asuccs.get(action);
		if (succStates == null) {
			succStates = new HashSet<S>();
			asuccs.put(action, succStates);
		}
		succStates.add(terminalState);
	}

	private void updateDurations(S state, A action, S terminalState,
			int duration) {
		Map<A, Map<S, Set<Integer>>> adurations = MapUtil.get2ndMap(_durations,
				state);
		Map<S, Set<Integer>> asdurations = MapUtil
				.get2ndMap(adurations, action);
		Set<Integer> asddurations = asdurations.get(terminalState);
		if (asddurations == null) {
			asddurations = new HashSet<Integer>();
			asdurations.put(terminalState, asddurations);
		}
		asddurations.add(duration);
	}

	private void incSCount(S state) {
		MapUtil.inc(_sCounts, state);
	}

	private void incSACount(S state, A action) {
		Map<A, Integer> aCounts = MapUtil.get2ndMap(_saCounts, state);
		MapUtil.inc(aCounts, action);
	}

	private void incSASCount(S state, A action, S terminalState) {
		Map<A, Map<S, Integer>> aCounts = MapUtil.get2ndMap(_sasCounts, state);
		Map<S, Integer> asCounts = MapUtil.get2ndMap(aCounts, action);
		MapUtil.inc(asCounts, terminalState);
	}

	private void incSASDCount(S state, A action, S terminalState, int duration) {
		Map<A, Map<S, Map<Integer, Integer>>> aCounts = MapUtil.get2ndMap(
				_sasdCounts, state);
		Map<S, Map<Integer, Integer>> asCounts = MapUtil.get2ndMap(aCounts,
				action);
		Map<Integer, Integer> asdCounts = MapUtil.get2ndMap(asCounts,
				terminalState);
		MapUtil.inc(asdCounts, duration);
	}

	private void updateR(S state, A action, S terminalState, int duration,
			double r) {
		Map<A, Map<S, Map<Integer, Double>>> aRSum = MapUtil.get2ndMap(_rsum,
				state);
		Map<S, Map<Integer, Double>> asRSum = MapUtil.get2ndMap(aRSum, action);
		Map<Integer, Double> asdRSum = MapUtil.get2ndMap(asRSum, terminalState);

		Double rsum = asdRSum.get(duration);
		if (rsum == null) {
			asdRSum.put(duration, r);
		} else {
			asdRSum.put(duration, rsum.doubleValue() + r);
		}
	}

	public int counts(S state) {
		return MapUtil.getWithDefault(_sCounts, state, 0);
	}

	public int counts(S state, A action) {
		Map<A, Integer> aCounts = MapUtil.get2ndMap(_saCounts, state);
		return MapUtil.getWithDefault(aCounts, action, 0);
	}

	public int counts(S state, A action, S terminalState) {
		Map<A, Map<S, Integer>> aCounts = MapUtil.get2ndMap(_sasCounts, state);
		Map<S, Integer> asCounts = MapUtil.get2ndMap(aCounts, action);
		return MapUtil.getWithDefault(asCounts, terminalState, 0);
	}

	public int counts(S state, A action, S terminalState, Integer duration) {
		Map<A, Map<S, Map<Integer, Integer>>> aCounts = MapUtil.get2ndMap(
				_sasdCounts, state);
		Map<S, Map<Integer, Integer>> asCounts = MapUtil.get2ndMap(aCounts,
				action);
		Map<Integer, Integer> asdCounts = MapUtil.get2ndMap(asCounts,
				terminalState);
		return MapUtil.getWithDefault(asdCounts, duration, 0);
	}

	@Override
	public double rmax() {
		return _rInterval.getSup();
	}

	@Override
	public double rmin() {
		return _rInterval.getInf();
	}

}
