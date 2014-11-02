package kendzi.kendzi3d.service.viewer.bo.world;

import java.awt.Color;

public class DiffuseMultiLayer extends Diffuse {

    private Color diffuse;

    private String texture0;

    private String texture1;

    public DiffuseMultiLayer() {
        super(DiffuseType.multilayer);
    }

    public DiffuseMultiLayer(Color diffuse, String texture0, String texture1) {
        super(DiffuseType.multilayer);
        this.diffuse = diffuse;
        this.texture0 = texture0;
        this.texture1 = texture1;
    }

    /**
     * @return the diffuse
     */
    public Color getDiffuse() {
        return diffuse;
    }

    /**
     * @param diffuse
     *            the diffuse to set
     */
    public void setDiffuse(Color diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * @return the texture0
     */
    public String getTexture0() {
        return texture0;
    }

    /**
     * @param texture0
     *            the texture0 to set
     */
    public void setTexture0(String texture0) {
        this.texture0 = texture0;
    }

    /**
     * @return the texture1
     */
    public String getTexture1() {
        return texture1;
    }

    /**
     * @param texture1
     *            the texture1 to set
     */
    public void setTexture1(String texture1) {
        this.texture1 = texture1;
    }

}
