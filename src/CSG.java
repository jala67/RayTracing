import java.util.List;

public class CSG implements CSGObject {
    Quadric quadric1;
    Quadric quadric2;
    String operation;

    public CSG(Quadric quadric1, Quadric quadric2, String operation) {
        this.quadric1 = quadric1;
        this.quadric2 = quadric2;
        this.operation = operation;
    }

    public Intersection intersect(Ray ray, CSGObject object) { // bad, quadric is never used (override)

        switch (operation) {
            case "union" -> {
                Intersection intersection1 = quadric1.intersect(ray, quadric1);
                Intersection intersection2 = quadric2.intersect(ray, quadric2);

                if (intersection1.intersection > 0 && intersection2.intersection > 0) {
                    if (intersection1.intersection < intersection2.intersection) {
                        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection1.intersection));
                        return new Intersection(intersectionPoint, intersection1.intersection, quadric1);
                    } else {
                        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection2.intersection));
                        return new Intersection(intersectionPoint, intersection2.intersection, quadric2);
                    }
                } else if (intersection1.intersection > 0) {
                    Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection1.intersection));
                    return new Intersection(intersectionPoint, intersection1.intersection, quadric1);
                } else if (intersection2.intersection > 0) {
                    Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection2.intersection));
                    return new Intersection(intersectionPoint, intersection2.intersection, quadric2);
                }

                return new Intersection(null, -1.0f, null);
            }
            case "intersection" -> {
                float intersection1 = quadric1.intersect(ray, quadric1).intersection;
                float intersection2 = quadric2.intersect(ray, quadric2).intersection;

                if (intersection1 > 0 && intersection2 > 0) {
                    if (intersection1 < intersection2) {
                        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection1));
                        return new Intersection(intersectionPoint, intersection2, quadric2);
                    } else {
                        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(intersection2));
                        return new Intersection(intersectionPoint, intersection1, quadric1);

                    }
                }
            }
            case "difference" -> {
                float Ain = quadric1.intersect(ray, quadric1).entryIntersection;
                float Aout = quadric1.intersect(ray, quadric1).exitIntersection;
                float Bin = quadric2.intersect(ray, quadric2).entryIntersection;
                float Bout = quadric2.intersect(ray, quadric2).exitIntersection;

                if (Ain > 0 && Bin > 0) {
                    if (Ain < Bin) {
                        Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(Ain));
                        return new Intersection(intersectionPoint, Ain, quadric1);
                    } else {
                        if (Ain < Bout && Bout < Aout) {
                            Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(Bout));
                            return new Intersection(intersectionPoint, Bout, quadric2); // needs normal from quadric2, ausgeschnittene Wölbung
                        } else if (Bout < Ain) {
                            Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(Ain));
                            return new Intersection(intersectionPoint, Ain, quadric1);
                        }
                        return new Intersection(null, -1.0f, null);
                    }
                } else if (Ain > 0) {
                    Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(Ain));
                    return new Intersection(intersectionPoint, Ain, quadric1);
                } else if (Bin > 0) {
                    return new Intersection(null, -1.0f, null);
                }
                return new Intersection(null, -1.0f, null);
            }
        }
        return new Intersection(null, -1.0f, null);
    }

    @Override
    public Vector getColor(Intersection intersection, Light light, Material material, Ray ray, List<CSGObject> objects, int maxDepth) {
        return intersection.quadric.getColor(intersection, light, intersection.quadric.material, ray, objects, maxDepth);
    }

    @Override
    public Material getMaterial(Intersection intersection) {
        return intersection.quadric.getMaterial(intersection);
    }
}
