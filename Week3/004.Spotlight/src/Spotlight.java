import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private final Point2D spotlightPosition = new Point2D.Double(0, 0);
    private static final List<ColorLine> LINES = new ArrayList<>();
    private static final int LINE_AMOUNT = 100;
    private static final int MAX_CANVAS_WIDTH = 2560;
    private static final int MAX_CANVAS_HEIGHT = 1440;

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        for (int i = 0; i < LINE_AMOUNT; i++) {
            double x1 = Math.random() * MAX_CANVAS_WIDTH;
            double y1 = Math.random() * MAX_CANVAS_HEIGHT;
            double x2 = Math.random() * MAX_CANVAS_WIDTH;
            double y2 = Math.random() * MAX_CANVAS_HEIGHT;

            LINES.add(new ColorLine(new Line2D.Double(x1, y1, x2, y2), Color.getHSBColor((float) Math.random(), 1, 1)));
        }

        canvas.setFocusTraversable(true);
        canvas.setOnMouseMoved(mouseEvent -> spotlightPosition.setLocation(mouseEvent.getX(), mouseEvent.getY()));
        mainPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F11) stage.setFullScreen(!stage.isFullScreen());
        });

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1280, 720));
        stage.setTitle("Spotlight");
        stage.show();
    }

    private void draw(FXGraphics2D graphics) {
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Shape shape = new Ellipse2D.Double(spotlightPosition.getX() - 100, spotlightPosition.getY() - 100, 200, 200);
        graphics.setClip(shape);

        for (ColorLine line : LINES) {
            graphics.setColor(line.getColor());
            graphics.draw(line.getLine());
        }

        graphics.setClip(null);
    }

    private void update(double deltaTime) {
    }

    public static void main(String[] args) {
        launch(Spotlight.class);
    }

}
