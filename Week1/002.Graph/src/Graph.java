import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;

public class Graph extends Application {
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Graph");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        int scaleFactor = 2;

        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(scaleFactor, -1 * scaleFactor);

        double resolution = 0.1;
        double scaleX = 75;
        double scaleY = 50;
        double startX = -10;
        double endX = 10;
        double lastY = calculateFormula(startX);

        for (double x = startX; x < endX; x += resolution) {
            double y = calculateFormula(x);
            graphics.draw(new Line2D.Double(x*scaleX, y*scaleY, (x-resolution)*scaleX, lastY*scaleY));
            lastY = y;
        }
    }

    private double calculateFormula(double x) {
//        return Math.pow(x, 3);
        final int a = 1;
        final int b = 10;
        final int c = 1;
        return a * Math.sin(2 * Math.PI * x / b) * Math.sin(c * x);
    }

    public static void main(String[] args) {
        launch(Graph.class);
    }

}
