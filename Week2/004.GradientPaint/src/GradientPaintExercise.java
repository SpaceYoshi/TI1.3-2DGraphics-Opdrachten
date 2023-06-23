import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private Point2D mousePosition;

    @Override
    public void start(Stage primaryStage)
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mousePosition = new Point2D.Double(canvas.getWidth()/2, canvas.getHeight()/2);
        canvas.setOnMouseDragged(event -> {
            mousePosition.setLocation(event.getX(), event.getY());
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Rectangle2D screen = new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setPaint(new RadialGradientPaint((float) canvas.getWidth()/2, (float) canvas.getHeight()/2, (float) canvas.getWidth()/2, (float) mousePosition.getX(), (float) mousePosition.getY(),
                new float[]{0, 0.25f, 0.5f, 0.75f, 1}, new Color[]{Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED}, MultipleGradientPaint.CycleMethod.REFLECT));
        graphics.fill(screen);
    }

    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
