package none.engine.component.common.uuid;

import java.util.UUID;

/**
 * Builds Uuid.
 */
public interface UUIDFactory {

    /**
     * Creates a UUID.
     *
     * @return UUID.
     */
    UUID createUUID();

    /**
     * Creates a UUID from given String.
     *
     * @param value String representing UUID.
     * @return UUID.
     */
    UUID fromString(String value);
}
