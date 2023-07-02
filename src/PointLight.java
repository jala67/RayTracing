public class PointLight {
    Vector position;
    float intensity;

    public PointLight(Vector position, float intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public float getIntensity() {
        return intensity;
    }

    // n vektor * richtung licht (punkt von kugel - lichtpos)
}
