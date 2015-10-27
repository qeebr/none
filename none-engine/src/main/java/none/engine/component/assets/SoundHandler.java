package none.engine.component.assets;

import none.engine.component.sound.Sound;

/**
 * Handles loading Sound-Files in Sound-Framework.
 */
public interface SoundHandler<T extends Sound> {

    /**
     * Loads a Sound, from the Filesystem.
     *
     * @param path    Path to file.
     * @param looping Indicates that loaded file is music or a short soundeffect.
     * @return loaded Sound.
     */
    T loadSound(String path, boolean looping);

    /**
     * Disposes a Sound.
     *
     * @param sound The Sound to be disposed.
     */
    void disposeSound(Sound sound);
}
