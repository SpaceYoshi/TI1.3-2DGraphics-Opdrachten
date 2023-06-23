import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class House extends Application {
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        int scaleFactor = 2;

        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(scaleFactor, -1 * scaleFactor);

        // Draw the x and y-axis for reference
//        graphics.setColor(Color.RED);
//        graphics.drawLine(0, 0, 200, 0);
//        graphics.setColor(Color.GREEN);
//        graphics.drawLine(0, 0, 0, 200);
//        graphics.setColor(Color.BLACK);

        // Drawing the house frame
        graphics.drawLine(-150, 0, -150, -200);
        graphics.drawLine(150, 0, 150, -200);

        graphics.drawLine(-150, 0, 0, 200);
        graphics.drawLine(150, 0, 0, 200);

        graphics.drawLine(-150, -200, 150, -200);

        // Drawing the door
        graphics.drawLine(-120, -200, -120, -100);
        graphics.drawLine(-55, -200, -55, -100);
        graphics.drawLine(-120, -100, -55, -100);

        // Drawing the window
        graphics.drawLine(-25, -150, 120, -150);
        graphics.drawLine(-25, -75, 120, -75);

        graphics.drawLine(-25, -150, -25, -75);
        graphics.drawLine(120, -150, 120, -75);
    }

    public static void main(String[] args) {
        launch(House.class);
    }

}
