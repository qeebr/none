package none.engine.component.common.uuid;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Preset UUIDs.
 */
@Singleton
public class GivenUUID implements UUIDFactory {
    private List<String> uuidList;
    private Iterator<String> uuidIterator;

    public GivenUUID(String... uuids) {
        Preconditions.checkArgument(uuids.length > 0, "No UUID-List.");
        this.uuidList = Arrays.asList(uuids);
        this.uuidIterator = this.uuidList.iterator();
    }

    @Override
    public UUID createUUID() {
        UUID uuid = UUID.fromString(uuidIterator.next());

        if (!uuidIterator.hasNext()) {
            uuidIterator = uuidList.iterator();
        }

        return uuid;
    }

    @Override
    public UUID fromString(String value) {
        return UUID.fromString(value);
    }
}
