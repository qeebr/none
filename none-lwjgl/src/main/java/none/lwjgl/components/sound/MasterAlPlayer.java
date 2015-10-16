package none.lwjgl.components.sound;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.component.EngineObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.sound.MasterPlayer;
import none.engine.component.sound.Sound;
import none.engine.scenes.Scene;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import java.nio.FloatBuffer;

/**
 * Plays sounds and music from a scene.
 */
public class MasterAlPlayer extends MasterPlayer {
    public static final String NAME = "MasterAlPlayer";

    private FloatBuffer listenerPos;
    private FloatBuffer listenerVel;
    private FloatBuffer listenerOri;

    private FloatBuffer sourcePos;
    private FloatBuffer sourceVel;

    @Inject
    public MasterAlPlayer(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);
    }

    @Override
    public void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            throw new IllegalStateException(e);
        }

        listenerPos = BufferUtils.createFloatBuffer(3);
        listenerVel = BufferUtils.createFloatBuffer(3);
        listenerOri = BufferUtils.createFloatBuffer(6);

        sourcePos = BufferUtils.createFloatBuffer(3);
        sourceVel = BufferUtils.createFloatBuffer(3);
    }

    @Override
    public void play(Scene scene) {
        setListenerValues();

        iterateThroughSceneText(scene.children());
    }

    private void iterateThroughSceneText(Iterable<EngineObject> children) {
        for (EngineObject child : children) {
            if (AlSound.NAME.equals(child.getName())) {
                playSound((AlSound) child);
            } else {
                iterateThroughSceneText(child.children());
            }
        }
    }

    private void playSound(AlSound child) {
        if (child.getState() == Sound.PlayState.WANT_TO_PLAY) {
            sourcePos.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
            sourceVel.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();

            //TODO use real coordinates.
            AL10.alSource(child.getSource().get(0), AL10.AL_POSITION, sourcePos);
            AL10.alSource(child.getSource().get(0), AL10.AL_VELOCITY, sourceVel);

            AL10.alSourcePlay(child.getSource().get(0));
            child.setState(Sound.PlayState.PLAYED);
        }
    }

    private void setListenerValues() {
        //TODO use camera.
        listenerPos.clear();
        listenerVel.clear();
        listenerOri.clear();
        listenerPos.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
        listenerVel.put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
        listenerOri.put(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).rewind();

        AL10.alListener(AL10.AL_POSITION, listenerPos);
        AL10.alListener(AL10.AL_VELOCITY, listenerVel);
        AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    }

    @Override
    public void dispose() {
        AL.destroy();
    }
}
