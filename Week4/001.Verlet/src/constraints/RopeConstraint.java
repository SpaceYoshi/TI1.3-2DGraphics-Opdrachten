package constraints;

import particles.Particle;

public class RopeConstraint extends DualParticleConstraint {
    public RopeConstraint(Particle a, Particle b) {
        super(a, b);
    }

    public RopeConstraint(Particle a, Particle b, double constraintDistance) {
        super(a, b, constraintDistance);
    }

    @Override
    public void satisfy() {
        if (currentDistance > constraintDistance) currentDistance = ConstraintUtility.SatisfyDualParticleConstraint(constraintDistance, b, a);
    }

}
