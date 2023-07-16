public class Torus implements Shape {
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
        this.epsilon = 0.0001f;
    }

    @Override
    public Intersection intersect(Ray ray) {
        // ray marching
        Intersection intersection = new Intersection(new Vector(0, 0, 0), -1.0f, this);
        float t = 0;
        float distance;
        int iterations = 0;
        Vector p = ray.getOrigin();
        while (iterations < maxIterations) {
            distance = calculateDistance(p);
            t += distance;
            iterations++;

            if (distance < epsilon) {
                intersection.intersection = t;
                intersection.intersectionPoint = ray.getOrigin().add(ray.getDirection().multiply(t - epsilon));
                return intersection;
            }
            p = p.add(ray.getDirection().multiply(distance));
        }
        return intersection;
    }

    @Override
    public Vector getNormal(Intersection intersection) {
        return calculateNormal(intersection.intersectionPoint);
    }

    @Override
    public Material getMaterial(Intersection intersection) {
        return material;
    }

    private Vector calculateNormal(Vector point) {
        float px = point.getX();
        float py = point.getY();
        float pz = point.getZ();

        float x = calculateDistance(new Vector(px + epsilon, py, pz)) - calculateDistance(new Vector(px - epsilon, py, pz));
        float y = calculateDistance(new Vector(px, py + epsilon, pz)) - calculateDistance(new Vector(px, py - epsilon, pz));
        float z = calculateDistance(new Vector(px, py, pz + epsilon)) - calculateDistance(new Vector(px, py, pz  - epsilon));

        return new Vector(x, y, z).normalize();
    }

    float calculateDistance(Vector point) {
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
}