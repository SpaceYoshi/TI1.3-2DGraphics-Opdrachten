import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Johan Talboom
 * @author Thijme Franken
 * @author Max Hager
 */
public class AngryBirds extends Application {
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private static final Media THEME_SONG = new Media(Objects.requireNonNull(AngryBirds.class.getResource("theme.mp3")).toString()); // Declare the media here, to prevent GC from cutting the audio off.

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);

        // Add debug button
        CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> debugSelected = showDebug.isSelected());

        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        camera = new Camera(canvas, this::draw, g2d);
        mousePicker = new MousePicker(canvas);

        mainPane.setTop(showDebug);
        mainPane.setCenter(canvas);

        playMusic();

        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1000));
        stage.setTitle("Angry Birds");
        stage.show();
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        // Set ground
        Body ground = new Body();
        ground.addFixture(Geometry.createRectangle(100, 0.5));
        ground.getTransform().setTranslationY(-5);
        ground.setMass(MassType.INFINITE);
        world.addBody(ground);
        gameObjects.add(new GameObject("ground.png", ground, new Vector2(0, 0), 1));

        // Set two walls to keep objects inside the screen
        Body leftWall = new Body();
        leftWall.addFixture(Geometry.createRectangle(0.5, 1000));
        leftWall.getTransform().setTranslation(-12, 0);
        leftWall.setMass(MassType.INFINITE);
        world.addBody(leftWall);

        Body rightWall = new Body();
        rightWall.addFixture(Geometry.createRectangle(0.5, 1000));
        rightWall.getTransform().setTranslation(12, 0);
        rightWall.setMass(MassType.INFINITE);
        world.addBody(rightWall);

        // Generate boxes
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10 - y; x++) {
                Body box = new Body();
                box.addFixture(Geometry.createSquare(.5));
                box.setMass(MassType.NORMAL);
                box.getTransform().setTranslation(5 + (x * 0.5) + 0.25 * y, y * 0.5 - 4.5);
                world.addBody(box);
                gameObjects.add(new GameObject("box.png", box, new Vector2(0, 0), 0.06));
            }
        }

        // Add catapult
        Body catapult = new Body();
        catapult.getTransform().setTranslation(-8, -3.8);
        gameObjects.add(new GameObject("catapult.png", catapult, new Vector2(-8, -155), .3));

        Body catapultBeginPoint = new Body();
        catapultBeginPoint.getTransform().setTranslation(-8, -2.3);

        // Set bird
        Body ball = new Body();
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(.3));
        fixture.setRestitution(.25);
        ball.addFixture(fixture);
        ball.getTransform().setTranslation(catapultBeginPoint.getTransform().getTranslation());
        Mass massBall = new Mass(new Vector2(0, 0), 5, 0.007);
        ball.setMass(massBall);
        world.addBody(ball);
        gameObjects.add(new GameObject("bird.png", ball, new Vector2(-50, -30), .1));

        PinJoint startJoint = new PinJoint(ball, catapultBeginPoint.getTransform().getTranslation(), 10000, 0, 500);
        startJoint.setTarget(catapultBeginPoint.getTransform().getTranslation());
        world.addJoint(startJoint);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        for (GameObject go : gameObjects) go.draw(graphics);

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
    }

    private void playMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(THEME_SONG);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(1);
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
