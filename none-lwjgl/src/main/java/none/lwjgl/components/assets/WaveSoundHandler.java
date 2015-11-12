package none.lwjgl.components.assets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.assets.Assets;
import none.engine.component.assets.SoundHandler;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.sound.Sound;
import none.lwjgl.components.sound.AlSound;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.Objects;

/**
 * A simple Wave SoundHandler.
 */
@Singleton
public class WaveSoundHandler extends BaseHandler<AlSound, String> implements SoundHandler<AlSound> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaveSoundHandler.class);

    private final UUIDFactory uuidFactory;

    @Inject
    public WaveSoundHandler(UUIDFactory uuidFactory, Assets assets) {
        super(assets);
        this.uuidFactory = Objects.requireNonNull(uuidFactory);
    }

    @Override
    public AlSound loadSound(String path, boolean looping) {
        AlSound sound = checkLoaded(path);

        if (sound == null) {
            LOGGER.info("Load Sound: {}", path);

            sound = loadWaveData(path, looping);
            addResource(path, sound);
        }

        return sound;
    }

    private AlSound loadWaveData(String path, boolean looping) {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        IntBuffer source = BufferUtils.createIntBuffer(1);

        AL10.alGenBuffers(buffer);

        WaveData waveFile = WaveData.create(getAssets().getClass().getResourceAsStream(path));
        AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();

        AL10.alGenSources(source);

        AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer.get(0));
        AL10.alSourcef(source.get(0), AL10.AL_PITCH, 1.0f);
        AL10.alSourcef(source.get(0), AL10.AL_GAIN, 1.0f);
        AL10.alSourcei(source.get(0), AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);

        return new AlSound(uuidFactory.createUUID(), path, buffer, source);
    }

    @Override
    public void disposeSound(Sound effect) {
        if (!(effect instanceof AlSound)) {
            throw new IllegalStateException("To disposing effect is not a AlSound.");
        }

        disposeSound((AlSound) effect);
    }

    private void disposeSound(AlSound effect) {
        if (dispose(effect, effect.getPath())) {
            LOGGER.info("Dispose Sound: {}", effect.getPath());

            AL10.alDeleteSources(effect.getSource());
            AL10.alDeleteBuffers(effect.getBuffer());
        }
    }
}
