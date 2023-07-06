import java.util.Random;

public class Vector {
     float x;
     float y;
     float z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    // Vector operations
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector normalize() {
        if (length() > 0) {
            float invLength = 1.0f / length();
            x = x * invLength;
            y = y * invLength;
            z = z * invLength;
        }
        return new Vector(x, y, z);
    }

    public void clamp(float min, float max) {

        x = Math.max(min, Math.min(x, max));
        y = Math.max(min, Math.min(y, max));
        z = Math.max(min, Math.min(z, max));
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector multiply(float scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar); // t, scales the vector
    }
    public Vector divide(float scalar) {
        return new Vector(x / scalar, y / scalar, z / scalar); // t, scales the vector
    }

    public float dotProduct(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public float squaredLength() {
        return x * x + y * y + z * z;
    }

    public static Vector randomPoint() {
        Random random = new Random();
        Vector point;
        do {
            float x = random.nextFloat() * 1.5f - 1;
            float y = random.nextFloat() * 1.5f - 1;
            float z = random.nextFloat() * 1.5f - 1;
            point = new Vector(x, y, z);
        } while (point.squaredLength() >= 1);
        return point;
    }
}
