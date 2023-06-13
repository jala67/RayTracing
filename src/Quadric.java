public class Quadric implements CSGObject {
    float a, b, c, d, e, f, g, h, i, j;
    Material material;

    public static void main(String[] args) {
        //Quadric q = new Quadric(1,2,3,4,5,6,7,8,9,10, new Vector(0f, 2f, -8f), new Vector(0, 255, 0));
        /*System.out.println(q.ursprung.werte[0] + " " + q.ursprung.werte[4] + " " + q.ursprung.werte[8] + " " + q.ursprung.werte[12] );
        System.out.println(q.ursprung.werte[1] + " " + q.ursprung.werte[5] + " " + q.ursprung.werte[9] + " " + q.ursprung.werte[13] );
        System.out.println(q.ursprung.werte[2] + " " + q.ursprung.werte[6] + " " + q.ursprung.werte[10] + " " + q.ursprung.werte[14]);
        System.out.println(q.ursprung.werte[3] + " " + q.ursprung.werte[7] + " " + q.ursprung.werte[11] + " " + q.ursprung.werte[15]);*/
    }
    public Quadric(float a, float b, float c, float d, float e, float f, float g, float h, float i, float j, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.material = material;
    }
    public Quadric(Matrix4 matrix, Material mat){
        this.a = matrix.matrix[0][0];
        this.b = matrix.matrix[1][1];
        this.c = matrix.matrix[2][2];
        this.d = matrix.matrix[0][1];
        this.e = matrix.matrix[0][2];
        this.f = matrix.matrix[1][2];
        this.g = matrix.matrix[0][3];
        this.h = matrix.matrix[1][3];
        this.i = matrix.matrix[2][3];
        this.j = matrix.matrix[3][3];
        this.material = mat;
    }

    // 3 main transformations
    public Quadric translate(Vector transVec){
        Matrix4 outputMatrix;
        Matrix4 matQuadric = new Matrix4(new float[]{a, d, e, g, d, b, f, h, e, f, c, i, g, h, i, j});
        Matrix4 matTranslate = new Matrix4().translate(-transVec.x,-transVec.y,-transVec.z);
        Matrix4 matTranslateTranspose = new Matrix4().translate(-transVec.x,-transVec.y,-transVec.z).transpose();

        outputMatrix = matTranslateTranspose.multiply(matQuadric).multiply(matTranslate);

        return new Quadric(outputMatrix,material);
    }

    public Quadric scale(Vector scaleVec){
        Matrix4 outputMatrix;

        scaleVec.x = 1 / scaleVec.x;
        scaleVec.y = 1 / scaleVec.y;
        scaleVec.z = 1 / scaleVec.z;

        Matrix4 matQuadric = new Matrix4(new float[]{a, d, e, g, d, b, f, h, e, f, c, i, g, h, i, j});
        Matrix4 matScale = new Matrix4().scale(scaleVec.x,scaleVec.y,scaleVec.z);
        Matrix4 matScaleTranspose = new Matrix4().scale(scaleVec.x,scaleVec.y,scaleVec.z).transpose();

        outputMatrix = matScaleTranspose.multiply(matQuadric).multiply(matScale);

        return new Quadric(outputMatrix, material);
    }

    public Quadric rotate(Vector rotateVec, float angle){
        Matrix4 outputMatrix;
        Matrix4 matQuadric = new Matrix4(new float[]{a, d, e, g, d, b, f, h, e, f, c, i, g, h, i, j});
        Matrix4 matRotate = new Matrix4().rotateVector(rotateVec,-angle);
        Matrix4 matRotateTranspose = new Matrix4().rotateVector(rotateVec,-angle).transpose();

        outputMatrix = matRotateTranspose.multiply(matQuadric).multiply(matRotate);

        return new Quadric(outputMatrix, material);
    }

    public Material getMaterial(Intersection intersection) {
        return this.material;
    }

    public Vector getNormal(Vector point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        float nx = a*x + d*y+ e*z +g;
        float ny = b*y + d*x + f*z +h;
        float nz = c*z + e*x + f*y +i;

        Vector normal = new Vector(nx, ny, nz);
        normal.normalize();

        return normal;
    }
//platzierung
    public Vector getColor(Intersection intersection, Light light, Material material, Vector rayOrigin) {
        Vector normal = getNormal(intersection.intersectionPoint);
        Vector intersectionToLight = light.getPosition().subtract(intersection.intersectionPoint);
       // float distance = intersectionToLight.length();

        Vector viewDirection = rayOrigin.subtract(intersection.intersectionPoint).normalize();
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
        System.out.println("Fresnel: " + F + "\tNormal: " + D + "\tGeometry: " + G);

        float ks = D*F*G;
        float kd = (1-ks)*(1-material.metalness);

        // gamma-correction
        float red = (float) Math.pow(material.getColor().getX(),2.2f);
        float green = (float) Math.pow(material.getColor().getY(),2.2f);
        float blue = (float) Math.pow(material.getColor().getZ(),2.2f);
        material.color.setX(red);
        material.color.setY(green);
        material.color.setZ(blue);

        // specular color (Cook Torrance)
        Vector lightColor = new Vector(1f,1f,1f);
        Vector multiplicateVec = material.getColor().multiply(kd).add(new Vector(ks,ks,ks));
        Vector specularColor = lightColor.multiply(NdotL* light.getIntensity());

        specularColor.setX(specularColor.getX()*multiplicateVec.getX());
        specularColor.setY(specularColor.getY()*multiplicateVec.getY());
        specularColor.setZ(specularColor.getZ()*multiplicateVec.getZ());

        // gamma-correction
        red = (float) Math.pow(material.getColor().getX(),(1/2.2f));
        green = (float) Math.pow(material.getColor().getY(),(1/2.2f));
        blue = (float) Math.pow(material.getColor().getZ(),(1/2.2f));
        material.color.setX(red);
        material.color.setY(green);
        material.color.setZ(blue);

        specularColor.clamp(0, 1);
        // final color
        return specularColor.multiply(255);
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

    public Intersection intersect(Ray ray, Quadric quadric) {
        Vector origin = ray.getOrigin();

        float a = this.a;
        float b = this.b;
        float c = this.c;
        float d = this.d;
        float e = this.e;
        float f = this.f;
        float g = this.g;
        float h = this.h;
        float i = this.i;
        float j = this.j;

        // quadratic equation coefficients
        float A = a * ray.getDirection().getX() * ray.getDirection().getX() +
                b * ray.getDirection().getY() * ray.getDirection().getY() +
                c * ray.getDirection().getZ() * ray.getDirection().getZ() +
                2 * (d * ray.getDirection().getX() * ray.getDirection().getY() +
                e * ray.getDirection().getX() * ray.getDirection().getZ() +
                f * ray.getDirection().getY() * ray.getDirection().getZ());

        float B = 2 * (a * origin.getX() * ray.getDirection().getX() +
                b * origin.getY() * ray.getDirection().getY() +
                c * origin.getZ() * ray.getDirection().getZ() +
                d * (origin.getX() * ray.getDirection().getY() + origin.getY() * ray.getDirection().getX()) +
                e * (origin.getX() * ray.getDirection().getZ() + origin.getZ() * ray.getDirection().getX()) +
                f * (origin.getY() * ray.getDirection().getZ() + origin.getZ() * ray.getDirection().getY()) +
                g * ray.getDirection().getX() +
                h * ray.getDirection().getY() +
                i * ray.getDirection().getZ());

        float C = a * origin.getX() * origin.getX() +
                b * origin.getY() * origin.getY() +
                c * origin.getZ() * origin.getZ() +
                2 * (d * origin.getX() * origin.getY() +
                e * origin.getX() * origin.getZ() +
                f * origin.getY() * origin.getZ() +
                g * origin.getX() +
                h * origin.getY() +
                i * origin.getZ()) +
                j;

        float discriminant = B * B - 4 * A * C;

        if (discriminant < 0) {
            // no intersection
            return new Intersection(new Vector(0, 0, 0), -1.0f, null);
        } else {
            // intersection points
            float sqrtDiscriminant = (float) Math.sqrt(discriminant);
            float t1 = (-B - sqrtDiscriminant) / (2.0f * A);
            float t2 = (-B + sqrtDiscriminant) / (2.0f * A);

            // return smallest positive intersection point
            if (t1 > 0 && t2 > 0) {
                if (t1 < t2) {
                    Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(t1));
                    Intersection intersection = new Intersection(intersectionPoint, t1, quadric);
                    intersection.entryIntersection = t1;
                    intersection.exitIntersection = t2;
                    return intersection;
                } else {
                    Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(t2));
                    Intersection intersection = new Intersection(intersectionPoint, t2, quadric);
                    intersection.entryIntersection = t2;
                    intersection.exitIntersection = t1;
                    return intersection;
                }
            } else if (t1 > 0) {
                Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(t1));
                return new Intersection(intersectionPoint, t1, quadric);
            } else if (t2 > 0) {
                Vector intersectionPoint = ray.getOrigin().add(ray.getDirection().normalize().multiply(t2));
                return new Intersection(intersectionPoint, t2, quadric);
            } else {
                // no positive intersection point
                return new Intersection(new Vector(0, 0, 0), -1.0f, null);
            }
        }
    }
}
