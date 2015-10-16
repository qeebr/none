package none.engine.component.sound;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * A single Sound effect.
 */
public class Sound extends AbsObject {
    private final String path;
    private PlayState state;

    protected Sound(String name, UUID id, String path) {
        super(name, id);
        Preconditions.checkArgument(StringUtils.isNoneBlank(path));
        this.path = path;
        this.state = PlayState.NONE;
    }

    public String getPath() {
        return path;
    }

    public PlayState getState() {
        return state;
    }

    public void setState(PlayState state) {
        this.state = state;
    }

    public enum PlayState {
        NONE,
        WANT_TO_PLAY,
        PLAYED
    }
}
