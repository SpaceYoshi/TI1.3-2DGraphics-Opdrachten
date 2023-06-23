import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Images from Pixabay.
 */
public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private static List<Image> images;
    private static final int IMAGE_AMOUNT = 6;
    private int currentImageIndex = 0;
    private int nextImageIndex = 1;
    private float nextAlpha = 0;
    private double transitionTimer = 0;
    private double fadeTimer = 0;

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);

        images = new ArrayList<>(IMAGE_AMOUNT);
        loadImages();

        canvas.setFocusTraversable(true);
        mainPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F11) stage.setFullScreen(!stage.isFullScreen());
        });

        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        }.start();
        
        stage.setScene(new Scene(mainPane, 1280, 720));
        stage.setTitle("Fading image");
        stage.show();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        graphics.drawImage(images.get(currentImageIndex), graphics.getTransform(), null);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nextAlpha));
        graphics.drawImage(images.get(nextImageIndex), graphics.getTransform(), null);
    }

    public void update(double deltaTime) {
        transitionTimer += deltaTime;

        if (transitionTimer >= 3) {
            // Start transition
            fadeTimer += deltaTime;
            if (fadeTimer >= 0.01) {
                if (nextAlpha + 0.01 > 1) nextAlpha = 1;
                else nextAlpha += 0.01;
                fadeTimer = 0;
            }

            if (nextAlpha == 1) {
                // Next image
                currentImageIndex = nextImageIndex;
                if (nextImageIndex == images.size() - 1) nextImageIndex = 0;
                else nextImageIndex++;

                // Reset
                transitionTimer = 0;
                nextAlpha = 0;
            }
        }
    }

    private static void loadImages() {
        try {
            for (int i = 1; i <= IMAGE_AMOUNT; i++) images.add(ImageIO.read(Objects.requireNonNull(FadingImage.class.getResourceAsStream("images/lynx-" + i + ".png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}
