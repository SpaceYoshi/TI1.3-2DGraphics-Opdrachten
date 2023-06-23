import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class YinYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Yin Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);

        // Translate, scale and reset the canvas taking into account the scale factor.
        double scaleFactor = 1.3;
        graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        graphics.scale(scaleFactor, -1 * scaleFactor);
        graphics.clearRect((int) ((0 - canvas.getWidth() / 2) / scaleFactor), (int) ((0 - canvas.getHeight() / 2) / scaleFactor),
                (int) (canvas.getWidth() / scaleFactor), (int) (canvas.getHeight() / scaleFactor));

        // Define shapes
        Ellipse2D outerCircle = new Ellipse2D.Double(-150, -150, 300, 300);
        Ellipse2D innerBlackCircle = new Ellipse2D.Double(-20, -20 + 70, 40, 40);
        Ellipse2D innerWhiteCircle = new Ellipse2D.Double(-20, -20 - 70, 40, 40);

        GeneralPath border = new GeneralPath();
        border.moveTo(0, -150);
        border.curveTo(-105, -150, -105, 0, 0, 0);
        border.curveTo(105, 0, 105, 150, 0, 150);
        border.curveTo(200, 138, 200, -138, 0, -150);
        border.closePath();

        Stroke s = new BasicStroke(3);
        graphics.setStroke(s);
        graphics.draw(outerCircle);
        graphics.fill(innerBlackCircle);
        graphics.fill(border);
        graphics.setColor(Color.WHITE);
        graphics.fill(innerWhiteCircle);
        graphics.setColor(Color.BLACK); // Reset the colour, so resizing the window properly works.
    }

    public static void main(String[] args) {
        launch(YinYang.class);
    }

}
