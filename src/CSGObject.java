import java.util.List;

public interface CSGObject {
    Intersection intersect(Ray ray, Quadric quadric);
    Vector getColor(Intersection intersection, Light light, Material material, Vector rayOrigin, List<CSGObject> objects);
    Material getMaterial(Intersection intersection);
}
