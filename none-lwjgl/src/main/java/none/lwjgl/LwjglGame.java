package none.lwjgl;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.GameOptions;
import none.engine.component.input.KeyboardComponent;
import none.engine.component.physic.MasterPhysic;
import none.engine.component.renderer.MasterRenderer;
import none.engine.component.sound.MasterPlayer;
import none.lwjgl.components.physic.MasterPhysicImpl;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * A LWJGL-Game.
 */
public class LwjglGame extends Game {
    public static final int FPS = 60;
    public static final int THOUSAND_TICKS = 1000;
    public static final int OPENGL_MAJOR_NUMBER = 3;
    public static final int OPENGL_MINOR_NUMBER = 2;

    private long lastAction;
    private boolean gameRunning;
    private MasterRenderer masterRenderer;
    private MasterPlayer masterPlayer;
    private MasterPhysic masterPhysic;

    private KeyboardComponent keyboardComponent;

    @Inject
    public LwjglGame(GameOptions options) {
        super(options);
        this.gameRunning = true;
    }

    @Override
    public void stop() {
        this.gameRunning = false;
    }

    @Override
    protected void init() {
        createDisplay();

        masterRenderer = getInjector().getInstance(MasterRenderer.class);
        masterPlayer = getInjector().getInstance(MasterPlayer.class);
        masterPhysic = getInjector().getInstance(MasterPhysicImpl.class);
        keyboardComponent = getInjector().getInstance(KeyboardComponent.class);

        masterRenderer.init();
        keyboardComponent.init();
        masterPlayer.init();

        getManager().init();

        lastAction = getTime();
    }

    @Override
    protected void update(int delta) {
        updateInput(delta);

        getManager().update(delta);
        masterPhysic.update(delta, getManager().getCurrentScene());
    }

    private void updateInput(int delta) {
        keyboardComponent.update(delta);
    }

    @Override
    protected void draw(int delta) {

        masterRenderer.draw(getManager().getCurrentScene());
        masterPlayer.play(getManager().getCurrentScene());

        Display.update();
    }

    @Override
    protected void dispose() {
        getManager().dispose();

        masterRenderer.dispose();

        masterPlayer.dispose();

        Display.destroy();
    }

    @Override
    protected void waitTimer() {
        Display.sync(FPS);
    }

    @Override
    protected boolean gameRunning() {
        return !Display.isCloseRequested() && gameRunning;
    }

    @Override
    protected int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastAction);
        lastAction = time;

        return delta;
    }

    protected long getTime() {
        return (Sys.getTime() * THOUSAND_TICKS) / Sys.getTimerResolution();
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
}
