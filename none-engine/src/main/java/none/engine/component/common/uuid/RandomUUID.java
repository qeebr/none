package none.engine.component.common.uuid;

import com.google.inject.Singleton;

import java.util.UUID;

/**
 * Creates random UUID's.
 */
@Singleton
public class RandomUUID implements UUIDFactory {

    @Override
    public UUID createUUID() {
        return UUID.randomUUID();
    }

    @Override
    public UUID fromString(String value) {
        return UUID.fromString(value);
    }
}

