public class Material {
    Vector color;
    float roughness;
    float metalness;
    float shinyness; // = 1f - roughness
    float transmission;
    float transparency;

    public Material(Vector color, float roughness, float metalness, float shinyness, float transmission, float transparency) {
        this.color = color;
        this.roughness = roughness;
        this.metalness = metalness;
        this.shinyness = shinyness;
        this.transmission = transmission;
        this.transparency = transparency;
    }

    public Vector getColor() { return this.color; }
    public float getRoughness() { return this.roughness; }
    public float getMetalness() { return this.metalness; }

    public float getTransparency(){ return this.transparency;}
    public float getTransmission(){ return this.transmission;}
    public float getShinyness() {
        return shinyness;
    }
    public void setColor(Vector color) { this.color = color; }
}