public class Intersection {
    float entryIntersection;
    float exitIntersection;
    Vector intersectionPoint;
    float intersection;
    Quadric quadric;
    CSGObject object;

    public Intersection(Vector intersectionPoint, float intersection, Quadric quadric) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.quadric = quadric;
    }

    public Intersection(Vector intersectionPoint, float intersection, CSGObject object) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.object = object;
    }
}
