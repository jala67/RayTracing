public class Ray {
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector getOrigin() {
        return origin;
    }

    public Vector getDirection() {
        return direction;
    }

    public Vector getPoint(float intersection) {
        return origin.add(direction.multiply(intersection));
    }
}

