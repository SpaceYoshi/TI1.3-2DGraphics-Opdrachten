package constraints;

import org.jfree.fx.FXGraphics2D;
import particles.Particle;

import java.awt.geom.Line2D;
import java.io.Serializable;

public abstract class DualParticleConstraint implements Constraint, Serializable {
    final Particle a;
    final Particle b;
    final double constraintDistance;
    double currentDistance;

    public DualParticleConstraint(Particle a, Particle b) {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public DualParticleConstraint(Particle a, Particle b, double constraintDistance) {
        this.a = a;
        this.b = b;
        this.constraintDistance = constraintDistance;
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.setColor(ConstraintUtility.getTensionColor(Math.abs(currentDistance - constraintDistance), constraintDistance));
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }

}
