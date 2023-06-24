package constraints;

import org.jfree.fx.FXGraphics2D;
import particles.Particle;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class StaticConstraint implements Constraint, Serializable {
    private final Particle particle;
    private Point2D fixedPosition;

    public StaticConstraint(Particle particle) {
        this.particle = particle;
        if (particle != null) fixedPosition = particle.getPosition();
    }

    @Override
    public void satisfy() {
        if (particle != null) particle.setPosition(fixedPosition);
    }

    @Override
    public void draw(FXGraphics2D g2d) {
    }

}
