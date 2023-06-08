public class Material {
    Vector color;
    float roughness;
    float metalness;


    public Material(Vector color, float roughness, float metalness) {
        this.color = color;
        this.roughness = roughness;
        this.metalness = metalness;
    }

    public Vector getColor() { return this.color; }
    public float getRoughness() { return this.roughness; }
    public float getMetalness() { return this.metalness;}
    public void setColor(Vector color) { this.color = color;}
}