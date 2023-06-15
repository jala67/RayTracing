import java.util.List;

public interface CSGObject {
    Intersection intersect(Ray ray, CSGObject object);
    Vector getColor(Intersection intersection, Light light, Material material, Ray ray, List<CSGObject> objects, int maxDepth);
    Material getMaterial(Intersection intersection);
}