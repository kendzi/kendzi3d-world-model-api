package kendzi.kendzi3d.service.viewer.bo.world;

import java.util.List;

public class Face {

    private Material material;

    private List<Integer> indexes;

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material
     *            the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @return the indexes
     */
    public List<Integer> getIndexes() {
        return indexes;
    }

    /**
     * @param indexes
     *            the indexes to set
     */
    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

}
