package none.engine.component.common.time;

import com.google.inject.Singleton;

/**
 * Uses System.nanoTime.
 */
@Singleton
public class SystemClock implements Clock {
    private static final int TO_MILLISECONDS = 1000000;

    @Override
    public long theTimeMilliSeconds() {
        return System.nanoTime() / TO_MILLISECONDS;
    }
}

