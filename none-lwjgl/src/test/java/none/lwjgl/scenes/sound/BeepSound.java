package none.lwjgl.scenes.sound;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.assets.SoundHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.Command;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import none.engine.component.renderer.CameraComponent;
import none.engine.component.renderer.OrthographicCamera;
import none.engine.component.renderer.Text;
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

    private KeyboardComponent keyboardComponent;
    private CameraComponent cameraComponent;

    private Sound sound;

    public BeepSound(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);

        uuidFactory = Objects.requireNonNull(factory, "factory");
    }

    @Override
    public CameraComponent getActiveCamera() {
        return cameraComponent;
    }

    @Override
    public void init() {
        OrthographicCamera camera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        camera.setFrustum(-100, 100, -100, 100, -100, 100);
        cameraComponent = camera;

        SoundHandler soundHandler = getGame().getInjector().getInstance(SoundHandler.class);
        sound = soundHandler.loadSound("sound/beep.wav", false);
        addObject(sound);

        String text = "Press Space to hear a beep.";
        int textSize = 8;
        Text explaination = new Text(uuidFactory.createUUID(), text, textSize, new Vector3d(-(text.length() / 2) * textSize, 0, 0));
        addObject(explaination);

        keyboardComponent = getGame().getInjector().getInstance(KeyboardComponent.class);
        keyboardComponent.registerCommand(playSound, Key.SPACE);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);

        if (keyboardComponent.isCommandClicked(playSound)) {
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