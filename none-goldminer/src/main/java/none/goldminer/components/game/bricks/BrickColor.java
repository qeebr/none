package none.goldminer.components.game.bricks;

/**
 * The Color of a Brick.
 */
public enum BrickColor {
    BLUE(1),
    GREEN(2),
    GREY(3),
    PURPLE(4),
    RED(5),
    YELLOW(6);

    private int column;

    BrickColor(int column) {
        this.column = column;
    }

    public static BrickColor valueOf(int id) {
        for (BrickColor color : BrickColor.values()) {
            if (color.getColumn() == id) {
                return color;
            }
        }

        throw new IllegalArgumentException("Unknown Column " + id);
    }

    public int getColumn() {
        return column;
    }
}
