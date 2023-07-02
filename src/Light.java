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
}
