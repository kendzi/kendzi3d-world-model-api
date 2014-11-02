package kendzi.kendzi3d.service.viewer.bo.world;

public abstract class Diffuse {

    private DiffuseType type;

    public Diffuse(DiffuseType type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public DiffuseType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(DiffuseType type) {
        this.type = type;
    }
}
