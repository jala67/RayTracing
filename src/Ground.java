public class Ground implements Shape {
    Vector position;
    float width;
    float height;
    Material material;

    public Ground(Vector position, float width, float height, Material material) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.material = material;
    }

    @Override
    public Intersection intersect(Ray ray) {
        // intersection point with the ground plane
        float t = -(ray.getOrigin().getY() - position.getY()) / ray.getDirection().getY();
        if (t > 0) {
            Vector intersectionPoint = ray.getPoint(t);
            float x = intersectionPoint.getX() - position.getX();
            float z = intersectionPoint.getZ() - position.getZ();
            // check if the intersection point is within the rectangle bounds
            if (Math.abs(x) <= width / 2 && Math.abs(z) <= height / 2) {
                return new Intersection(intersectionPoint, t, this);
            }
        }
        return new Intersection(new Vector(0, 0, 0), -1.0f, null);
    }

    @Override
    public Vector getNormal(Intersection intersection) {
        return new Vector(0, -1, 0);  // ground surface normal always faces the y-axis
    }

    @Override
    public Material getMaterial(Intersection intersection) {
        return material;
    }
}