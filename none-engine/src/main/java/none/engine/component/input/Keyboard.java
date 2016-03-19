package none.engine.component.input;

import java.util.List;
import java.util.UUID;

/**
 * The Keyboard.
 */
public abstract class Keyboard extends Commandable<Key> {
    public static final String NAME = "Keyboard";

    protected Keyboard() {
        super(NAME, UUID.randomUUID());
    }

    /**
     * A List of Character which are where pressed.
     *
     * @return List of pressed characters.
     */
    public abstract List<Character> getCurrentCharacters();
}
