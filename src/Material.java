public class Material {
    Vector color;
    float roughness;
    float metalness;
    float shinyness; // = 1f - roughness
    float tranparency;


    public Material(Vector color, float roughness, float metalness, float shinyness) {
        this.color = color;
        this.roughness = roughness;
        this.metalness = metalness;
        this.shinyness = shinyness;
    }

    public Vector getColor() { return this.color; }
    public float getRoughness() { return this.roughness; }
    public float getMetalness() { return this.metalness; }
    public float getShinyness() {
        return shinyness;
    }
    public void setColor(Vector color) { this.color = color; }
}