import java.awt.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RayTracer {
    List<Light> lights;
    static List<CSGObject> objects;
    private static Camera camera;

    public RayTracer() {
        this.lights = new ArrayList<>();
        this.objects = new ArrayList<>();
        camera = new Camera(1920, 1080);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void addObject(CSGObject object) {
        objects.add(object);
    }

    public static void main(String[] args) {

        RayTracer rayTracer = new RayTracer();

        // create quadrics
        
        Quadric quadric2 = new Quadric(1f, -1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.4f, 0.8f, 0.3f), 0.8f, 0f, 0.2f, 0f, 0f)); //roughness+shinyness = 1
        Quadric sphereQuadricShadow = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(1f, 0f, 1f), 0.05f, 0f, 0.95f, 1.5f, 1f));
        Quadric sphereBackGround = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(1f, 1f, 1f), 1f, 0f, 0f, 0f, 0f));
        Quadric sphereQuadric = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.5f, 0f, 0.5f, 1.5f, 0.3f));
        Quadric sphereQuadric2 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0f, 0f, 1f), 0.7f, 0f, 0.3f, 1.5f, 0.7f));
        Quadric sphereQuadric3 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.3f, 0f, 0.7f, 1.5f, 1f));
        Quadric sphereQuadric4 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(1, 0f, 0f), 0.2f, 0f, 0.8f, 0, 0f));
        Quadric sphereQuadric5 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.4f, 0f, 0.6f, 1.5f, 0.3f));
        Quadric sphereQuadric6 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(1,1,0f), 0.4f, 0f, 0.6f, 1.5f, 0.2f));

        //transform quadrics
        sphereQuadric = sphereQuadric.translate(new Vector(3, 0, -5)).scale(new Vector(1, 1, 1));
        sphereQuadric2 = sphereQuadric2.translate(new Vector(2, 0, -5));
        sphereQuadric3 = sphereQuadric3.translate(new Vector(-2, -1, -6.2f));
        sphereQuadric4 = sphereQuadric4.translate(new Vector(-3, -1, -6));
        sphereQuadric5 = sphereQuadric5.translate(new Vector(-3, 2, -6));
        sphereQuadric6 = sphereQuadric6.translate(new Vector(-3.5f, 2, -4));
        quadric2 = quadric2.translate(new Vector(1, 0, -8f));
        sphereQuadricShadow = sphereQuadricShadow.translate(new Vector(2.5f, 1.5f, -3));
        sphereBackGround = sphereBackGround.scale(new Vector(50, 50, 50)).translate(new Vector(0, 0, -70));

        //CSG operations
        CSG csgUnion = new CSG(sphereQuadric3, sphereQuadric4, "union");
        CSG csgIntersection = new CSG(sphereQuadric, sphereQuadric2, "intersection");
        CSG csgDifference = new CSG(sphereQuadric5, sphereQuadric6, "difference");

        // add objects
        rayTracer.addObject(sphereQuadricShadow);
        rayTracer.addObject(quadric2);
        rayTracer.addObject(sphereQuadric4);
        rayTracer.addObject(sphereQuadric6);
        //rayTracer.addObject(csgDifference);
        /*rayTracer.addObject(csgIntersection);
        rayTracer.addObject(csgUnion);*/
        rayTracer.addObject(sphereBackGround);

        Light light1 = new Light(new Vector(2, 2, 5), 1f);
        rayTracer.addLight(light1);

        int[] pixels = new int[camera.imageWidth * camera.imageHeight];

        for (int y = 0; y < camera.imageHeight; y++) {
            for (int x = 0; x < camera.imageWidth; x++) {

                Ray ray = camera.getRay(x, y); // änderung
                Vector color = rayTracer.getColor(ray, 4).multiply(255);

                // Set pixel color
                int r = (int) color.getX();
                int g = (int) color.getY();
                int b = (int) color.getZ();

                pixels[y * camera.imageWidth + x] = (255 << 24) | (r << 16) | (g << 8) | b;
            }
        }

        MemoryImageSource mis = new MemoryImageSource(camera.imageWidth, camera.imageHeight, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), pixels, 0, camera.imageWidth);
        Image image = Toolkit.getDefaultToolkit().createImage(mis);

        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private Vector calculateRefractionDirection(Vector incidentDirection, Vector surfaceNormal, float refractionIndex) {

        float i1 = 1.0f; // Brechungsindex des Mediums, aus dem der Strahl eintritt (normalerweise 1 für Vakuum oder Luft)
        float i2 = refractionIndex; // Brechungsindex des Mediums, in das der Strahl eintritt
        float i = i1 / i2;

        if ( incidentDirection.multiply(-1).dotProduct(surfaceNormal) < 0){
            surfaceNormal = surfaceNormal.multiply(-1);
            i = i2/i1;
        }
        float a = incidentDirection.multiply(-1).dotProduct(surfaceNormal);
        float b = (float) Math.sqrt(1-i*i*(1-a*a));
        Vector refractVector = incidentDirection.multiply(i).add(surfaceNormal.multiply(i*a-b));

        return  refractVector;

    }

    private float calculateMicrofacetDistribution(float NdotH, float roughnessSquared) {
        return (float) (roughnessSquared/(Math.PI*Math.pow(NdotH*NdotH*(roughnessSquared-1)+1, 2)));
    }

    private float calculateGeometricTerm(float NdotV, float NdotL, float roughness) {
        return NdotV / (NdotV * (1 - roughness / 2) + roughness / 2) * NdotL / (NdotL * (1 - roughness / 2) + roughness / 2);
    }

    private float calculateFresnelTerm(float F0, float NdotV) {
        return  F0 + (1 - F0) * (float) Math.pow(1 - NdotV, 5);
    }

    public Vector getColor(Ray ray, int maxDepth) {

        if (maxDepth == 0) {
            return new Vector(0, 0, 0);
        }

        Intersection closestIntersection = new Intersection(null, Float.POSITIVE_INFINITY, null);
        Intersection tmp = null;
        int idx=-1;
        for (int i = 0; i < objects.size(); i++) {

            Intersection intersection = objects.get(i).intersect(ray);
            if (intersection.intersection > 0 && intersection.intersection < closestIntersection.intersection) {
                idx =i;
                closestIntersection = intersection;
                tmp = intersection;
                // Vector intersectionVector = ray.getOrigin().add(ray.getDirection().normalize().multiply(closestIntersection)); // pos geaddet um kamera zu bewegen
            }
        }
        if (tmp == null) {
            return new Vector(0.05f, 0.05f, 0.05f);
        }

        Vector normal = objects.get(idx).getNormal(tmp);
        Vector intersectionToLight = lights.get(0).getPosition().subtract(tmp.intersectionPoint);
        // float distance = intersectionToLight.length();

        // Check for shadows
        boolean isInShadow = false;
        Ray shadowRay = new Ray(tmp.intersectionPoint.add(intersectionToLight.multiply(0.005f)), intersectionToLight);
        for (CSGObject object : objects) {
            Intersection shadowIntersection = object.intersect(shadowRay);
            if (shadowIntersection.intersection > 0 && shadowIntersection.intersection < intersectionToLight.length()) {
                isInShadow = true;
                break;
            }
        }
        // Calculate shadow factor
        float shadowFactor = isInShadow ? 0.2f : 1.0f; // Adjust the shadow darkness here (e.g., 0.5f for 50% darkness)

        // Calculate reflection color

        Vector reflectionColor = new Vector(0, 0, 0);
        Vector refractionColor = new Vector(0, 0, 0);

        if (tmp.quadric.material.getShinyness() > 0) {
            Vector reflectedDirection = ray.getDirection().subtract(normal.multiply(2 * ray.getDirection().dotProduct(normal)));
            Vector reflectionRayOrigin = tmp.intersectionPoint.add(reflectedDirection.multiply(0.001f)); // add small value to avoid hitting the same object
            Ray reflectedRay = new Ray(reflectionRayOrigin, reflectedDirection);
            Vector reflectedColor = getColor( reflectedRay, maxDepth - 1);

            reflectionColor = reflectedColor;
        }
        if (tmp.quadric.material.getTransparency() > 0) {
            Vector refractedDirection = calculateRefractionDirection(ray.getDirection(),normal,tmp.quadric.material.getTransmission());
            Vector refractedRayOrigin = tmp.intersectionPoint.add(refractedDirection.multiply(0.001f));
            Ray refractionRay = new Ray(refractedRayOrigin,refractedDirection);
            Vector refractedColor =getColor(refractionRay, maxDepth-1);

            reflectionColor =refractedColor;
        }



        // Calculate refraction color
        /*Vector refractedColor = new Vector(0, 0, 0);
        if (material.counter == 1 && material.transparency > 0) {
            Vector refractedDirection = calculateRefractionDirection(ray.getDirection(), normal, material.transmission);
            Ray refractedRay = new Ray(intersection.intersectionPoint, refractedDirection);
            material.counter = 0;
            refractedColor = getColor(intersection, light, material, refractedRay, objects, maxDepth - 1);
        }*/

        Vector viewDirection = ray.getOrigin().subtract(tmp.intersectionPoint).normalize();
        Vector lightDirection = intersectionToLight.normalize();
        Vector halfway = viewDirection.add(lightDirection).normalize();

        float F0 = 0.04f;
        float NdotL = normal.dotProduct(lightDirection);
        float NdotV = normal.dotProduct(viewDirection);
        float NdotH = normal.dotProduct(halfway);
        float roughnessSquared = tmp.quadric.material.getRoughness() * tmp.quadric.material.getRoughness();

        // Cook-Torrance terms
        float D = calculateMicrofacetDistribution(NdotH, roughnessSquared);
        float G = calculateGeometricTerm(NdotV, NdotL, tmp.quadric.material.roughness);
        float F = calculateFresnelTerm(F0, NdotV);
        // System.out.println("Fresnel: " + F + "\tNormal: " + D + "\tGeometry: " + G);

        float ks = D * F * G;
        float kd = (1 - ks) * (1 - tmp.quadric.material.metalness);

        // gamma-correction
        float red = (float) Math.pow(tmp.quadric.material.getColor().getX(), 2.2f);
        float green = (float) Math.pow(tmp.quadric.material.getColor().getY(), 2.2f);
        float blue = (float) Math.pow(tmp.quadric.material.getColor().getZ(), 2.2f);
        tmp.quadric.material.color.setX(red);
        tmp.quadric.material.color.setY(green);
        tmp.quadric.material.color.setZ(blue);

        // specular color (Cook Torrance)
        Vector lightColor = new Vector(1f, 1f, 1f);
        Vector multiplicateVec = tmp.quadric.material.getColor().multiply(kd).add(new Vector(ks, ks, ks));
        Vector specularColor = lightColor.multiply(NdotL * lights.get(0).getIntensity());

        specularColor.setX(specularColor.getX() * multiplicateVec.getX());
        specularColor.setY(specularColor.getY() * multiplicateVec.getY());
        specularColor.setZ(specularColor.getZ() * multiplicateVec.getZ());

        // gamma-correction
        red = (float) Math.pow(tmp.quadric.material.getColor().getX(), (1 / 2.2f));
        green = (float) Math.pow(tmp.quadric.material.getColor().getY(), (1 / 2.2f));
        blue = (float) Math.pow(tmp.quadric.material.getColor().getZ(), (1 / 2.2f));
        tmp.quadric.material.color.setX(red);
        tmp.quadric.material.color.setY(green);
        tmp.quadric.material.color.setZ(blue);

        specularColor.clamp(0, 1);
        // final color
        Vector finalColor = specularColor.multiply(shadowFactor);
        finalColor = finalColor.multiply(1-tmp.quadric.material.getShinyness());
        finalColor = finalColor.multiply(1-tmp.quadric.material.getTransparency());
        return finalColor.add(reflectionColor.multiply(tmp.quadric.material.getShinyness())).add(refractionColor.multiply(tmp.quadric.material.getTransparency()));  // add(refractedColor.multiply(material.transparency)) auskommentiert
    }
}

