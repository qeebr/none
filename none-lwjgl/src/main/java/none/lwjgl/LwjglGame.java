package none.lwjgl;

import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.input.Keyboard;
import none.engine.component.physic.MasterPhysic;
import none.engine.component.renderer.MasterRenderer;
import none.engine.component.sound.MasterPlayer;
import none.lwjgl.components.GameLoop;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.util.Objects;

/**
 * A LWJGL-Game.
 */
public class LwjglGame extends Game {
    public static final int FPS = 60;
    public static final int THOUSAND_TICKS = 1000;
    public static final int OPENGL_MAJOR_NUMBER = 3;
    public static final int OPENGL_MINOR_NUMBER = 2;

    private long lastAction;
    private GameLoop loopImpl;

    private MasterRenderer masterRenderer;
    private MasterPlayer masterPlayer;
    private MasterPhysic masterPhysic;

    private Keyboard keyboard;

    /**
     * CTor for LwjglGame.
     *
     * @param options  Options for this Game.
     * @param gameLoop The to be executed GameLoop.
     */
    public LwjglGame(GameOptions options, GameLoop gameLoop) {
        super(options);

        this.loopImpl = Objects.requireNonNull(gameLoop);
        this.loopImpl.setGame(this); //Make sure Loop does know us.
    }

    public MasterRenderer getMasterRenderer() {
        return masterRenderer;
    }

    public MasterPlayer getMasterPlayer() {
        return masterPlayer;
    }

    public MasterPhysic getMasterPhysic() {
        return masterPhysic;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    @Override
    protected void init() {
        createDisplay();

        masterRenderer = getInjector().getInstance(MasterRenderer.class);
        masterPlayer = getInjector().getInstance(MasterPlayer.class);
        masterPhysic = getInjector().getInstance(MasterPhysic.class);
        keyboard = getInjector().getInstance(Keyboard.class);

        masterRenderer.init();
        keyboard.init();
        masterPlayer.init();

        getManager().init();

        lastAction = getTime();
    }

    @Override
    protected void gameLoop() {
        loopImpl.doLoop();
    }

    @Override
    protected void dispose() {
        getManager().dispose();

        masterRenderer.dispose();
        masterPlayer.dispose();

        Display.destroy();
    }

    public boolean gameRunning() {
        return !Display.isCloseRequested() && !isQuitGame();
    }

    /**
     * Returns the Delta Time in Ms
     *
     * @return returns the delta time in ms.
     */
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastAction);
        lastAction = time;

        return delta;
    }

    public void waitTimer() {
        Display.sync(FPS);
    }

    public void updateSceen() {
        Display.update();
    }

    private void createDisplay() {
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAttribs = new ContextAttribs(OPENGL_MAJOR_NUMBER, OPENGL_MINOR_NUMBER)
                    .withForwardCompatible(true).withProfileCore(true);

            Display.setDisplayMode(new DisplayMode(getOptions().getDisplayWidth(),
                    getOptions().getDisplayHeight()));
            Display.create(pixelFormat, contextAttribs);
        } catch (LWJGLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the Time in Milliseconds.
     *
     * @return the Time.
     */
    private long getTime() {
        return (Sys.getTime() * THOUSAND_TICKS) / Sys.getTimerResolution();
    }
}
