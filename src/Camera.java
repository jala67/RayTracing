public class Camera {
    // camera
    Vector position; // Position der Kamera im Raum
    Vector direction; // Blickrichtung
    Vector up; // oben in der Bildebene
    Vector right; // rechts in der Bildebene

    // image layer
    float aspectRatioX;
    float aspectRatioY;
    int imageWidth;
    int imageHeight;
    Vector imageStartPixel;

    public Camera(int imageWidth,int imageHeight) {
        this.position = new Vector(0, -3f, 7);
        this.up = new Vector(0,1,0);
        this.right = new Vector(1,0,0);
        this.direction = new Vector(0, 0.25f, -1);

        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.aspectRatioX = imageWidth / (float)imageHeight;
        this.aspectRatioY = (imageHeight * aspectRatioX) / imageWidth;
        this.imageStartPixel = direction.add(right.multiply(-1).multiply(aspectRatioX * 0.5f).add(up.multiply(-1).multiply(aspectRatioY * 0.5f)));
    }

    public Ray getRay(float x, float y) {
        return new Ray(this.position,imageStartPixel.add(right.multiply(x * (aspectRatioX/imageWidth))).add(up.multiply( y * (aspectRatioY/imageHeight))));
    }
}

