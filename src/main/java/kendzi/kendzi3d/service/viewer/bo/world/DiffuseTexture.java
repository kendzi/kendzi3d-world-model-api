package kendzi.kendzi3d.service.viewer.bo.world;

public class DiffuseTexture extends Diffuse {

    private String texture0;

    public DiffuseTexture() {
        super(DiffuseType.texture);
    }

    public DiffuseTexture(String texture0) {
        super(DiffuseType.texture);
        this.texture0 = texture0;
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
}
