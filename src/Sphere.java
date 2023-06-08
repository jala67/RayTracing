import java.awt.Color;

public class Sphere {

    private Vector center;
    private float radius;
    private Vector color;

    public Sphere(Vector center, float radius, Vector color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public Vector getColor() {
        return color;
    }

    public Vector getNormal(Vector intersectionPoint) {
        return intersectionPoint.subtract(center).normalize();
    }

    // https://github.com/lambdabaa/RayTracer/blob/master/ray/ray/surface/Sphere.java
    // https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection.html
    // Folie 43
    public float intersect(Ray ray) {

        Vector oc = ray.getOrigin().subtract(center); // Vektor von Ursprung des Strahls zum Kugelzentrum
        float a = ray.getDirection().dotProduct(ray.getDirection());
        float b = 2 * oc.dotProduct(ray.getDirection());
        float c = oc.dotProduct(oc) - radius * radius;
        float discriminant = b * b - 4 * a * c; // wenn > 0 -> 2 Lösungen, = 0 -> 1 lösung, < 0 -> keine Lösung

         if (discriminant < 0) {
            // Keine Schnittpunkte
            return -1.0f;
         }  else {
            // Schnittpunkt/e vorhanden
            return  (-b -Math.signum(b) * (float) Math.sqrt(discriminant)) / (2 * a);
         }
    }


    public Vector getDiffuseColor(Vector intersectionPoint, Light light, Vector color) {
        Vector normal = getNormal(intersectionPoint);
        Vector intersectionToLight = intersectionPoint.subtract(light.getPosition()); //intersectionPoint -> light source
        float distance = intersectionToLight.length();
        // light.intensity() * n.scalar(richtung licht) * rgb vektor
        float diffuseCoefficient = normal.dotProduct(intersectionToLight.normalize());
        Vector finalColor = color.multiply((light.getIntensity()/(distance*distance+1))* diffuseCoefficient);

        finalColor.setX( Math.max(Math.min(finalColor.getX(),255),0));
        finalColor.setY( Math.max(Math.min(finalColor.getY(),255),0));
        finalColor.setZ( Math.max(Math.min(finalColor.getZ(),255),0));

        // diffused color
        return finalColor;
    }

}

