public class Intersection {
    float intersection; // distance from rayOrigin to entry intersection
    float exitIntersection;
    Vector intersectionPoint;
    Shape object;

    public Intersection(Vector intersectionPoint, float intersection, Shape object) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.object = object;
    }
}
