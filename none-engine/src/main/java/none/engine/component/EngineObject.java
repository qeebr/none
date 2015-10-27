package none.engine.component;

import none.engine.Game;

import java.util.Optional;
import java.util.UUID;

/**
 * An Object in the Game.
 */
public interface EngineObject {

    /**
     * Immutable Id.
     *
     * @return Object-Id.
     */
    UUID getId();

    /**
     * Solves the for a class specified name.
     *
     * @return Object-Class-Name
     */
    String getName();

    /**
     * Returns the Parent-EngineObject.
     *
     * @return Parent-EngineObject, or null when SceneManager.
     */
    EngineObject getParent();

    /**
     * Returns the Game. In which this EngineObject is executed.
     *
     * @return Game.
     */
    Game getGame();

    /**
     * Initializes the EngineObject. Loading Resources creating children etc.
     */
    void init();

    /**
     * Executs the logic for this EngineObject.
     *
     * @param deltaInMs the delta time from last update in milliseconds.
     */
    void update(int deltaInMs);

    /**
     * Disposes Resources needed from this EngineObject.
     */
    void dispose();

    /**
     * Search inside the object for given Object-Name.
     *
     * @param name EngineObject-Name.
     * @return Optional.
     */
    <K extends EngineObject> Optional<K> find(String name);

    /**
     * Search inside the object for given UUID.
     *
     * @param id id.
     * @return Optional.
     */
    <K extends EngineObject> Optional<K> find(UUID id);

    /**
     * Returns an Iterable for this EngineObject.
     *
     * @return Iterable.
     */
    Iterable<EngineObject> children();
}
