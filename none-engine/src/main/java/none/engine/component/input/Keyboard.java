package none.engine.component.input;

import java.util.UUID;

/**
 * The Keyboard.
 */
public abstract class Keyboard extends Commandable<Key> {
    public static final String NAME = "Keyboard";

    protected Keyboard() {
        super(NAME, UUID.randomUUID());
    }
}
