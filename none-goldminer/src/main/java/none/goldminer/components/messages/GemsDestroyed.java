package none.goldminer.components.messages;

import none.engine.component.messages.Message;

/**
 * Message when Gems where destroyed.
 */
public class GemsDestroyed implements Message {
    private int gemCount;
    private int gemLevel;

    public GemsDestroyed(int gemCount, int gemLevel) {
        this.gemCount = gemCount;
        this.gemLevel = gemLevel;
    }

    public int getGemCount() {
        return gemCount;
    }

    public int getGemLevel() {
        return gemLevel;
    }
}
