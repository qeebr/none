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

    Game getGame();

    void init();

    void update(int delta);

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

    Iterable<EngineObject> children();
}
