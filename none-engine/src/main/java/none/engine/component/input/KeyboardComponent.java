package none.engine.component.input;

import none.engine.component.AbsObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The KeyboardComponent.
 */
public abstract class KeyboardComponent extends AbsObject {
    public static final String NAME = "KeyboardComponent";

    private Map<Command, Key> commandsToId;
    private Map<Command, Boolean> commandState;
    private Map<Command, Boolean> oldCommandState;

    protected KeyboardComponent() {
        super(NAME, UUID.randomUUID());

        commandsToId = new HashMap<>();
        commandState = new HashMap<>();
        oldCommandState = new HashMap<>();
    }

    public void registerCommand(Command command, Key key) {
        commandsToId.put(command, key);
    }

    public void deregisterCommand(Command command) {
        commandsToId.remove(command);
        commandState.remove(command);
        oldCommandState.remove(command);
    }

    public boolean isCommandDown(Command command) {
        return commandState.getOrDefault(command, false);
    }

    public boolean isCommandClicked(Command command) {
        return !oldCommandState.getOrDefault(command, false)
                && commandState.getOrDefault(command, false);
    }

    public boolean isCommandReleased(Command command) {
        return oldCommandState.getOrDefault(command, false)
                && !commandState.getOrDefault(command, false);
    }

    protected abstract boolean isIdDown(Key keyId);

    @Override
    public void update(int delta) {
        for (Map.Entry<Command, Key> entry : commandsToId.entrySet()) {
            oldCommandState.put(entry.getKey(), commandState.getOrDefault(entry.getKey(), false));

            commandState.put(entry.getKey(), isIdDown(entry.getValue()));
        }
    }
}
