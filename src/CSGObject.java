public interface CSGObject {
    Intersection intersect(Ray ray, Quadric quadric);
    Vector getColor(Intersection intersection, Light light, Material material, Vector rayOrigin);
    Material getMaterial(Intersection intersection);
}
