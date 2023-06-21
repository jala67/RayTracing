import java.util.List;

public interface CSGObject {
    Intersection intersect(Ray ray);
    Vector getNormal(Intersection intersection);
    Material getMaterial(Intersection intersection);
}