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
    List<CSGObject> objects;
    private static Camera camera;

    public RayTracer() {
        this.lights = new ArrayList<>();
        this.objects = new ArrayList<>();
        camera = new Camera(1920,1080);
    }

    public void addLight(Light light) {
        lights.add(light);
    }
    public void addObject(CSGObject object){objects.add(object);}

    public static void main(String[] args) {

        RayTracer rayTracer = new RayTracer();

        // create quadrics
        Quadric cone = new Quadric(1, -1, 1, 0, 0, 0, 0, 0, 0, 0, new Material(new Vector(0.8f, 0.5f, 0.1f), 0.1f, 0f));
        Quadric quadric2 = new Quadric(1f, -1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.4f, 0.8f, 0.3f), 0.1f, 0f));
        Quadric sphereQuadric = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.1f, 0f));
        Quadric sphereQuadric2 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0f, 0f, 1f), 0.1f, 0f));
        Quadric sphereQuadric3 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.8f, 0f));
        Quadric sphereQuadric4 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.2f, 0.1f, 0.8f), 0.8f, 0f));
        Quadric sphereQuadric5 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.8f, 0.1f, 0.5f), 0.1f, 0f));
        Quadric sphereQuadric6 = new Quadric(1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, new Material(new Vector(0.2f, 0.1f, 0.8f), 0.1f, 0f));

        //transform quadrics
        sphereQuadric = sphereQuadric.translate(new Vector(3,0,-5)).scale(new Vector(1,1,1));
        sphereQuadric2 = sphereQuadric2.translate(new Vector(2,0,-5));
        sphereQuadric3 = sphereQuadric3.translate(new Vector(-2,-1,-6.2f));
        sphereQuadric4 = sphereQuadric4.translate(new Vector(-3,-1,-6));
        sphereQuadric5 = sphereQuadric5.translate(new Vector(-3,2,-6));
        sphereQuadric6 = sphereQuadric6.translate(new Vector(-3.5f,2,-4));
        quadric2 = quadric2.translate(new Vector(1, 0, -8f));

        //CSG operations
        CSG csgUnion = new CSG(sphereQuadric3, sphereQuadric4, "union");
        CSG csgIntersection = new CSG(sphereQuadric, sphereQuadric2, "intersection");
        CSG csgDifference = new CSG(sphereQuadric5, sphereQuadric6, "difference");

        // add objects
        rayTracer.addObject(quadric2);
        rayTracer.addObject(csgDifference);
        rayTracer.addObject(csgIntersection);
        rayTracer.addObject(csgUnion);

        Light light1 = new Light(new Vector(2, 2 ,5), 1f);
        rayTracer.addLight(light1);

        int[] pixels = new int[camera.imageWidth * camera.imageHeight];

        for (int y = 0; y < camera.imageHeight; y++) {
            for (int x = 0; x < camera.imageWidth; x++) {

                Ray ray = camera.getRay( x, y); // änderung
                Vector color = new Vector(0, 0, 0); // background color

                Intersection closestIntersection = new Intersection(null, Float.POSITIVE_INFINITY, null);
                for (CSGObject object : rayTracer.objects) {
                    Intersection i = object.intersect(ray, quadric2);
                    if (i.intersection > 0 && i.intersection < closestIntersection.intersection) {
                        closestIntersection = i;
                       // Vector intersectionVector = ray.getOrigin().add(ray.getDirection().normalize().multiply(closestIntersection)); // pos geaddet um kamera zu bewegen
                        color = object.getColor(closestIntersection, light1, object.getMaterial(closestIntersection), ray.getOrigin()); // änderung
                    }
                }

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
}
