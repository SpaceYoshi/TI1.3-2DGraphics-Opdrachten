import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Camera {

    private Point2D position;
    private float zoom;

    public Camera(Point2D position, float zoom) {
        this.position = position;
        this.zoom = zoom;
    }

    public AffineTransform getTransform() {
        AffineTransform transform = new AffineTransform();
        transform.translate(position.getX(), position.getY());
        transform.scale(zoom, zoom);
        return transform;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        if (zoom < 0.1) this.zoom = 0.1f; // Prevent zoom <= 0
        else this.zoom = zoom;
    }

}
