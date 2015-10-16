package none.engine;

import com.google.inject.Binder;
import com.google.inject.Module;
import none.engine.component.common.time.Clock;
import none.engine.component.common.time.SystemClock;
import none.engine.component.common.uuid.RandomUUID;
import none.engine.component.common.uuid.UUIDFactory;

import java.util.Objects;

/**
 * Binds all often needed interfaces.
 */
public class GameModule implements Module {

    private final Game game;

    public GameModule(Game game) {
        this.game = Objects.requireNonNull(game, "game");
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Game.class).toInstance(game);

        binder.bind(Clock.class).to(SystemClock.class);
        binder.bind(UUIDFactory.class).to(RandomUUID.class);
    }
}
