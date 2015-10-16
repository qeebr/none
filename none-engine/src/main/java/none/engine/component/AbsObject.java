package none.engine.component;

import com.google.common.base.Preconditions;
import none.engine.Game;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * An abstract base class. Handles getter/setter for Name, and Game.
 */
public abstract class AbsObject implements EngineObject {
    private static final ArrayList<EngineObject> EMPTY_LIST = new ArrayList<>();
    private final String name;
    private final UUID id;

    private EngineObject parent;
    private Game game;

    protected AbsObject(String name, UUID id) {
        this.id = Preconditions.checkNotNull(id, "id");
        Preconditions.checkArgument(StringUtils.isNotEmpty(name));
        this.name = name;
    }

    protected AbsObject(String name, UUID id, Game game) {
        this(name, id);
        this.game = Preconditions.checkNotNull(game, "game");
    }

    protected AbsObject(String name, UUID id, Game game, EngineObject parent) {
        this(name, id, game);
        this.parent = Preconditions.checkNotNull(parent);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public EngineObject getParent() {
        return parent;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(int delta) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public <T extends EngineObject> Optional<T> find(String name) {
        return Optional.empty();
    }

    @Override
    public <T extends EngineObject> Optional<T> find(UUID id) {
        return Optional.empty();
    }

    @Override
    public Iterable<EngineObject> children() {
        return EMPTY_LIST;
    }
}
