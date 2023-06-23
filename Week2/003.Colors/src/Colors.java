import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Colors extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage)
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Colors");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);

        // Scale and reset the canvas taking into account the scale factor.
        double scaleFactor = 1;
        graphics.scale(scaleFactor, scaleFactor);
        graphics.clearRect((int) ((0 - canvas.getWidth() / 2) / scaleFactor), (int) ((0 - canvas.getHeight() / 2) / scaleFactor),
                (int) (canvas.getWidth() / scaleFactor), (int) (canvas.getHeight() / scaleFactor));

        // Draw squares
        int width = 100;
        for (int i = 0, j = 0; i < 13; i++, j += width) {
            graphics.setColor(getColor(i));
            graphics.fill(new Rectangle2D.Double(j, 0, width, width));
        }
    }

    private static Color getColor(int value) {
        switch (value) {
            case 0: return Color.BLACK;
            case 1: return Color.BLUE;
            case 2: return Color.CYAN;
            case 3: return Color.DARK_GRAY;
            case 4: return Color.GRAY;
            case 5: return Color.GREEN;
            case 6: return Color.LIGHT_GRAY;
            case 7: return Color.MAGENTA;
            case 8: return Color.ORANGE;
            case 9: return Color.PINK;
            case 10: return Color.RED;
            case 11: return Color.WHITE;
            case 12: return Color.YELLOW;
            default: throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public static void main(String[] args)
    {
        launch(Colors.class);
    }

}
