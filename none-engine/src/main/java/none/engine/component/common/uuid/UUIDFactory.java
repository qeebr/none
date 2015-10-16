package none.engine.component.common.uuid;

import java.util.UUID;

/**
 * Builds Uuid.
 */
public interface UUIDFactory {

    UUID createUUID();

    UUID fromString(String value);
}
