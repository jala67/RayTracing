public class Intersection {
    float entryIntersection;
    float exitIntersection;
    Vector intersectionPoint;
    float intersection; // distance from rayOrigin to intersection
    CSGObject object;

    public Intersection(Vector intersectionPoint, float intersection, CSGObject object) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.object = object;
    }
}
