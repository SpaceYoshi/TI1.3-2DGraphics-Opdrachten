import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] walkCycle;
    private BufferedImage[] jumpCycle;
    private BufferedImage[] currentCycle;
    private final Point2D position = new Point2D.Double();
    private Animation state = Animation.WALK;
    private double passedTimeMovement = 0;
    private double passedTimeSprite = 0;
    private double passedTimePreJump = 0;
    private int sprite = 0;
    private int spriteHeight;
    private int spriteWidth;
    private int relativeHeight;
    private boolean jumpingUp = false;
    private boolean walkingForward = true;
    private boolean changeCycle = false;

    private enum Animation {WALK, PRE_JUMP, JUMP}

    @Override
    public void start(Stage stage) {
        loadSprites();
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

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

        canvas.setOnMousePressed(this::mousePressed);

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
    }

    private void loadSprites() {
        int spriteAmount = 8;
        walkCycle = new BufferedImage[spriteAmount];
        jumpCycle = new BufferedImage[spriteAmount];

        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/sprites.png")));
            spriteWidth = spriteSheet.getWidth() / spriteAmount;
            spriteHeight = spriteSheet.getHeight() / 9;

            for (int i = 0; i < spriteAmount; i++) {
                walkCycle[i] = spriteSheet.getSubimage(i * spriteWidth, 4 * spriteHeight, spriteWidth, spriteHeight);
                jumpCycle[i] = spriteSheet.getSubimage(i * spriteWidth, 5 * spriteHeight, spriteWidth, spriteHeight);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentCycle = walkCycle;
    }

    private void draw(FXGraphics2D g2d) {
        g2d.setTransform(new AffineTransform());
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform at = new AffineTransform();
        at.translate(position.getX(), position.getY());
        if (!walkingForward) at.scale(-1, 1);

        g2d.drawImage(currentCycle[sprite], at, null);
    }

    private void update(double deltaTime) {
        passedTimeMovement += deltaTime;
        passedTimeSprite += deltaTime;

        // Update movement
        if (passedTimeMovement >= 0.001) {
            switch (state) {
                case WALK:      walk(); break;
                case PRE_JUMP:  preJump(deltaTime); break;
                case JUMP:      jump(); break;
            }
            passedTimeMovement = 0;
        }
        // Update sprite
        if (changeCycle) updateCycle();
        updateSprite();
    }

    private void walk() {
        int speed = 1;
        if (position.getX() >= canvas.getWidth() + spriteWidth / 3f) walkingForward = false;
        else if (position.getX() <= 0 - spriteWidth / 3f) walkingForward = true;

        if (walkingForward) position.setLocation(position.getX() + speed, getBaseYPosition());
        else position.setLocation(position.getX() - speed, getBaseYPosition());
    }

    private void preJump(double deltaTime) {
        passedTimePreJump += deltaTime;
        if (passedTimePreJump >= 0.5) {
            state = Animation.JUMP;
            passedTimePreJump = 0;
        }
    }

    private void jump() {
        int speed = 2;
        if (relativeHeight == 0) jumpingUp = true;

        relativeHeight = (jumpingUp) ? relativeHeight + speed : relativeHeight - speed;
        position.setLocation(position.getX(), getBaseYPosition() - relativeHeight);

        if (relativeHeight >= 50) jumpingUp = false;
        else if (relativeHeight <= 0) {
            state = Animation.WALK;
            changeCycle = true;
        }
    }

    private void updateSprite() {
        if (state == Animation.JUMP) {
            if (passedTimeSprite >= 0.3) {
                if (!jumpingUp || sprite == currentCycle.length - 1) sprite--;
                else sprite++;
                passedTimeSprite = 0;
            }
        } else if (passedTimeSprite >= 0.1) {
            if (sprite == currentCycle.length - 1) sprite = 0;
            else sprite++;
            passedTimeSprite = 0;
        }
    }

    private void updateCycle() {
        switch (state) {
            case WALK:      currentCycle = walkCycle; break;
            case PRE_JUMP:  currentCycle = jumpCycle; break;
        }
        sprite = 0;
        changeCycle = false;
    }

    private double getBaseYPosition() {
        return canvas.getHeight() / 2 - spriteHeight / 2f;
    }

    private void mousePressed(MouseEvent e) {
        if (state == Animation.PRE_JUMP || state == Animation.JUMP) return;
        state = Animation.PRE_JUMP;
        changeCycle = true;
    }

    public static void main(String[] args) {
        launch(MovingCharacter.class);
    }

}
