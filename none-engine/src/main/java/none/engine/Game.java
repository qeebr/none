package none.engine;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import none.engine.component.messages.MessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Game container.
 * Needs to be implemented with frameworks for graphics/sound etc.
 */
public abstract class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final GameOptions options;
    private final MessageBus messageBus;
    private Injector injector;
    private SceneManager manager;

    public Game(GameOptions options) {
        this.options = Preconditions.checkNotNull(options);
        this.messageBus = new MessageBus();
    }

    /**
     * Returns the SceneManager.
     *
     * @return SceneManager.
     */
    public SceneManager getManager() {
        return manager;
    }

    /**
     * Returns the GameOptions.
     *
     * @return GameOptions.
     */
    public GameOptions getOptions() {
        return options;
    }

    /**
     * Returns the Dependency-Injector.
     *
     * @return Injector.
     */
    public Injector getInjector() {
        return injector;
    }


    public MessageBus getMessageBus() {
        return messageBus;
    }

    /**
     * Starts the Game, and set the SceneManager and Injector for the execution.
     * Initialises the Game, executes the GameLoop and disposes after GameLoop has been left.
     * GameLoop is Single-Threaded, executes update, draw and an waiting step.
     *
     * @param manager  The Games SceneManger.
     * @param injector The Injector SceneManager.
     */
    public void run(SceneManager manager, Injector injector) {
        this.manager = Preconditions.checkNotNull(manager);
        this.injector = Preconditions.checkNotNull(injector);
        LOGGER.info("Start Initialisation");
        init();
        LOGGER.info("Initialisation done -> Start Game-Loop");

        while (gameRunning()) {
            int delta = getDelta();

            update(delta);
            draw(delta);

            waitTimer();
        }

        LOGGER.info("Game done -> Disposing");
        dispose();
        LOGGER.info("Disposing done");
    }

    /**
     * Leaves the GameLoop and stops the Game execution.
     */
    public abstract void stop();

    protected abstract void init();

    protected abstract void update(int delta);

    protected abstract void draw(int delta);

    protected abstract void dispose();

    protected abstract void waitTimer();

    protected abstract boolean gameRunning();

    protected abstract int getDelta();
}
