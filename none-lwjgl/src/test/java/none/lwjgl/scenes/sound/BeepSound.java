package none.lwjgl.scenes.sound;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.assets.SoundHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.primitives.Text;
import none.engine.component.sound.Sound;
import none.engine.scenes.Scene;
import org.joml.Vector3d;

import java.util.Objects;

/**
 * Plays a beep sound.
 */
public class BeepSound extends AbsStructObject<EngineObject> implements Scene {
    public static final String NAME = BeepSound.class.getSimpleName();

    private final UUIDFactory uuidFactory;


    private PlaySound playSound = new PlaySound();

    private Keyboard keyboard;
    private Camera camera;

    private Sound sound;

    public BeepSound(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);

        uuidFactory = Objects.requireNonNull(factory, "factory");
    }

    @Override
    public Camera getActiveCamera() {
        return camera;
    }

    @Override
    public void init() {
        OrthographicCamera camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-100, 100, -100, 100, -100, 100);
        this.camera = camera;

        SoundHandler soundHandler = getGame().getInjector().getInstance(SoundHandler.class);
        sound = soundHandler.loadSound("sound/beep.wav", false);
        addObject(sound);

        String text = "Press Space to hear a beep.";
        int textSize = 8;
        Text explaination = new Text(uuidFactory.createUUID(), text, textSize);
        Transform transform = new Transform(uuidFactory.createUUID(), new Vector3d(-(text.length() / 2) * textSize, 0, 0));
        addObject(new Renderable("Explaining Text", uuidFactory.createUUID(), explaination, transform));

        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        keyboard.registerCommand(playSound, Key.SPACE);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);

        if (keyboard.isCommandClicked(playSound)) {
            sound.setState(Sound.PlayState.WANT_TO_PLAY);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        SoundHandler soundHandler = getGame().getInjector().getInstance(SoundHandler.class);
        soundHandler.disposeSound(sound);
        sound = null;

        removeAllObjects();
    }

    private class PlaySound implements Command {

    }
}
