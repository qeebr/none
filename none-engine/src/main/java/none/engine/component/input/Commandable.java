package none.engine.component.input;

import none.engine.component.AbsObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A abstract base class for classes who wants to use command classes.
 */
public abstract class Commandable<K> extends AbsObject {
    private final Map<Command, K> commandsToId;
    private final Map<Command, Boolean> commandState;
    private final Map<Command, Boolean> oldCommandState;


    protected Commandable(String name, UUID uuid) {
        super(name, uuid);

        commandsToId = new HashMap<>();
        commandState = new HashMap<>();
        oldCommandState = new HashMap<>();
    }

    /**
     * Registers a Command-class.
     *
     * @param command The registered Command.
     * @param key     The associated Key.
     */
    public void registerCommand(Command command, K key) {
        commandsToId.put(command, key);
    }

    /**
     * Deregisters a Command-class.
     *
     * @param command Command-class to be deregistered.
     */
    public void deregisterCommand(Command command) {
        commandsToId.remove(command);
        commandState.remove(command);
        oldCommandState.remove(command);
    }

    /**
     * Checks if this Command is in this update-cycle down.
     *
     * @param command The command-class.
     * @return boolean indicating if the command is currently pressed.
     */
    public boolean isCommandDown(Command command) {
        return commandState.getOrDefault(command, false);
    }

    /**
     * Checks if given Command, was in LAST update-cycle UP and is CURRENTLY DOWN.
     *
     * @param command The command-class.
     * @return boolean indicating that command was just pressed.
     */
    public boolean isCommandClicked(Command command) {
        return !oldCommandState.getOrDefault(command, false)
                && commandState.getOrDefault(command, false);
    }

    /**
     * Checks if given Command, was in LAST update-cycle DOWN and is CURRENTLY UP.
     *
     * @param command The command-class.
     * @return boolean indicating that command was just released.
     */
    public boolean isCommandReleased(Command command) {
        return oldCommandState.getOrDefault(command, false)
                && !commandState.getOrDefault(command, false);
    }

    protected abstract boolean isIdDown(K key);

    @Override
    public void update(int deltaInMs) {
        for (Map.Entry<Command, K> entry : commandsToId.entrySet()) {
            oldCommandState.put(entry.getKey(), commandState.getOrDefault(entry.getKey(), false));

            commandState.put(entry.getKey(), isIdDown(entry.getValue()));
        }
    }
}
