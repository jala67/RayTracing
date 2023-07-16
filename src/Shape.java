public interface Shape {
    Intersection intersect(Ray ray);
    Vector getNormal(Intersection intersection);
    Material getMaterial(Intersection intersection);
}