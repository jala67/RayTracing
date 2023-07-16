public class Intersection {
    float entryIntersection;
    float exitIntersection;
    Vector intersectionPoint;
    float intersection; // distance from rayOrigin to intersection
    Shape object;

    public Intersection(Vector intersectionPoint, float intersection, Shape object) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.object = object;
    }
}
