public class Intersection {
    float entryIntersection;
    float exitIntersection;
    Vector intersectionPoint;
    float intersection;
    Quadric quadric;

    public Intersection(Vector intersectionPoint, float intersection, Quadric quadric) {
        this.intersectionPoint = intersectionPoint;
        this.intersection = intersection;
        this.quadric = quadric;
    }
}
