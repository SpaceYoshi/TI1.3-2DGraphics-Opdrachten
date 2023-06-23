import java.awt.*;
import java.awt.geom.Line2D;

public class ColorLine {
    private final Line2D line;
    private final Color color;

    public ColorLine(Line2D line, Color color) {
        this.line = line;
        this.color = color;
    }

    public Line2D getLine() {
        return line;
    }

    public Color getColor() {
        return color;
    }

}
