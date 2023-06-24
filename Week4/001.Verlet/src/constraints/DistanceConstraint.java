package constraints;

import particles.Particle;

public class DistanceConstraint extends DualParticleConstraint {
    public DistanceConstraint(Particle a, Particle b) {
        super(a, b);
    }

    public DistanceConstraint(Particle a, Particle b, double constraintDistance) {
        super(a, b, constraintDistance);
    }

    @Override
    public void satisfy() {
        currentDistance = ConstraintUtility.SatisfyDualParticleConstraint(constraintDistance, b, a);
    }

}
