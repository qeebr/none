package none.engine.component.assets;

import none.engine.component.sound.Sound;

/**
 * Handles loading Sound-Files in Sound-Framework.
 */
public interface SoundHandler<T extends Sound> {

    T loadSound(String path, boolean looping);

    void disposeSound(Sound effect);
}
