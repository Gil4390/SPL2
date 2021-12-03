package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	private T result;
	private boolean done;
	/**
	 * This should be the only public constructor in this class.
	 */
	public Future(T result) {
		//TODO: implement this
		this.result=result;
		done = false;
	}

	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     *
	 * @inv this.result = @pre this.result;
	 * @inv this.done = @pre this.done;
     */
	public T get() {
		//TODO: implement this.
		if(done)
			return result;
		//wait until done to be true
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
	 * @pre this.done = false;
	 * @pre this.result = null;
	 * @post this.done = true;
	 * @post this.result = result;
     */
	public void resolve (T result) {
		//TODO: implement this.
		//need to comutation
		done = true;
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
	 * @inv this.done = @pre this.done;
     */
	public boolean isDone() {
		//TODO: implement this.
		return done;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) {
		//TODO: implement this.
		if(done)
			return result;
		else{
			//unit.sleep(timeout);
			if(done)
				return result;
		}
		return null;
	}

}
