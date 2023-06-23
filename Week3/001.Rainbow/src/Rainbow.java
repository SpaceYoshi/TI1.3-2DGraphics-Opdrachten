import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(AffineTransform.getTranslateInstance(canvas.getWidth()/2, canvas.getHeight()/2));
        graphics.setBackground(Color.WHITE);
        graphics.clearRect((int) (-canvas.getWidth()/2), (int) (-canvas.getHeight()/2), (int) canvas.getWidth(), (int) canvas.getHeight());

        int size = 100;
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, size);

        String text = "Rainbow";
        int length = text.length();

        for (int i = 0; i < length; i++) {
            GlyphVector gv = font.createGlyphVector(graphics.getFontRenderContext(), String.valueOf(text.charAt(i)));
            Color color = Color.getHSBColor((float) i / (length),1,1);
            Rectangle2D bounds = gv.getLogicalBounds();

            AffineTransform at = new AffineTransform();
            at.rotate((-0.5 + (double) i / (length - 1)) * Math.PI);
            at.translate(-bounds.getWidth() / 2, (-bounds.getHeight() / 2) - size);

            Shape shape = at.createTransformedShape(gv.getOutline());

            graphics.setColor(color);
            graphics.fill(shape);
        }
    }

    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
