package com.github.kingtim1.jmdp.util;

/**
 * A factory can generate new instances of a type with zero parameters.
 * @author Timothy A. Mann
 *
 * @param <X> the type of instances that this factory constructs
 */
public interface Factory<X> {
	/**
	 * Constructs a new instance of type <code>X</code>.
	 * @return a new instance
	 */
	public X newInstance();
}
