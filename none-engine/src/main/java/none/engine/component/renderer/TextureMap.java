package none.engine.component.renderer;

import java.util.List;

/**
 * A TextureMap.
 */
public class TextureMap {

    private int width;
    private int height;
    private List<TexturePosition> positions;

    public TextureMap() {

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<TexturePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TexturePosition> positions) {
        this.positions = positions;
    }
}
