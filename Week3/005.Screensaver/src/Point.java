import java.awt.geom.Point2D;

public class Point {
    private final Point2D[] positions;
    private double velocityX;
    private double velocityY;
    private int positionIndex = 0;

    Point(double startX, double startY, double startAngle, double velocity, int historyAmount) {
        this.velocityX = velocity * Math.cos(Math.toRadians(startAngle));
        this.velocityY = velocity * Math.sin(Math.toRadians(startAngle));
        this.positions = new Point2D[1 + historyAmount];

        this.positions[0] = new Point2D.Double(startX, startY);
        for (int i = 1; i < positions.length; i++) positions[i] = new Point2D.Double(0,0);
    }

    public void update(double canvasWidth, double canvasHeight, double deltaTime) {
        double currentX = positions[positionIndex].getX();
        double currentY = positions[positionIndex].getY();

        if (currentX + velocityX * deltaTime > canvasWidth || currentX + velocityX * deltaTime < 0) velocityX = -velocityX;
        if (currentY + velocityY * deltaTime > canvasHeight || currentY + velocityY * deltaTime < 0) velocityY = -velocityY;

        if (positionIndex < positions.length - 1) positionIndex++;
        else positionIndex = 0;

        positions[positionIndex] = new Point2D.Double(currentX + velocityX * deltaTime, currentY + velocityY * deltaTime);
    }

    public Point2D[] getLocations() {
        return positions;
    }

}
