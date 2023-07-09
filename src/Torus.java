public class Torus implements CSGObject {
    Vector center;
    float majorRadius;
    float minorRadius;
    Material material;
    int maxIterations;
    final float epsilon;

    public Torus(Vector center, float majorRadius, float minorRadius, Material material) {
        this.center = center;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.material = material;
        this.maxIterations = 100;
        this.epsilon = 0.01f;
    }

    @Override
    public Intersection intersect(Ray ray) {
        // ray marching
        float t = 0;
        float distance = 0;
        int iterations = 0;
        while (distance < majorRadius && iterations < maxIterations) {
            Vector p = ray.getOrigin().add(ray.getDirection().multiply(t));
            distance = calculateDistance(p);
            t += distance;
            iterations++;
        }
        if (iterations >= maxIterations) {
            // no intersection within number of max iterations
            return new Intersection(new Vector(0, 0, 0), -1.0f, null);
        }
        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().multiply(t - epsilon));
        return new Intersection(intersectionPoint, t, this);
    }

    @Override
    public Vector getNormal(Intersection intersection) {
        return calculateNormal(intersection.intersectionPoint);
    }

    @Override
    public Material getMaterial(Intersection intersection) {
        return material;
    }

    private float calculateDistance(Vector point) {
        float cx = center.getX();
        float cy = center.getY();
        float cz = center.getZ();

        float px = point.getX();
        float py = point.getY();
        float pz = point.getZ();

        float x = (float) Math.sqrt((px - cx) * (px - cx) + (pz - cz) * (pz - cz)) - majorRadius;
        float y = py - cy;
        return (float) Math.sqrt(x * x + y * y) - minorRadius;
    }

    private Vector calculateNormal(Vector point) {
        float cx = center.getX();
        float cy = center.getY();
        float cz = center.getZ();

        float px = point.getX();
        float py = point.getY();
        float pz = point.getZ();

        float x = 2 * (px - cx);
        float y = 2 * (py - cy);
        float z = 2 * (pz - cz);

        float length = (float) Math.sqrt(x * x + y * y + z * z);
        return new Vector(x / length, y / length, z / length);
    }
}