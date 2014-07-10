package org.github.kingtim1.jmdp.util;

/**
 * Represents the two possible optimization objectives: MINIMIZE or MAXIMIZE.
 * @author Timothy A. Mann
 *
 */
public enum Optimization {
	MINIMIZE,
	MAXIMIZE;
	
	/**
	 * Determines if the first argument is preferable to the second argument.
	 * @param first the first score
	 * @param second the second score
	 * @return true if <code>first</code> is preferable to <code>second</code>
	 */
	public boolean firstIsBetter(double first, double second)
	{
		if(this.equals(MINIMIZE)){
			return first < second;
		}else{
			return first > second;
		}
	}
}
