import constraints.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import particles.Particle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;

public class VerletEngine extends Application {
    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);
    private static final String FILE_NAME = "Week4/001.Verlet/save.txt";

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);

        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button curtainButton = new Button("Curtain");
        HBox buttonBox = new HBox(10, saveButton, loadButton, curtainButton);

        mainPane.setCenter(canvas);
        mainPane.setTop(buttonBox);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        }.start();

        // Mouse events
        canvas.setOnMouseClicked(this::mouseClicked);
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseReleased(this::mouseReleased);
        canvas.setOnMouseDragged(this::mouseDragged);

        // Button events
        saveButton.setOnAction(this::save);
        loadButton.setOnAction(this::load);
        curtainButton.setOnAction(this::curtain);

        // Set scene
        stage.setScene(new Scene(mainPane, 1920, 1000));
        stage.setTitle("Verlet Engine");
        stage.show();
    }

    @Override
    public void init() {
        for (int i = 0; i < 20; i++) particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        for (int i = 0; i < 10; i++) constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint constraint : constraints) constraint.draw(graphics);
        for (Particle particle : particles) particle.draw(graphics);
    }

    private void update(double deltaTime) {
        for (Particle particle : particles) particle.update((int) canvas.getWidth(), (int) canvas.getHeight());
        for (Constraint constraint : constraints) constraint.satisfy();
    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle particle : particles) {
            if (particle.getPosition().distance(point) < nearest.getPosition().distance(point)) nearest = particle;
        }
        return nearest;
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle newParticle = new Particle(mousePosition);
        particles.add(newParticle);

        if (e.getButton() == MouseButton.PRIMARY) {
            Particle nearest = getNearest(mousePosition);

            if (e.isShiftDown()) {
                constraints.add(new RopeConstraint(newParticle, nearest, 150));
            } else if (e.isControlDown()) {
                constraints.add(new StaticConstraint(newParticle));
            } else {
                constraints.add(new DistanceConstraint(newParticle, nearest));
            }
        } else if (e.getButton() == MouseButton.SECONDARY) {
            ArrayList<Particle> sorted = new ArrayList<>(particles);
            // Sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            sorted.sort((o1, o2) -> (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition)));

            if (e.isControlDown()) {
                constraints.add(new DistanceConstraint(newParticle, sorted.get(1), 100));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2), 100));
            } else {
                constraints.add(new DistanceConstraint(newParticle, sorted.get(1)));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
            }
        } else if (e.getButton() == MouseButton.MIDDLE) {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        }
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    private void save(ActionEvent actionEvent) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            // Write the particles and constraints to the file
            objectOutputStream.writeObject(particles);
            objectOutputStream.writeObject(constraints);
            objectOutputStream.writeObject(mouseConstraint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(ActionEvent actionEvent) {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            // Read the particles and constraints from the file
            particles = (ArrayList<Particle>) objectInputStream.readObject();
            constraints = (ArrayList<Constraint>) objectInputStream.readObject();
            mouseConstraint = (PositionConstraint) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void curtain(ActionEvent actionEvent) {
        particles.clear();
        constraints.clear();

        int width = 7;
        int height = 5;
        int startX = 100;
        int startY = 100;
        int spacingX = 150;
        int spacingY = 150;

        ArrayList<Particle> topRow = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            Particle particle = new Particle(new Point2D.Double(startX + (spacingX * i), startY));
            StaticConstraint staticConstraint = new StaticConstraint(particle);

            if (i > 0) {
                DistanceConstraint distanceConstraint = new DistanceConstraint(particle, topRow.get(i - 1));
                constraints.add(distanceConstraint);
            }

            topRow.add(particle);
            constraints.add(staticConstraint);
        }

        particles.addAll(topRow);

        ArrayList<Particle> currentParticleRow = new ArrayList<>();

        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                Particle particle = new Particle(new Point2D.Double(startX + (spacingX * j), spacingY * i));

                if (j > 0) {
                    DistanceConstraint distanceConstraintHor = new DistanceConstraint(particle, currentParticleRow.get(j - 1));
                    constraints.add(distanceConstraintHor);
                }

                DistanceConstraint distanceConstraintVer = new DistanceConstraint(particle, topRow.get(j), spacingX);
                constraints.add(distanceConstraintVer);
                currentParticleRow.add(particle);
            }

            constraints.add(mouseConstraint);
            particles.addAll(currentParticleRow);
            topRow.clear();
            topRow.addAll(currentParticleRow);
            currentParticleRow.clear();
        }
    }

    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

}
