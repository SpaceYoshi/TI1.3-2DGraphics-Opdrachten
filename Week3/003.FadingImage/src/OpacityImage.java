import java.awt.*;

public class OpacityImage {
    private final Image image;
    private float alpha = 0;

    public OpacityImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

}
