/**
	PolicyEvaluation.java

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.github.kingtim1.jmdp.DP;
import com.github.kingtim1.jmdp.FiniteStateMDP;
import com.github.kingtim1.jmdp.StationaryPolicy;

/**
 * Exact policy evaluation via matrix inversion. This algorithm will only work
 * for MDPs with a moderate number of states and actions.
 * 
 * @author Timothy A. Mann
 *
 * @param <S>
 *            the state type
 * @param <A>
 *            the action type
 */
public class PolicyEvaluation<S, A> implements DP<DiscountedVFunction<S>> {

	private FiniteStateMDP<S, A> _mdp;
	private DiscountFactor _df;
	private StationaryPolicy<S,A> _policy;

	public PolicyEvaluation(FiniteStateMDP<S, A> mdp, DiscountFactor df, StationaryPolicy<S,A> policy) {
		_mdp = mdp;
		_df = df;
		_policy = policy;
	}

	@Override
	public DiscountedVFunction<S> run() {
		int n = _mdp.numberOfStates();
		List<S> states = new ArrayList<S>(n);
		Iterable<S> istates = _mdp.states();
		for(S state : istates){
			states.add(state);
		}
		
		// Construct matrix A and vector b
		RealMatrix id = MatrixUtils.createRealIdentityMatrix(n);
		RealMatrix gpp = gammaPPi(states);
		RealMatrix A = id.subtract(gpp);
		RealVector b = rPi(states);
		
		// Solve for V^{\pi}
		RealMatrix Ainv = MatrixUtils.inverse(A);
		RealVector vpi = Ainv.operate(b);
		
		// Construct the value function
		Map<S,Double> valueMap = new HashMap<S,Double>();
		for(int i=0;i<states.size(); i++){
			S state = states.get(i);
			double val = vpi.getEntry(i);
			valueMap.put(state, val);
		}
		
		return new MapVFunction<S>(valueMap, 0);
	}
	
	private RealMatrix gammaPPi(List<S> states){
		int n = _mdp.numberOfStates();
		double[][] gpp = new double[n][n];
		
		// Fill the matrix
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				gpp[i][j] = gammaPPi(states, i, j);
			}
		}
		
		return new Array2DRowRealMatrix(gpp);
	}
	
	private double gammaPPi(List<S> states, int statei, int statej){
		S state = states.get(statei);
		S nextState = states.get(statej);
		
		if(_policy.isDeterministic()){
			A action = _policy.policy(state);
			return _df.doubleValue() * _mdp.tprob(state, action, nextState);
		}else{
			double vavg = 0;
			Iterable<A> actions = _mdp.actions(state);
			for(A action : actions){
				vavg += _policy.aprob(state, action) * _mdp.tprob(state, action, nextState);
			}
			return _df.doubleValue() * vavg;
		}
	}
	
	private RealVector rPi(List<S> states){
		int n = _mdp.numberOfStates();
		double[] rp = new double[n];
		
		// Fill the vector
		for(int i=0;i<n;i++){
			rp[i] = rPi(states, i);
		}
		
		return new ArrayRealVector(rp);
	}
	
	private double rPi(List<S> states, int statei){
		S state = states.get(statei);
		if(_policy.isDeterministic()){
			A action = _policy.policy(state);
			double ravg = 0;
			for(int j=0;j<_mdp.numberOfStates();j++){
				S nextState = states.get(j);
				ravg += _mdp.tprob(state, action, nextState) * _mdp.r(state, action, nextState);
			}
			return ravg;
		}else{
			Iterable<A> actions = _mdp.actions(state);
			double ravg = 0;
			for(A action : actions){
				double aprob = _policy.aprob(state, action);
				for(int j=0;j<_mdp.numberOfStates();j++){
					S nextState = states.get(j);
					double tprob = _mdp.tprob(state, action, nextState);
					double r = _mdp.r(state, action, nextState);
					ravg += aprob * tprob * r;
				}
			}
			return ravg;
		}
	}

}
