import java.awt.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RayTracer {
    List<Light> lights;
    static List<Shape> shapes;
    private static Camera camera;
    private static final int numSamples = 2; // number of rays per pixel
    BufferedImage skydomeImage = ImageIO.read(new File("SkyDome.png"));

    public RayTracer() throws IOException {
        this.lights = new ArrayList<>();
        shapes = new ArrayList<>();
        camera = new Camera(1920, 1080);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public static void main(String[] args) throws IOException {
        RayTracer rayTracer = new RayTracer();

        // create objects
        Quadric cone = new Quadric(1f, -1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.4f, 0.8f, 0.3f), 0.8f, 0f, 0.2f, 0f, 0f)); //roughness+shinyness = 1
        Quadric sphereQuadric = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.5f, 0f, 0.5f, 1.5f, 0.3f));
        Quadric sphereQuadric2 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0f, 0f, 1f), 0.7f, 0f, 0.3f, 1.5f, 0.7f));
        Quadric sphereQuadric3 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.7f, 0.1f, 0.7f), 0.7f, 0f, 0.3f, 0f, 0f));
        Quadric sphereQuadric4 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.2f, 0.8f, 0.7f), 0.7f, 0f, 0.3f, 0, 0f));
        Quadric sphereQuadric5 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.7f, 0f, 0.3f, 0f, 0f));
        Quadric sphereQuadric6 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.8f, 0f, 0.2f, 0f, 0f));
        Ground ground = new Ground(new Vector(0, 4, -10), 1000, 50, new Material(new Vector(0.9f, 0.9f, 0.9f), 0.8f, 0f, 0.2f, 0f, 0f));
        Torus torus = new Torus(new Vector(2,0,-8), 4f, 0.5f, new Material(new Vector(0.9f, 0.3f, 0.6f), 0.05f, 0f, 0.95f, 1.5f, 1f));

        // transform quadrics
        sphereQuadric = sphereQuadric.translate(new Vector(3, 0, -5.5f));
        sphereQuadric2 = sphereQuadric2.translate(new Vector(2, 0, -5.5f));
        sphereQuadric3 = sphereQuadric3.translate(new Vector(-4, -1, -7f));
        sphereQuadric4 = sphereQuadric4.translate(new Vector(-5, -1, -6));
        sphereQuadric5 = sphereQuadric5.translate(new Vector(-3, 2, -6));
        sphereQuadric6 = sphereQuadric6.translate(new Vector(-3.5f, 1, -4));
        cone = cone.translate(new Vector(2, 0, -8f));

        // CSG operations
        CSG csgUnion = new CSG(sphereQuadric3, sphereQuadric4, "union");
        CSG csgIntersection = new CSG(sphereQuadric, sphereQuadric2, "intersection");
        CSG csgDifference = new CSG(sphereQuadric5, sphereQuadric6, "difference");

        // add objects
        rayTracer.addShape(csgDifference);
        rayTracer.addShape(ground);
        rayTracer.addShape(cone);
        rayTracer.addShape(csgUnion);
        rayTracer.addShape(torus);

        Light light1 = new Light(new Vector(2, -6, 6), 1f, 1f);
        rayTracer.addLight(light1);

        int[] pixels = new int[camera.imageWidth * camera.imageHeight];

        for (int y = 0; y < camera.imageHeight; y++) {
            for (int x = 0; x < camera.imageWidth; x++) {
                Vector color = new Vector(0, 0, 0);
                Vector firstRayColor = new Vector(0, 0, 0);
                int numRays = numSamples;
                boolean similarColors = false;

                for (int s = 0; s < numSamples; s++) {
                    float offsetX = (float) (Math.random() - 0.5f);
                    float offsetY = (float) (Math.random() - 0.5f);
                    Ray ray = camera.getRay(x + offsetX, y + offsetY);
                    Vector rayColor = rayTracer.getColor(ray, 4);
                    color = color.add(rayColor);

                    if (s == 0) {
                        firstRayColor = rayColor;
                    }
                    similarColors = isSimilar(firstRayColor, rayColor);
                }

                // additional sampling if colors are not similar
                if (!similarColors) {
                    numRays += 8;
                     color = new Vector(0, 0, 0);

                    for (int s = 0; s < numRays; s++) {
                        float offsetX = (float) (Math.random() - 0.5f);
                        float offsetY = (float) (Math.random() - 0.5f);
                        Ray ray = camera.getRay(x + offsetX, y + offsetY);
                        color = color.add(rayTracer.getColor(ray, 4));
                    }
                }

                Vector averageColor = color.divide(numRays);
                int r = (int) (averageColor.getX() * 255);
                int g = (int) (averageColor.getY() * 255);
                int b = (int) (averageColor.getZ() * 255);

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

    public static boolean isSimilar(Vector vector1, Vector vector2) {
        float tolerance = 0.001f; // tolerance value
        float diffX = Math.abs(vector1.x - vector2.x);
        float diffY = Math.abs(vector1.y - vector2.y);
        float diffZ = Math.abs(vector1.z - vector2.z);
        return diffX <= tolerance && diffY <= tolerance && diffZ <= tolerance;
    }

    private Vector calculateRefractionDirection(Vector rayDirection, Vector surfaceNormal, float refractionIndex) {
        float i1 = 1.0f; // Brechungsindex des Mediums, aus dem der Strahl eintritt (normalerweise 1 für Vakuum oder Luft)
        float i2 = refractionIndex; // Brechungsindex des Mediums, in das der Strahl eintritt
        float i = i1 / i2;

        if (rayDirection.multiply(-1).dotProduct(surfaceNormal) < 0){
            surfaceNormal = surfaceNormal.multiply(-1);
            i = i2/i1;
        }
        float a = rayDirection.multiply(-1).dotProduct(surfaceNormal);
        float b = (float) Math.sqrt(1-i*i*(1-a*a)); //negativ total reflection
        Vector refractVector = rayDirection.multiply(i).add(surfaceNormal.multiply(i*a-b));
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
        int numShadowRays = 5;
        if (maxDepth == 0) {
            return new Vector(0, 0, 0);
        }

        Intersection closestIntersection = new Intersection(null, Float.POSITIVE_INFINITY, null);
        Intersection tmp = null;
        int idx = -1;
        for (int i = 0; i < shapes.size(); i++) {
            Intersection intersection = shapes.get(i).intersect(ray);
            if (intersection.intersection > 0 && intersection.intersection < closestIntersection.intersection) {
                idx = i;
                closestIntersection = intersection;
                tmp = intersection;
            }
        }
        if (tmp == null) {
            // no intersection -> skydome
            Vector direction = ray.getDirection().normalize();
            float u = 0.5f + (float) (Math.atan2(direction.getZ(), direction.getX()) / (2 * Math.PI));
            float v = 0.5f - (float) (Math.asin(direction.getY()) / Math.PI);

            int x = (int) (u * skydomeImage.getWidth());
            int y = (int) (v * skydomeImage.getHeight());

            x = Math.max(0, Math.min(x, skydomeImage.getWidth() - 1));
            y = Math.max(0, Math.min(y, skydomeImage.getHeight() - 1));

            int rgb = skydomeImage.getRGB(x, y);

            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;
            return new Vector(r, g, b);
        }

        Vector normal = shapes.get(idx).getNormal(tmp);
        Vector intersectionToLight = lights.get(0).getPosition().subtract(tmp.intersectionPoint);
        // calculate shadow factor with path tracing
        float shadowFactor = 0.3f;
        for (int i = 0; i < numShadowRays; i++) {
            // random point on the light sphere
            Vector randomPointOnLight = lights.get(0).getPosition().add(Vector.randomPoint());
            Vector shadowRayDirection = randomPointOnLight.subtract(tmp.intersectionPoint);
            Ray shadowRay = new Ray(tmp.intersectionPoint.add(shadowRayDirection.multiply(0.001f)), shadowRayDirection);
            // check for intersection with objects to determine if the point is in shadow
            boolean isInShadow = false;
            for (Shape shape : shapes) {
                if (shape.getMaterial(tmp).transparency < 0.5f) {
                    Intersection shadowIntersection = shape.intersect(shadowRay);
                    if (shadowIntersection.intersection > 0 && shadowIntersection.intersection < shadowRayDirection.length()) {
                        isInShadow = true;
                        break;
                    }
                }
            }
            if (!isInShadow) {
                shadowFactor += 1.0f / numShadowRays;
            }
        }
        // Calculate reflection color
        Vector reflectionColor = new Vector(0, 0, 0);
        Vector refractionColor = new Vector(0, 0, 0);

        Material material = tmp.object.getMaterial(tmp);

        if (material.getShinyness() > 0) {
            Vector reflectedDirection = ray.getDirection().subtract(normal.multiply(2 * ray.getDirection().dotProduct(normal)));
            Vector reflectionRayOrigin = tmp.intersectionPoint.add(reflectedDirection.multiply(0.001f)); // add small value to avoid hitting the same object
            Ray reflectedRay = new Ray(reflectionRayOrigin, reflectedDirection);
            reflectionColor = getColor( reflectedRay, maxDepth - 1);
        }
        if (material.getTransparency() > 0) {
            Vector refractedDirection = calculateRefractionDirection(ray.getDirection(), normal, material.getTransmission());
            Vector refractedRayOrigin = tmp.intersectionPoint.add(refractedDirection.multiply(0.001f));
            Ray refractionRay = new Ray(refractedRayOrigin,refractedDirection);
            reflectionColor = getColor(refractionRay, maxDepth - 1);
        }

        Vector viewDirection = ray.getOrigin().subtract(tmp.intersectionPoint).normalize();
        Vector lightDirection = intersectionToLight.normalize();
        Vector halfway = viewDirection.add(lightDirection).normalize();

        float F0 = 0.04f;
        float NdotL = normal.dotProduct(lightDirection);
        float NdotV = normal.dotProduct(viewDirection);
        float NdotH = normal.dotProduct(halfway);
        float roughnessSquared = material.getRoughness() * material.getRoughness();

        // Cook-Torrance terms
        float D = calculateMicrofacetDistribution(NdotH, roughnessSquared);
        float G = calculateGeometricTerm(NdotV, NdotL, material.roughness);
        float F = calculateFresnelTerm(F0, NdotV);

        float ks = D * F * G;
        float kd = (1 - ks) * (1 - material.metalness);

        // gamma-correction
        float red = (float) Math.pow(material.getColor().getX(), 2.2f);
        float green = (float) Math.pow(material.getColor().getY(), 2.2f);
        float blue = (float) Math.pow(material.getColor().getZ(), 2.2f);
        material.color.setX(red);
        material.color.setY(green);
        material.color.setZ(blue);

        // specular color (Cook Torrance)
        Vector lightColor = new Vector(1f, 1f, 1f);
        Vector multiplicateVec = material.getColor().multiply(kd).add(new Vector(ks, ks, ks));
        Vector specularColor = lightColor.multiply(NdotL * lights.get(0).getIntensity());

        specularColor.setX(specularColor.getX() * multiplicateVec.getX());
        specularColor.setY(specularColor.getY() * multiplicateVec.getY());
        specularColor.setZ(specularColor.getZ() * multiplicateVec.getZ());

        // gamma-correction
        red = (float) Math.pow(material.getColor().getX(), (1 / 2.2f));
        green = (float) Math.pow(material.getColor().getY(), (1 / 2.2f));
        blue = (float) Math.pow(material.getColor().getZ(), (1 / 2.2f));
        material.color.setX(red);
        material.color.setY(green);
        material.color.setZ(blue);

        specularColor.clamp(0, 1);
        // final color
        Vector finalColor = specularColor.multiply(shadowFactor);
        finalColor = finalColor.multiply(1-material.getShinyness());
        finalColor = finalColor.multiply(1-material.getTransparency());
        return finalColor.add(reflectionColor.multiply(material.getShinyness())).add(refractionColor.multiply(material.getTransparency()));
    }
}