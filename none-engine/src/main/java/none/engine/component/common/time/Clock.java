package none.engine.component.common.time;

/**
 * Returns current SystemTime.
 */
public interface Clock {
    /**
     * Returns the current systemtime in milliseconds.
     *
     * @return time in ms.
     */
    long theTimeMilliSeconds();
}

