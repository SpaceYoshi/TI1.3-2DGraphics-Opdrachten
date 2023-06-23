import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;
    private ComboBox<String> colorBox;
    private Canvas canvas;
    private boolean executedBefore = false;
    private boolean resetMode = false;
    private boolean randomMode = false;
    private boolean darkMode = false;
    private boolean listenerLock = false; // Lock the listeners while TextField values are being updated, to prevent recursion resulting in a stack overflow error.
    private Random random;
    private final double scaleFactor = 1;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(1600, 800); // Size change to make buttons visible on a high resolution monitor with scaling.
        random = new Random();
        double defaultA = 300;
        double defaultB = 1;
        double defaultC = 300;
        double defaultD = 10;

        VBox mainBox = new VBox();
        HBox topBar = new HBox(5);
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));
        topBar.setStyle("-fx-background-color: rgb(240,240,240)");
        topBar.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));

        Button randomButton = new Button("Random");
        Button clearButton = new Button("Clear");
        Button defaultButton = new Button("Default");
        ToggleButton resetModeButton = new ToggleButton("Reset Mode");
        ToggleButton darkModeButton = new ToggleButton("Dark Mode");

        colorBox = new ComboBox<>();
        colorBox.getItems().add("Rainbow");
        colorBox.getItems().add("Cyberpunk");
        colorBox.getSelectionModel().selectFirst();

        topBar.getChildren().add(v1 = new TextField(Double.toString(defaultA)));
        topBar.getChildren().add(v2 = new TextField(Double.toString(defaultB)));
        topBar.getChildren().add(v3 = new TextField(Double.toString(defaultC)));
        topBar.getChildren().add(v4 = new TextField(Double.toString(defaultD)));
        topBar.getChildren().add(randomButton);
        topBar.getChildren().add(resetModeButton);
        topBar.getChildren().add(clearButton);
        topBar.getChildren().add(defaultButton);
        topBar.getChildren().add(colorBox);
        topBar.getChildren().add(darkModeButton);

        v1.textProperty().addListener(e -> {
            if (!listenerLock) draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        v2.textProperty().addListener(e -> {
            if (!listenerLock) draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        v3.textProperty().addListener(e -> {
            if (!listenerLock) draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        v4.textProperty().addListener(e -> {
            if (!listenerLock) draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        randomButton.setOnAction(event -> {
            randomMode = true;
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        resetModeButton.setOnAction(event -> resetMode = !resetMode);
        clearButton.setOnAction(event -> clearCanvas());
        defaultButton.setOnAction(event -> {
            listenerLock = true;
            v1.setText(Double.toString(defaultA));
            v2.setText(Double.toString(defaultB));
            v3.setText(Double.toString(defaultC));
            v4.setText(Double.toString(defaultD));
            listenerLock = false;
            clearCanvas();
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        darkModeButton.setOnAction(event -> toggleDarkMode(mainBox, topBar));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        //you can use Double.parseDouble(v1.getText()) to get a double value from the first text field
        //feel free to add more text fields or other controls if needed, but beware that swing components might clash in naming

        // Execute once, to prevent extra translations.
        if (!executedBefore) {
            graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
            graphics.scale(scaleFactor, -1 * scaleFactor);
            executedBefore = true;
        }
        if (resetMode) clearCanvas();

        double resolution = 0.1;
        double scaleX = 1;
        double scaleY = 1;
        double startAngle = 0;
        double endAngle = 20 * Math.PI;
        double a;
        double b;
        double c;
        double d;
        String selectedColour = colorBox.getValue();

        if (randomMode) {
            randomMode = false; // Reset random mode.
            a = random.nextInt(501) -250; // Limit to keep inside window
            b = random.nextInt(10001) - 5000; // Any value
            c = random.nextInt(1001) - 500; // Limit to keep inside window
            d = random.nextInt(10001) - 5000; // Any value
            listenerLock = true;
            v1.setText(Double.toString(a));
            v2.setText(Double.toString(b));
            v3.setText(Double.toString(c));
            v4.setText(Double.toString(d));
            listenerLock = false;
        } else {
            a = getInput(v1);
            b = getInput(v2);
            c = getInput(v3);
            d = getInput(v4);
        }

        Point2D lastPoint = scalePoint(calculateFormula(startAngle, a, b, c, d), scaleX, scaleY);

        int i = 0;
        for (double angle = startAngle; angle < endAngle; angle += resolution, i++) {
            Point2D point = scalePoint(calculateFormula(angle, a, b, c, d), scaleX, scaleY);
            if (selectedColour.equals("Rainbow")) graphics.setColor(Color.getHSBColor((float) angle, 1, 1));
            else {
                if (i % 2 == 0) graphics.setColor(Color.getHSBColor(0.16f, 1, 1));
                else graphics.setColor(Color.getHSBColor(0.5f, 1, 1));
            }
            graphics.draw(new Line2D.Double(point, lastPoint));
            lastPoint = point;
        }
    }

    /*
    Given formulas:
    x = a × cos(b × φ) + c × cos(d × φ)
    y = a × sin(b × φ) + c × sin(d × φ)
     */
    private Point2D calculateFormula(double angle, double a, double b, double c, double d) {
        return new Point2D.Double(a * Math.cos(b * angle) + c * Math.cos(d * angle), a * Math.sin(b * angle) + c * Math.sin(d * angle));
    }

    private Point2D scalePoint(Point2D point, double scaleX, double scaleY) {
        point.setLocation(point.getX() * scaleX, point.getY() * scaleY);
        return point;
    }

    private double getInput(TextField textField) {
        double input = 1; // Default value
        try {
            input = Double.parseDouble(textField.getText());
        } catch (NumberFormatException ignored) {}
        return input;
    }

    private void clearCanvas() {
        canvas.getGraphicsContext2D().clearRect((0 - canvas.getWidth()/2) / scaleFactor, (0 - canvas.getHeight()/2) / scaleFactor, canvas.getWidth() / scaleFactor, canvas.getHeight() / scaleFactor);
    }

    private void toggleDarkMode(Pane mainBox, Pane topBar) {
        if (darkMode) {
            darkMode = false;
            topBar.setStyle("-fx-background-color: rgb(240,240,240)");
            mainBox.setStyle("-fx-background-color: rgb(244,244,244)");
        } else {
            darkMode = true;
            topBar.setStyle("-fx-background-color: rgb(40,40,40)");
            mainBox.setStyle("-fx-background-color: rgb(43,43,43)");
        }
    }

    /*
    List of cool combinations:
    1: -136, -3330, 292, -999
    2: 107, -2067, -325, -2771
    3: 216, 919, -228, -1563
    4: -239, 3210, 488, -3014
     */
    public static void main(String[] args) {
        launch(Spirograph.class);
    }

}
