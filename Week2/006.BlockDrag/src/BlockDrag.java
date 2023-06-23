import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockDrag extends Application {
    private ResizableCanvas canvas;
    private List<Block> blocks;
    private Random random;
    private Integer selectedBlockIndex;
    private Point2D pressPoint;
    private Camera camera;

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        camera = new Camera(new Point2D.Double(0, 0), 1);
        blocks = new ArrayList<>();
        random = new Random();
        for (int i = 0; i < 10; i++) blocks.add(new Block(new Rectangle2D.Double(0, 0, 100, 100), new Point2D.Double(canvas.getWidth() * random.nextDouble(), canvas.getHeight() * random.nextDouble()), getColor(i)));

        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseReleased(this::mouseReleased);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnScroll(this::mouseScrolled);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(camera.getTransform());
        graphics.setBackground(Color.white);
//        Point2D clearStart = getDestinationPoint(new Point2D.Double(0, 0));
//        Point2D clearEnd = getDestinationPoint(new Point2D.Double(canvas.getWidth(), canvas.getHeight()));
//        canvas.getGraphicsContext2D().clearRect(clearStart.getX(), clearStart.getY(), clearEnd.getX(), clearEnd.getY());
        graphics.clearRect(-10000, -10000, 100000, 100000); // Maybe not the best solution, but a large enough rectangle does the job.
        graphics.setStroke(new BasicStroke(2));

        for (Block block : blocks) block.draw(graphics);
    }

    public static void main(String[] args) {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) for (int i = 0; i < blocks.size(); i++) if (blocks.get(i).getTransformedShape().contains(getMousePosition(e))) selectedBlockIndex = i;
        updatePressPoint(e);
    }

    private void mouseReleased(MouseEvent e) {
        selectedBlockIndex = null;
    }

    private void mouseDragged(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            if (selectedBlockIndex != null) {
                Block block = blocks.get(selectedBlockIndex);
                block.setPosition(new Point2D.Double(block.getPosition().getX() + getMousePosition(e).getX() - pressPoint.getX(), block.getPosition().getY() + getMousePosition(e).getY() - pressPoint.getY()));
            } else return;
        } else if (e.getButton() == MouseButton.SECONDARY) {
            camera.setPosition(new Point2D.Double(camera.getPosition().getX() + getMousePosition(e).getX() - pressPoint.getX(), camera.getPosition().getY() + getMousePosition(e).getY() - pressPoint.getY()));
        } else return;
        updatePressPoint(e);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void mouseScrolled(ScrollEvent e) {
        camera.setZoom(camera.getZoom() + (float) e.getDeltaY() / 200);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void updatePressPoint(MouseEvent e) {
        pressPoint = getMousePosition(e);
    }

    private Point2D getMousePosition(MouseEvent e) {
        return getDestinationPoint(new Point2D.Double(e.getX(), e.getY()));
    }

    private Point2D getDestinationPoint(Point2D source) {
        Point2D destination;
        try {
            destination = camera.getTransform().inverseTransform(new Point2D.Double(source.getX(), source.getY()), null);
        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException(ex);
        }
        return destination;
    }

    private Color getColor(int value) {
        switch (value) {
            case 0: return Color.BLUE;
            case 1: return Color.CYAN;
            case 2: return Color.GREEN;
            case 3: return Color.MAGENTA;
            case 4: return Color.ORANGE;
            case 5: return Color.PINK;
            case 6: return Color.RED;
            case 7: return Color.YELLOW;
            default: return new Color(random.nextFloat() / 2 + 0.5f, random.nextFloat() / 2 + 0.5f, random.nextFloat() / 2 + 0.5f);
        }
    }

}
