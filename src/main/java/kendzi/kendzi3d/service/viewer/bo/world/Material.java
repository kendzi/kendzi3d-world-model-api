package kendzi.kendzi3d.service.viewer.bo.world;

import java.awt.Color;

public class Material {

    private Color ambient;

    private Diffuse diffuse;

    private Color specular;

    private Integer shininess;

    /**
     * @return the ambient
     */
    public Color getAmbient() {
        return ambient;
    }

    /**
     * @param ambient
     *            the ambient to set
     */
    public void setAmbient(Color ambient) {
        this.ambient = ambient;
    }

    /**
     * @return the diffuse
     */
    public Diffuse getDiffuse() {
        return diffuse;
    }

    /**
     * @param diffuse
     *            the diffuse to set
     */
    public void setDiffuse(Diffuse diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * @return the specular
     */
    public Color getSpecular() {
        return specular;
    }

    /**
     * @param specular
     *            the specular to set
     */
    public void setSpecular(Color specular) {
        this.specular = specular;
    }

    /**
     * @return the shininess
     */
    public Integer getShininess() {
        return shininess;
    }

    /**
     * @param shininess
     *            the shininess to set
     */
    public void setShininess(Integer shininess) {
        this.shininess = shininess;
    }
}
