package kendzi.kendzi3d.service.viewer.module;

import java.util.List;

import kendzi.kendzi3d.world.quad.layer.Layer;

public class LayerConfiguration {

    private List<Layer> layers;

    /**
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers
     *            the layers to set
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

}
