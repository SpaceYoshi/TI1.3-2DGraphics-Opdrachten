import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private static final List<Point> POINTS = new ArrayList<>();
    private static final int HISTORY_AMOUNT = 30;
    private static final int VELOCITY = 100;
    private static final int CANVAS_START_WIDTH = 1280;
    private static final int CANVAS_START_HEIGHT = 720;
    private double timer = 0;
    private float colorHue = (float) Math.random();
    private boolean increaseHue = true;

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);

        POINTS.add(new Point(CANVAS_START_WIDTH /4d, 3 * CANVAS_START_HEIGHT/4d, 120, VELOCITY, HISTORY_AMOUNT));
        POINTS.add(new Point(CANVAS_START_WIDTH /4d, CANVAS_START_HEIGHT/4d, -60, VELOCITY, HISTORY_AMOUNT));
        POINTS.add(new Point(3 * CANVAS_START_WIDTH/4d, 3 * CANVAS_START_HEIGHT/4d, 180, VELOCITY, HISTORY_AMOUNT));
        POINTS.add(new Point(3 * CANVAS_START_WIDTH/4d, CANVAS_START_HEIGHT/4d, 60, VELOCITY, HISTORY_AMOUNT));

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        }.start();

        stage.setScene(new Scene(mainPane, CANVAS_START_WIDTH, CANVAS_START_HEIGHT));
        stage.setTitle("Screensaver");
        stage.show();
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.DARK_GRAY);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(Color.getHSBColor(colorHue, 1, 1));

        Point2D[] p0 = POINTS.get(0).getLocations();
        Point2D[] p1 = POINTS.get(1).getLocations();
        Point2D[] p2 = POINTS.get(2).getLocations();
        Point2D[] p3 = POINTS.get(3).getLocations();

        for (int i = 0; i < HISTORY_AMOUNT; i++) {
            Point2D point0 = p0[i];
            Point2D point1 = p1[i];
            Point2D point2 = p2[i];
            Point2D point3 = p3[i];

            graphics.draw(new Line2D.Double(point0.getX(), point0.getY(), point1.getX(), point1.getY()));
            graphics.draw(new Line2D.Double(point0.getX(), point0.getY(), point2.getX(), point2.getY()));
            graphics.draw(new Line2D.Double(point2.getX(), point2.getY(), point3.getX(), point3.getY()));
            graphics.draw(new Line2D.Double(point1.getX(), point1.getY(), point3.getX(), point3.getY()));
        }
    }

    private void update(double deltaTime) {
        timer += deltaTime;

        if (timer > 0.05) {
            for (Point point : POINTS) point.update(canvas.getWidth(), canvas.getHeight(), timer);

            // Update color
            final float hueStep = 0.01f;
            if (colorHue + hueStep > 1) increaseHue = false;
            else if (colorHue - hueStep < 0) increaseHue = true;

            if (increaseHue) colorHue += hueStep;
            else colorHue -= hueStep;

            timer = 0;
        }
    }

    public static void main(String[] args) {
        launch(Screensaver.class);
    }

}
