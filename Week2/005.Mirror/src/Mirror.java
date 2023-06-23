import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Mirror extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setStroke(new BasicStroke(2));

        // Draw the x and y-axis.
        int axisOffset = 1;
        int axisStepAmount = 50;
        int axisStepLength = 5;
        for (int i = axisStepAmount; i < canvas.getWidth(); i += axisStepAmount) {
            graphics.draw(new Line2D.Double(i, axisOffset, i, axisOffset + axisStepLength));
            graphics.draw(new Line2D.Double(axisOffset, i, axisOffset + axisStepLength, i));
        }
        graphics.draw(new Line2D.Double(axisOffset, axisOffset, canvas.getWidth(), axisOffset));
        graphics.draw(new Line2D.Double(axisOffset, axisOffset, axisOffset, canvas.getHeight()));

        // Draw the line y = kx
        double k = 2.5;
        AffineTransform rotation = new AffineTransform();
        rotation.rotate(Math.atan(k));
        graphics.draw(rotation.createTransformedShape(new Line2D.Double(0, 0, canvas.getWidth(), 0)));

        // Draw the original square
        Rectangle2D square = new Rectangle2D.Double(0, 150, 100, 100);
        graphics.draw(square);

        // Draw the reflected square
        double m00 = 2 / (1 + k*k) - 1;
        double m10 = 2*k / (1 + k*k); // Equal to m01
        double m11 = 2*k*k / (1 + k*k) - 1;
        AffineTransform reflection = new AffineTransform(m00, m10, m10, m11, 0, 0);
        graphics.draw(reflection.createTransformedShape(square));
    }

    public static void main(String[] args) {
        launch(Mirror.class);
    }

}
