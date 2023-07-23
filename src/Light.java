public class Light {
    // sphere light
    Vector position;
    float radius;
    float intensity;

    public Light(Vector position, float radius, float intensity) {
        this.position = position;
        this.radius = radius;
        this.intensity = intensity;
    }

    public Vector getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public float getIntensity() {
        return intensity;
    }

    public Vector randomPoint() {
        float x = (float) (Math.random() * 1.5f - 1);
        float y = (float) (Math.random() * 1.5f - 1);
        float z = (float) (Math.random() * 1.5f - 1);
        Vector randomPoint = new Vector(x, y, z).normalize();
        return randomPoint.multiply(radius);
    }
}
