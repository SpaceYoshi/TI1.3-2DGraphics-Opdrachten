import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Spiral extends Application {
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        int scaleFactor = 2;

        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(scaleFactor, -1 * scaleFactor);

        double resolution = 0.1;
        double scaleX = 15;
        double scaleY = 15;
        double startAngle = 0;
        double endAngle = 10 * Math.PI;
        double n = 1; // Constant defining the density of the spiral
        Point2D lastPoint = scalePoint(calculateFormula(startAngle, n), scaleX, scaleY);

        for (double angle = startAngle; angle < endAngle; angle += resolution) {
            Point2D point = scalePoint(calculateFormula(angle, n), scaleX, scaleY);
            graphics.draw(new Line2D.Double(point, lastPoint));
            lastPoint = point;
        }
    }

    /*
    Formulas:
    x = n × Ø × cos(Ø)
    y = n × Ø × sin(Ø)
     */
    private Point2D calculateFormula(double angle, double n) {
        return new Point2D.Double(n * angle * Math.cos(angle), n * angle * Math.sin(angle));
    }

    private Point2D scalePoint(Point2D point, double scaleX, double scaleY) {
        point.setLocation(point.getX() * scaleX, point.getY() * scaleY);
        return point;
    }

    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
