package constraints;

import particles.Particle;

import java.awt.*;
import java.awt.geom.Point2D;

public class ConstraintUtility {
    static double SatisfyDualParticleConstraint(double constraintDistance, Particle b, Particle a) {
        double currentDistance = a.getPosition().distance(b.getPosition());
        double adjustmentDistance = (currentDistance - constraintDistance) / 2;

        Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
        double length = BA.distance(0, 0);
        if (length > 0.0001) // We kunnen alleen corrigeren als we een richting hebben
        {
            BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
        } else {
            BA = new Point2D.Double(1, 0);
        }

        a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * adjustmentDistance,
                a.getPosition().getY() + BA.getY() * adjustmentDistance));
        b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * adjustmentDistance,
                b.getPosition().getY() - BA.getY() * adjustmentDistance));

        return currentDistance;
    }

    static Color getTensionColor(double tension, double constraintDistance) {
        double maxValue = 2 * constraintDistance; // red

        if (tension > maxValue) tension = maxValue;

        float hue = (float) ((1 - (tension - constraintDistance) / (maxValue - constraintDistance)) * 0.4f);
        return Color.getHSBColor(hue, 1, 1);
    }

}
