import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Block {

    private Shape shape;
    private Point2D position;
    private Color color;

    public Block(Shape shape, Point2D position, Color color) {
        this.shape = shape;
        this.position = position;
        this.color = color;
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.draw(getTransformedShape());
        graphics.setColor(color);
        graphics.fill(getTransformedShape());
    }

    public Shape getTransformedShape() {
        return getTransform().createTransformedShape(shape);
    }

    public AffineTransform getTransform() {
        AffineTransform transform = new AffineTransform();
        transform.translate(position.getX(), position.getY());
        return transform;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }

}
