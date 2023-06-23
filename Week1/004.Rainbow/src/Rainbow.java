import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class Rainbow extends Application {

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        int scaleFactor = 1;

        graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        graphics.scale(scaleFactor, -1 * scaleFactor);

        double resolution = 1/2000d;
        double scaleX = 20;
        double scaleY = 20;
        double startAngle = 0;
        double endAngle = Math.PI;
        double insideRadius = 15;
        double outsideRadius = 20;

        for (double angle = startAngle; angle < endAngle; angle += resolution) {
            Line2D line = scaleLine(calculateFormula(angle, insideRadius, outsideRadius), scaleX, scaleY);
            graphics.setColor(Color.getHSBColor((float) (angle/Math.PI), 1, 1));
            graphics.draw(line);
        }
    }

    private Line2D calculateFormula(double angle, double insideRadius, double outsideRadius) {
        return new Line2D.Double(insideRadius * Math.cos(angle), insideRadius * Math.sin(angle),
                outsideRadius * Math.cos(angle), outsideRadius * Math.sin(angle));
    }

    private Line2D scaleLine(Line2D line, double scaleX, double scaleY) {
        line.setLine(line.getX1() * scaleX, line.getY1() * scaleY, line.getX2() * scaleX, line.getY2() * scaleY);
        return line;
    }

    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
