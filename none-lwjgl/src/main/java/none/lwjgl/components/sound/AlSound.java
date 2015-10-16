package none.lwjgl.components.sound;

import none.engine.component.sound.Sound;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.UUID;

/**
 * A Sound for OpenAL.
 */
public class AlSound extends Sound {
    public static final String NAME = "AlSound";

    private final IntBuffer buffer;
    private final IntBuffer source;

    public AlSound(UUID id, String path, IntBuffer buffer, IntBuffer source) {
        super(NAME, id, path);
        this.buffer = Objects.requireNonNull(buffer);
        this.source = Objects.requireNonNull(source);
    }

    public IntBuffer getBuffer() {
        return buffer;
    }

    public IntBuffer getSource() {
        return source;
    }
}
