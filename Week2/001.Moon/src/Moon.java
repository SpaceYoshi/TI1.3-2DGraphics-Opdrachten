import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class Moon extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Moon");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);

        // Translate, scale and reset the canvas taking into account the scale factor.
        double scaleFactor = 1.5;
        graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        graphics.scale(scaleFactor, -1 * scaleFactor);
        graphics.clearRect((int) ((0 - canvas.getWidth()/2) / scaleFactor), (int) ((0 - canvas.getHeight()/2) / scaleFactor),
                (int) (canvas.getWidth() / scaleFactor), (int) (canvas.getHeight() / scaleFactor));

        // Using an Arc2D and a GeneralPath object
//        Arc2D outerArc = new Arc2D.Double(0, 0, 200, 200, -120 * -1, (90 + 135) * -1, Arc2D.PIE);
//
//        GeneralPath innerPath = new GeneralPath();
//        innerPath.moveTo(outerArc.getStartPoint().getX(), outerArc.getStartPoint().getY());
//        innerPath.curveTo(outerArc.getStartPoint().getX() + 110, outerArc.getStartPoint().getY() - 75,
//                outerArc.getEndPoint().getX() + 125, outerArc.getEndPoint().getY() + 50,
//                outerArc.getEndPoint().getX(), outerArc.getEndPoint().getY());
//        innerPath.closePath();
//
//        Area outerArea = new Area(outerArc);
//        Area innerArea = new Area(innerPath);
//        outerArea.subtract(innerArea);
//        graphics.fill(outerArea);

        // Using two GeneralPath objects
        GeneralPath outerPath = new GeneralPath();
        outerPath.moveTo(0, 100);
        outerPath.curveTo(110, 100, 135, -125, -50, -100);
        outerPath.closePath();

        GeneralPath innerPath = new GeneralPath();
        innerPath.moveTo(0, 100);
        innerPath.curveTo(45, 70, 50, -75, -50, -100);
        innerPath.closePath();

        // Method 1
//        graphics.fill(outerPath);
//        graphics.setColor(Color.WHITE);
//        graphics.fill(innerPath);
//        graphics.draw(innerPath); // or outerPath, to remove the leftover line.

        // Method 2
        Area outerArea = new Area(outerPath);
        Area innerArea = new Area(innerPath);
        outerArea.subtract(innerArea);
        graphics.fill(outerArea);
    }

    public static void main(String[] args) {
        launch(Moon.class);
    }

}
