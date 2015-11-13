package none.engine.component.renderer.primitives;

import com.google.common.base.Preconditions;
import none.engine.component.AbsObject;

import java.util.UUID;

/**
 * A 2D-Sprite. Contains every information, a sprite contains.
 */
public class Sprite extends AbsObject {
    public static final String NAME = "Sprite";

    private final int maxColumns;
    private final int maxRows;
    private final int height;
    private final int width;
    private final int layer;

    private int column;
    private int row;

    public Sprite(UUID id, int maxRows, int maxColumns, int height, int width) {
        this(id, maxRows, maxColumns, height, width, 1);
    }

    public Sprite(UUID id, int maxRows, int maxColumns, int height, int width, int layer) {
        super(NAME, id);
        Preconditions.checkArgument(maxRows > 0, "maxRows > 0");
        Preconditions.checkArgument(maxColumns > 0, "maxColumns > 0");
        Preconditions.checkArgument(height > 0, "height > 0");
        Preconditions.checkArgument(width > 0, "width > 0");
        Preconditions.checkArgument(layer > 0, "layer > 0");

        this.maxColumns = maxColumns;
        this.maxRows = maxRows;

        this.height = height;
        this.width = width;
        this.layer = layer;

        this.column = 1;
        this.row = 1;
    }

    /**
     * Returns the current Column. (Contains values from 1 to inclusive MaxColumn).
     *
     * @return Sprite Column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the current Column. (Contains values from 1 to inclusive MaxColumn).
     *
     * @param column the new column.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Returns the current Row. (Contains values from 1 to inclusive MaxColumn).
     *
     * @return Sprite Row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the current Row. (Contains values from 1 to inclusive MaxRow).
     *
     * @param row the new row.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * The Sprite height in pixels.
     *
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The Sprite width in pixels.
     *
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The Higher Layer overlapping lower layers.
     *
     * @return Layer.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * The maximum count of Columns in loaded texture/sprite.
     *
     * @return Count of Columns.
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * The maximum count of Rows in loaded texture/sprite.
     *
     * @return Count of Rows.
     */
    public int getMaxRows() {
        return maxRows;
    }
}
