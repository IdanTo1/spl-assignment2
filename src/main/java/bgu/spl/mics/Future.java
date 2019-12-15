package bgu.spl.mics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A Future object represents a promised result - an object that will eventually be resolved to hold a result of some
 * operation. The class allows Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class. No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

    private T _result;
    private boolean _done;

    /**
     * This should be the the only public constructor in this class.
     *
     * @pre none
     * @post (isDone = = false)
     */
    public Future() {
        _done = false;
        _result = null;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved. This is a blocking method! It waits for the
     * computation in case it has not been completed.
     * <p>
     *
     * @return return the result of type T if it is available, if not wait until it is available.
     * @post (isDone = = true)
     */
    public synchronized T get() {
        while (!_done)
            try {
                this.wait();
            } catch (InterruptedException ignored) {
            }
        return _result;
    }

    /**
     * Resolves the result of this Future object.
     *
     * @pre (isDone () == false)
     * @post (isDone () == true && get() == result)
     */
    public synchronized void resolve(T result) {
        _done = true;
        _result = result;
        notifyAll();
    }

    /**
     * @return true if this object has been resolved, false otherwise
     * @pre none
     * @post none
     */
    public boolean isDone() {
        return _done;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved, This method is non-blocking, it has a
     * limited amount of time determined by {@code timeout}
     * <p>
     *
     * @param timeout the maximal amount of time units to wait for the result.
     * @param unit    the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, wait for {@code timeout} TimeUnits {@code unit}.
     * If time has elapsed, return null.
     * @post (isDone () == true || current_time >= start_time + timeout
     */
    public T get(long timeout, TimeUnit unit) {
        while (!_done)
            try {
                this.wait(TimeUnit.MILLISECONDS.convert(timeout, unit));
            } catch (InterruptedException ignored) {
            }
        return _result;
    }

}
