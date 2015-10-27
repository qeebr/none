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
    private boolean quitGame;

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

    /**
     * Returns the Message Bus.
     *
     * @return Message Bus.
     */
    public MessageBus getMessageBus() {
        return messageBus;
    }

    /**
     * A In-Game required desire to quit the game.
     * This Method does not cover "Windows-Management" flags.
     *
     * @return Bool which indicate, user wants to quit game.
     */
    public boolean isQuitGame() {
        return quitGame;
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
        this.quitGame = false;

        LOGGER.info("Start Initialisation");
        init();
        LOGGER.info("Initialisation done -> Start Game-Loop");

        gameLoop();

        LOGGER.info("Game done -> Disposing");
        dispose();
        LOGGER.info("Disposing done");
    }

    /**
     * Leaves the GameLoop and stops the Game execution.
     */
    public void stop() {
        quitGame = true;
    }

    /**
     * Initialises the Game. Should be used to create Window and to call init on first scene.
     */
    protected abstract void init();

    /**
     * Implementation for GameLoop. Different-Games need different loops.
     */
    protected abstract void gameLoop();

    /**
     * After GameLoop have been quit, resources should be disposed.
     */
    protected abstract void dispose();

}
