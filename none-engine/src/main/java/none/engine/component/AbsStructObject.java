package none.engine.component;

import none.engine.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Abstract base class for a structural Element in Engine.
 */
public abstract class AbsStructObject<T extends EngineObject> extends AbsObject {
    private final List<T> gameObjects = new ArrayList<>();
    private final List<T> toBeAdded = new ArrayList<>();
    private final List<T> toBeRemoved = new ArrayList<>();

    protected AbsStructObject(String name, UUID id) {
        super(name, id);
    }

    protected AbsStructObject(String name, UUID id, Game game) {
        super(name, id, game);
    }

    protected AbsStructObject(String name, UUID id, Game game, EngineObject parent) {
        super(name, id, game, parent);
    }

    public Iterable<T> getObjectIterator() {
        return gameObjects;
    }

    public int objectsCount() {
        return gameObjects.size() + toBeAdded.size() - toBeRemoved.size();
    }

    public void removeObject(T gameObject) {
        toBeRemoved.add(gameObject);
    }

    public void removeAllObjects() {
        toBeRemoved.addAll(gameObjects);
    }

    public void addObject(T gameObject) {
        toBeAdded.add(gameObject);
    }

    @Override
    public void init() {
        updateGameObject();
        for (T object : gameObjects) {
            object.init();
        }
    }

    @Override
    public void update(int delta) {
        updateGameObject();
        for (T object : gameObjects) {
            object.update(delta);
        }
    }

    @Override
    public void dispose() {
        updateGameObject();
        for (T object : gameObjects) {
            object.dispose();
        }
    }

    @Override
    public Optional<T> find(UUID objectId) {
        for (T object : gameObjects) {
            if (object.getId().equals(objectId)) {
                return Optional.of(object);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<T> find(String name) {
        for (T object : gameObjects) {
            if (object.getName().equals(name)) {
                return Optional.of(object);
            }
        }

        return Optional.empty();
    }

    @Override
    public Iterable<EngineObject> children() {
        return (Iterable<EngineObject>) getObjectIterator();
    }

    private void updateGameObject() {
        if (!toBeAdded.isEmpty()) {
            for (T object : toBeAdded) {
                gameObjects.add(object);
            }
            toBeAdded.clear();
        }

        if (!toBeRemoved.isEmpty()) {
            for (T object : toBeRemoved) {
                gameObjects.remove(object);
            }
            toBeRemoved.clear();
        }
    }
}
